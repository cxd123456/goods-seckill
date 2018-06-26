package com.miaosha.controller;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.miaosha.common.CodeMsg;
import com.miaosha.common.Result;
import com.miaosha.config.rabbitmq.MQSender;
import com.miaosha.config.rabbitmq.MiaoshaMessage;
import com.miaosha.config.redis.GoodsKey;
import com.miaosha.config.redis.RedisService;
import com.miaosha.entity.MiaoshaOrderEntity;
import com.miaosha.entity.OrderInfoEntity;
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

	@Autowired
	private MiaoshaService miaoshaService;
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private RedisService redisService;
	@Autowired
	private MQSender sender;

	@PostConstruct
	public void setGoodsStockToRedis() {
		List<GoodsVo> goodsList = goodsService.selectGoodsVoList();
		for (GoodsVo goods : goodsList) {
			redisService.set(GoodsKey.getMiaoshaGoodsStock, goods.getId().toString(), goods.getStock_count());
		}
	}

	/**
	 * QPS:
	 * 
	 * 模拟: 3000个用户 * 10次循环 = 30000个请求
	 * 
	 * 秒杀库存: 300个库存
	 * 
	 * 秒杀接口优化核心思路：减少对数据库的访问
	 * 
	 * @param model
	 * @param user
	 * @param goodsId
	 * @return
	 */
	@RequestMapping("do_miaosha")
	@ResponseBody
	public Result<Integer> doMiaosha() {

		// ===============优化===============

		Long goodsId = 1L; // 模拟商品id
		Long userId = IdWorker.getId(); // 模拟秒杀用户id

		// 预减库存
		Long stock = redisService.decr(GoodsKey.getMiaoshaGoodsStock, goodsId.toString());
		if (stock < 0) {
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
	 * 成功：orderId
	 * 失败：-1
	 * 排队中：0
	 * @return
	 */
	@RequestMapping("result")
	@ResponseBody
	public Result<Long> result() {
		Long userId = 1L;
		Long goodsId = 1L;
		Long orderId = miaoshaService.getMiaoshaResult(userId, goodsId);
		return Result.success(orderId);
	}

}
