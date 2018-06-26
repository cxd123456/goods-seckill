package com.miaosha.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.miaosha.config.redis.MiaoshaKey;
import com.miaosha.config.redis.RedisService;
import com.miaosha.entity.MiaoshaOrderEntity;
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
	@Autowired
	private RedisService redisService;

	@Transactional
	@Override
	public OrderInfoEntity miaosha(Long userId, GoodsVo goodsVo) {
		// 1.减库存 
		Boolean result = goodsService.reduceStock(goodsVo);
		if (result) {
			// 2.生成订单 order_info, miaosha_order
			return orderService.createOrder(userId, goodsVo);
		} else {
			setGoodsOver(goodsVo.getId());
			return null;
		}
	}

	@Override
	public Long getMiaoshaResult(Long userId, Long goodsId) {
		MiaoshaOrderEntity order = orderService.getMiaoshaOrderByUserIdGoodsId(userId, goodsId);
		if (order != null) {
			return order.getOrder_id();
		} else {
			Boolean isOver = getGoodsOver(goodsId);
			if (isOver) {
				return -1L;
			} else {
				return 0L;
			}
		}
	}

	
	private void setGoodsOver(Long goodsId) {
		redisService.set(MiaoshaKey.isGoodsOver, goodsId.toString(), Boolean.TRUE);
		
	}
	
	private Boolean getGoodsOver(Long goodsId) {
		return redisService.exists(MiaoshaKey.isGoodsOver, goodsId.toString());
	}

}
