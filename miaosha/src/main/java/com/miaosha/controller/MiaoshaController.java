package com.miaosha.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.miaosha.common.CodeMsg;
import com.miaosha.entity.MiaoshaOrderEntity;
import com.miaosha.entity.MiaoshaUserEntity;
import com.miaosha.entity.OrderInfoEntity;
import com.miaosha.service.GoodsService;
import com.miaosha.service.MiaoshaService;
import com.miaosha.service.OrderService;
import com.miaosha.vo.GoodsVo;

/**
 * 秒杀controller
 * 
 * @创建时间：2018年6月20日
 */
@Controller
@RequestMapping("miaosha")
public class MiaoshaController {
	
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private MiaoshaService miaoshaService;
	
	@RequestMapping("do_miaosha")
	public String doMiaosha(Model model, MiaoshaUserEntity user, 
			@RequestParam("goodsId") Long goodsId) {
		
		if (user == null) {	// 未登录，跳登录页
			return "/login";
		}
		
		GoodsVo goodsVo = goodsService.getGoodsById(goodsId);
		
		// 判断库存
		if (goodsVo.getStock_count() <= 0) {	// 库存不足
			model.addAttribute("errmsg", CodeMsg.MIAO_SHA_OVER.getMsg());
			return "miaosha_fail";
		}
		
		// 判断是否已经秒杀过了，如果秒杀过，就无法再次秒杀
		MiaoshaOrderEntity order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
		if (order != null) {
			model.addAttribute("errmsg", CodeMsg.REPEATE_MIAOSHA.getMsg());
			return "miaosha_fail";
		}
		
		// 可以秒杀 1.减库存 2.生成订单 3.写入秒杀订单
		OrderInfoEntity orderInfoEntity = miaoshaService.miaosha(user, goodsVo);
		
		model.addAttribute("orderInfo", orderInfoEntity);
		model.addAttribute("goods", goodsVo);
		
		return "order_detail";
	}

}
