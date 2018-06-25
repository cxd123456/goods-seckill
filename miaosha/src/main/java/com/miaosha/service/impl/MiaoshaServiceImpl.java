package com.miaosha.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.miaosha.entity.MiaoshaUserEntity;
import com.miaosha.entity.OrderInfoEntity;
import com.miaosha.service.GoodsService;
import com.miaosha.service.MiaoshaService;
import com.miaosha.service.OrderService;
import com.miaosha.vo.GoodsVo;

/**
 * 秒杀service
 * 
 * @创建时间：2018年6月20日
 */
@Service
public class MiaoshaServiceImpl implements MiaoshaService {
	
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private OrderService orderService;

	@Transactional
	@Override
	public OrderInfoEntity miaosha(Long userId, GoodsVo goodsVo) {
		
		// 1.减库存 
		goodsService.reduceStock(goodsVo);
		
		// 2.生成订单 order_info, miaosha_order
		return orderService.createOrder(userId, goodsVo);
	}

}
