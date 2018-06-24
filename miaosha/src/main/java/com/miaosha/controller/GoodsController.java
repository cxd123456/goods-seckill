package com.miaosha.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import com.alibaba.fastjson.JSON;
import com.miaosha.common.Result;
import com.miaosha.config.redis.GoodsKey;
import com.miaosha.config.redis.RedisService;
import com.miaosha.entity.MiaoshaUserEntity;
import com.miaosha.service.GoodsService;
import com.miaosha.service.MiaoshaUserService;
import com.miaosha.vo.GoodsVo;

/**
 * 商品展示controller
 * 
 * @创建时间：2018年6月20日
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {

	private static final Logger LOG = LoggerFactory.getLogger(GoodsController.class);

	@Autowired
	private GoodsService goodsService;
	@Autowired
	private MiaoshaUserService miaoshaUserService;
	@Autowired
	private RedisService redisService;
	@Autowired
	private ThymeleafViewResolver thymeleafViewResolver;
	@Autowired
	private ApplicationContext applicationContext;

	/**
	 * 商品列表展示
	 * 
	 * QPS: 500
	 * 
	 * 模拟：5000个并发 * 10次循环 = 50000个请求
	 * 
	 * 耗时：1分40秒
	 * 
	 * 分析：在并发访问过程中，数据库服务器CPU占用率达到了95%，负载最高到了3(mysql装在单核cpu的虚拟机上，内存1GB)，
	 * 可见，并发访问中，造成QPS低的瓶颈在数据库。
	 * 
	 * 关于负载介绍: https://zhidao.baidu.com/question/186962244.html
	 * 
	 * @param model
	 * @param response
	 * @param miaoshaUserEntity
	 * @return
	 */
	@RequestMapping(value = "to_list", produces = "text/html")	// produces，指定返回thml页面
	@ResponseBody
	public String toList(
			Model model, HttpServletResponse response, HttpServletRequest request
//			@CookieValue(value = MiaoshaUserServiceImpl.COOKIE_NAME_TOKEN, required = false) String cookieToken,
//			@RequestParam(value = MiaoshaUserServiceImpl.COOKIE_NAME_TOKEN, required = false) String paramToken
//			MiaoshaUserEntity miaoshaUserEntity
			) {

//		String token = null;
//
//		if (StringUtils.isEmpty(paramToken)) {
//			if (!StringUtils.isEmpty(cookieToken)) {
//				token = cookieToken;
//			}
//		} else {
//			token = paramToken;
//		}
////		
//		if (StringUtils.isEmpty(token)) {
//			return "/login";
//		}
//
//		MiaoshaUserEntity miaoshaUserEntity = miaoshaUserService.getByToken(token, response);
//
//		if (miaoshaUserEntity == null) {
//			return "/login";
//		}
//
//		model.addAttribute("user", miaoshaUserEntity);
		
		// 添加html页面缓存到redis，也就是redis中缓存已经被渲染好的html静态页面
		// -------------------------------------------------
		
		// 取缓存redis缓存中的thymeleaf渲染好的html页面
		String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
		if (!StringUtils.isEmpty(html)) {
			return html;
		}
		
		List<GoodsVo> list = goodsService.selectGoodsVoList();
		model.addAttribute("goodsList", list);
		
		// 手动渲染thymeleaf模板
		SpringWebContext swc = new SpringWebContext(request, response, request.getServletContext(), 
				request.getLocale(), model.asMap(), applicationContext);	// 将model中数据放到SpringWebContext
		SpringTemplateEngine templateEngine = thymeleafViewResolver.getTemplateEngine();	
		html = templateEngine.process("goods_list", swc);	// thymeleaf模板引擎手动渲染出html页面
//		if (!StringUtils.isEmpty(html)) {
//			redisService.set(GoodsKey.getGoodsList, "", html);	// 将渲染好的html页面放进缓存
//		}
		return html;
		
		// -------------------------------------------------
	}

	/**
	 * 商品列表展示
	 * 
	 * 
	 * 
	 * @return
	 */
	@RequestMapping("to_list_redis")
	@ResponseBody
	public Result<List<GoodsVo>> toListRedis() {
		// redis缓存商品列表
		// 1. 去redis取缓存
		List<GoodsVo> redisGoodsList = redisService.get(GoodsKey.getGoodsList, "toList", List.class);

		// 2. 缓存有，直接取出来，返回
		if (redisGoodsList != null) {
			LOG.info("=====从redis缓存中取数据=====");
			return Result.success(redisGoodsList);
		}

		LOG.info("=====从mysql数据库中取数据=====");
		// 3. 缓存中没有，从数据库拿，然后再放到缓存中
		List<GoodsVo> list = goodsService.selectGoodsVoList();

		if (list == null) {
			LOG.info("=====mysql数据库中无数据=====");
			return Result.success(null);
		}

		LOG.info("=====mysql数据库中数据放到redis缓存中=====");
		// 放到redis缓存
		redisService.set(GoodsKey.getGoodsList, "toList", list);

		return Result.success(list);
	}

	/**
	 * 商品详情
	 * 
	 * @param model
	 * @param goodsId
	 * @param user
	 * @return
	 */
	@RequestMapping("to_detail/{goodsId}")
	public String toDetail(Model model, @PathVariable(value = "goodsId") Long goodsId, MiaoshaUserEntity user) {
		GoodsVo goods = goodsService.getGoodsById(goodsId);

		long startTime = goods.getStart_time().getTime();
		long endTime = goods.getEnd_time().getTime();
		long nowTime = System.currentTimeMillis();

		int miaoshaStatus = 0;
		int remainSeconds = 0;

		if (startTime > nowTime) { // 秒杀未开始
			miaoshaStatus = 0;
			remainSeconds = (int) ((startTime - nowTime) / 1000);
		} else if (nowTime > endTime) { // 秒杀已结束
			miaoshaStatus = 2;
			remainSeconds = -1;
		} else { // 秒杀进行中
			miaoshaStatus = 1;
			remainSeconds = 0;
		}

		model.addAttribute("user", user);
		model.addAttribute("goods", goods);
		model.addAttribute("miaoshaStatus", miaoshaStatus);
		model.addAttribute("remainSeconds", remainSeconds);
		return "goods_detail";
	}

	public static void main(String[] args) {
		List<GoodsVo> list = new ArrayList<>();

		GoodsVo gv = new GoodsVo();
		gv.setId(1L);
		gv.setGoods_name("电脑1");

		GoodsVo gv2 = new GoodsVo();
		gv2.setId(2L);
		gv2.setGoods_name("电脑2");
		list.add(gv);
		list.add(gv2);

		String jsonString = JSON.toJSONString(list);

		System.out.println(jsonString);

		List<GoodsVo> parseObject = JSON.parseObject(jsonString, List.class);

		System.out.println(parseObject);

	}

}
