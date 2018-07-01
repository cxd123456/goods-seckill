package com.miaosha.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.miaosha.config.annotation.AccessLimit;
import com.miaosha.config.redis.AccessKey;
import com.miaosha.config.redis.MiaoshaKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.miaosha.common.CodeMsg;
import com.miaosha.common.Result;
import com.miaosha.config.rabbitmq.MQSender;
import com.miaosha.config.rabbitmq.MiaoshaMessage;
import com.miaosha.config.redis.GoodsKey;
import com.miaosha.config.redis.RedisService;
import com.miaosha.entity.MiaoshaOrderEntity;
import com.miaosha.service.GoodsService;
import com.miaosha.service.MiaoshaService;
import com.miaosha.service.OrderService;
import com.miaosha.utils.IdWorker;
import com.miaosha.vo.GoodsVo;

/**
 * 秒杀controller
 *
 * @创建时间：2018年6月20日
 */
@Controller
@RequestMapping("miaosha")
public class MiaoshaController {

    private static final Logger LOG = LoggerFactory.getLogger(MiaoshaController.class);

    private Map<Long, Boolean> localOverMap = new HashMap<>(); // 本地存储是否秒杀完毕的状态，要比redis更快

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private MiaoshaService miaoshaService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private MQSender sender;

    /**
     * 未优化的秒杀接口
     * <p>
     * 模拟: 1000个用户 * 10次循环 = 10000个请求
     * <p>
     * 库存：10个
     * <p>
     * 耗时：26秒
     * <p>
     * QPS：最大到了380
     *
     * @return
     */
    @RequestMapping(value = "do_seckill")
    @ResponseBody
    public Result<Integer> doSeckill() {

        Long goodsId = 1L; // 模拟商品id
        long userId = IdWorker.getId(); // 模拟秒杀用户id

        LOG.info("===========进入秒杀==========");

        GoodsVo goodsVo = goodsService.getGoodsById(goodsId); // 判断库存
        if (goodsVo.getStock_count() <= 0) { // 库存不足 return
            Result.error(CodeMsg.MIAO_SHA_OVER);
        }

        // 判断是否已经秒杀过了，如果秒杀过，就无法再次秒杀
        MiaoshaOrderEntity order = orderService.getMiaoshaOrderByUserIdGoodsId(userId, goodsId);
        if (order != null) {
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }

        // 上面判断库存和判断是否秒杀过无法完全解决并发问题，真正解决并发问题是下面两步操作
        // 1. 减库存使用mysql排它锁，保证不会卖超
        // 2. 生成秒杀订单使用mysql唯一索引，保证秒杀过的用户无法再次秒杀
        // 可以秒杀 1.减库存 2.生成订单 3.写入秒杀订单
        miaoshaService.miaosha(userId, goodsVo);

        return Result.success(0);

    }

    @PostConstruct
    public void setGoodsStockToRedis() {
        List<GoodsVo> goodsList = goodsService.selectGoodsVoList();
        for (GoodsVo goods : goodsList) {
            redisService.set(GoodsKey.getMiaoshaGoodsStock, goods.getId().toString(), goods.getStock_count());
            localOverMap.put(goods.getId(), false); //
        }
    }

    /**
     * 优化后的秒杀接口
     * <p>
     * 模拟: 1000个用户 * 10次循环 = 10000个请求
     * <p>
     * 秒杀库存: 10个库存
     * <p>
     * 耗时：7秒
     * <p>
     * QPS: 达到1400
     * <p>
     * 可以看出，QPS提高了4倍，确实是对性能提升了太多
     * <p>
     * 秒杀接口优化核心思路：减少对数据库的访问
     * <p>
     * 秒杀地址隐藏, 思路是秒杀地址对外是不固定的, 每次都必须先请求获取秒杀地址
     * 对于秒杀请求, 还需要校验地址的正确性
     * 方式是@PathVariable, 地址传参
     * 目的: 接口 隐藏 防刷
     *
     * @return
     */
    @RequestMapping("/{path}/do_miaosha")
    @ResponseBody
    public Result<Integer> doMiaosha(@PathVariable String path) {

        // ===============优化===============

        Long goodsId = 1L; // 模拟商品id
        Long userId = 1011955828648882178L; // IdWorker.getId(); // 模拟秒杀用户id

        // 验证Path, 秒杀地址校验
        boolean check = miaoshaService.checkPath(path, userId, goodsId);
        if (!check) {
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }

        // TODO 重要的优化点，内存标记
        // 判断内存标记是否秒杀结束，这要访问redis要快，减少redis访问
        Boolean over = localOverMap.get(goodsId);
        if (over) { // 已经秒杀结束，就不再去redis中与减库存，防止所有的请求都去访问redis，但是也不能避免多余的请求去预减redis
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }

        // 预减库存
        Long stock = redisService.decr(GoodsKey.getMiaoshaGoodsStock, goodsId.toString());
        if (stock < 0) { // 库存完了
            localOverMap.put(goodsId, true); // 内存标记，库存秒杀完毕
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }

        // 判断是否已经秒杀过了，如果秒杀过，就无法再次秒杀
        MiaoshaOrderEntity order = orderService.getMiaoshaOrderByUserIdGoodsId(userId, goodsId);
        if (order != null) {
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }

        // 消息队列入队
        MiaoshaMessage message = new MiaoshaMessage(goodsId, userId);
        sender.sendMiaoshaMessage(message);

        return Result.success(0);

        /*
         * Long goodsId = 1L; // 模拟商品id long userId = IdWorker.getId(); // 模拟秒杀用户id
         *
         * LOG.info("===========进入秒杀==========");
         *
         * GoodsVo goodsVo = goodsService.getGoodsById(goodsId); // 判断库存 if
         * (goodsVo.getStock_count() <= 0) { // 库存不足 return
         * Result.error(CodeMsg.MIAO_SHA_OVER); }
         *
         * // 判断是否已经秒杀过了，如果秒杀过，就无法再次秒杀 MiaoshaOrderEntity order =
         * orderService.getMiaoshaOrderByUserIdGoodsId(userId, goodsId); if (order !=
         * null) { return Result.error(CodeMsg.REPEATE_MIAOSHA); }
         *
         * // 上面判断库存和判断是否秒杀过无法完全解决并发问题，真正解决并发问题是下面两步操作 // 1. 减库存使用mysql排它锁，保证不会卖超 // 2.
         * 生成秒杀订单使用mysql唯一索引，保证秒杀过的用户无法再次秒杀 // 可以秒杀 1.减库存 2.生成订单 3.写入秒杀订单
         * OrderInfoEntity orderInfoEntity = miaoshaService.miaosha(userId, goodsVo);
         *
         * return Result.success(orderInfoEntity);
         */

    }

    /**
     * 秒杀结果查询接口
     * <p>
     * 成功：orderId 失败：-1 排队中：0
     *
     * @return
     */
    @RequestMapping("result")
    @ResponseBody
    public Result<Long> result() {
        Long userId = 1011955828648882178L;
        Long goodsId = 1L;
        Long orderId = miaoshaService.getMiaoshaResult(userId, goodsId);
        return Result.success(orderId);
    }

    /**
     * 1. 判断图形验证码是否正确
     * 2. 获取秒杀地址接口
     *
     * @return
     */
    @AccessLimit(seconds = 5, maxCount = 5, needLogin = true)
    @RequestMapping("path")
    @ResponseBody
    public Result<String> getMiaoshaPath(HttpServletRequest request, @RequestParam(value = "verifyCode", required = false) Integer verifyCode) {
        Long goodsId = 1L;
        Long userId = 1011955828648882178L;

        // 接口防刷, 访问接口次数限制
        String uri = request.getRequestURI();
        String key = uri + "_" + userId;
        Integer count = redisService.get(AccessKey.ACCESS, key, Integer.class);
        if (count == null) {
            redisService.set(AccessKey.ACCESS, key, 1);
        } else if (count < 5) {
            redisService.incr(AccessKey.ACCESS, key);
        } else {
            return Result.error(CodeMsg.ACCESS_LIMIT);
        }


        boolean check = miaoshaService.checkVerifyCode(userId, goodsId, verifyCode);
        if (!check) {
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }

        String str = DigestUtils.md5DigestAsHex(UUID.randomUUID().toString().getBytes());
        redisService.set(MiaoshaKey.getMiaoshaPath, userId + "_" + goodsId, str);
        return Result.success(str);
    }

    @RequestMapping("verifyCode")
    @ResponseBody
    public Result<String> verifyCode(HttpServletResponse response) throws Exception {
        Long goodsId = 1L;
        Long userId = 1011955828648882178L;

        BufferedImage image = miaoshaService.createMiaoshaVerfyCode(userId, goodsId);

        ServletOutputStream outputStream = response.getOutputStream();
        ImageIO.write(image, "JPEG", outputStream);
        outputStream.flush();
        outputStream.close();

        return null;
    }

}
