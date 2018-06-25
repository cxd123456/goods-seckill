package com.miaosha.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.miaosha.config.redis.OrderKey;
import com.miaosha.config.redis.RedisService;
import com.miaosha.entity.MiaoshaOrderEntity;
import com.miaosha.entity.MiaoshaUserEntity;
import com.miaosha.entity.OrderInfoEntity;
import com.miaosha.mapper.MiaoshaOrderEntityMapper;
import com.miaosha.mapper.OrderInfoEntityMapper;
import com.miaosha.service.OrderService;
import com.miaosha.vo.GoodsVo;

/**
 * 订单service
 * 
 * @创建时间：2018年6月20日
 */
@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private OrderInfoEntityMapper orderInfoEntityMapper;
	@Autowired
	private MiaoshaOrderEntityMapper miaoshaOrderEntityMapper;
	@Autowired
	private RedisService redisService;
	
	
	
	@Override
	public MiaoshaOrderEntity getMiaoshaOrderByUserIdGoodsId(Long userId, Long goodsId) {
//		return orderInfoEntityMapper.getMiaoshaOrderByUserIdGoodsId(userId, goodsId);
		return redisService.get(OrderKey.getMiaoshaOrderByUidGid, userId + "_" +goodsId, MiaoshaOrderEntity.class);
	}


	@Transactional
	@Override
	public OrderInfoEntity createOrder(Long userId, GoodsVo goodsVo) {
		
		// 生成订单
		OrderInfoEntity orderInfo = new OrderInfoEntity();
		orderInfo.setUser_id(userId);
		orderInfo.setDelivery_addr_id(0L);
		orderInfo.setGoods_id(goodsVo.getId());
		orderInfo.setGoods_name(goodsVo.getGoods_name());
		orderInfo.setGoods_count(1);
		orderInfo.setGoods_price(goodsVo.getMiaosha_price());
		orderInfo.setOrder_channel(new Byte("1"));	// 1pc, 2android, 3ios
		orderInfo.setStatus(new Byte("0"));	// 0新建未支付，1已支付，2已发货，3已收货，4已退款，5已完成
		orderInfo.setCrate_time(new Date());
		
		orderInfoEntityMapper.insertSelective(orderInfo);
		
		// 生成秒杀订单
		MiaoshaOrderEntity miaoshaOrder = new MiaoshaOrderEntity();
		miaoshaOrder.setUser_id(userId);
		miaoshaOrder.setGoods_id(goodsVo.getId());
		miaoshaOrder.setOrder_id(orderInfo.getId());
		
		miaoshaOrderEntityMapper.insertSelective(miaoshaOrder);
		
		redisService.set(OrderKey.getMiaoshaOrderByUidGid, userId + "_" +goodsVo.getId(), miaoshaOrder);
		
		return orderInfo;
	}
	
	

}
