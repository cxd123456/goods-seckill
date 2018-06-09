package com.seckill.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.seckill.entity.OrderInfoEntity;
import com.seckill.entity.SeckillGoodsEntity;
import com.seckill.entity.SeckillOrderEntity;
import com.seckill.mapper.OrderInfoEntityMapper;
import com.seckill.mapper.SeckillGoodsEntityMapper;
import com.seckill.mapper.SeckillOrderEntityMapper;
import com.seckill.service.SeckillGoodsService;
import com.seckill.util.ResultCode;

@Service
public class SeckillGoodsServiceImpl implements SeckillGoodsService {

	
	@Autowired
	private SeckillGoodsEntityMapper seckillGoodsEntityMapper;
	@Autowired
	private SeckillOrderEntityMapper seckillOrderEntityMapper;
	@Autowired
	private OrderInfoEntityMapper orderInfoEntityMapper;
	
	/**
	 * 初级goods秒杀思路：请求直接打到mysql
	 */
	@Transactional
	@Override
	public ResultCode<String> createGoodsOrder(Long user_id, Long seckill_goods_id) {
		ResultCode<String> result = new ResultCode<>();
		
		// 1. 是否到秒杀时间
		// 2. 查库存
		SeckillGoodsEntity goods = seckillGoodsEntityMapper.selectEnableGoods(seckill_goods_id);
		
		if (goods == null) {
			return result.ERROE("未到秒杀时间或者库存不足");
		}
		
		// 3. 判断是否购买过
		SeckillOrderEntity seckillOrderEntity = seckillOrderEntityMapper.selectSeckillOrder(user_id, seckill_goods_id);
		if (seckillOrderEntity != null) {
			return result.ERROE("该用户已经购买过");
		}
		
		// 4. 生成订单，减库存
		OrderInfoEntity orderInfoEntity = new OrderInfoEntity();
		orderInfoEntity.setUser_id(user_id);
		orderInfoEntity.setGoods_id(goods.getGoods_id());
		orderInfoEntity.setGoods_name(goods.getGoods_name());
		orderInfoEntity.setGoods_count(1);
		orderInfoEntity.setGoods_price(goods.getSeckill_price());
		Long orderId = orderInfoEntityMapper.insertSelective(orderInfoEntity);
		
		SeckillOrderEntity soe = new SeckillOrderEntity();
		soe.setUser_id(user_id);
		soe.setGoods_id(seckill_goods_id);
		soe.setOrder_id(orderId);
		seckillOrderEntityMapper.insertSelective(soe);
		
		// 减库存
		seckillGoodsEntityMapper.updateSeckillGoodsStockCount(seckill_goods_id);
		
		return result.OK(null);
	}

}
