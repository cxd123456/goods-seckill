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
	 * 
	 * 只用mysql，这种方案
	 * 
	 * 
	 */
	@Transactional
	@Override
	public ResultCode<String> createGoodsOrder(Long user_id, Long seckill_goods_id) {
		ResultCode<String> result = new ResultCode<>();
		
		// 1. 是否到秒杀时间
		// 2. 查库存
		SeckillGoodsEntity goods = seckillGoodsEntityMapper.selectEnableGoods(seckill_goods_id);
		
		if (goods == null) 
			return result.ERROE("未到秒杀时间或者库存不足");
		
		// 3. 判断是否购买过
		SeckillOrderEntity seckillOrderEntity = seckillOrderEntityMapper.selectSeckillOrder(user_id, seckill_goods_id);
		if (seckillOrderEntity != null) 
			return result.ERROE("该用户已经购买过");
		
		// 4. 生成订单，减库存
		// 减库存
		/*
		 *  这里使用mysql排他锁，控制抢购按顺序进行
		 *  排他锁，参考：https://www.cnblogs.com/boblogsbo/p/5602122.html
		 *  mysql在innoDB引擎下，默认update，insert，delete语句都会添加排他锁
		 *  在操作同一行数据时，在排他锁未释放的情况下，该行数据无法再被添加任何锁(排他锁和共享锁)
		 *  所以，这里修改库存，是在排他锁下，按顺序减库存，会出现阻塞的情况
		 */
		if (seckillGoodsEntityMapper.updateSeckillGoodsStockCount(seckill_goods_id) <= 0) 
			return result.ERROE("抢购失败");
		// 生成订单
		OrderInfoEntity orderInfoEntity = new OrderInfoEntity();
		orderInfoEntity.setUser_id(user_id);
		orderInfoEntity.setGoods_id(goods.getGoods_id());
		orderInfoEntity.setGoods_name(goods.getGoods_name());
		orderInfoEntity.setGoods_count(1);
		orderInfoEntity.setGoods_price(goods.getSeckill_price());
		orderInfoEntityMapper.insertSelective(orderInfoEntity);
		
		SeckillOrderEntity soe = new SeckillOrderEntity();
		soe.setUser_id(user_id);
		soe.setGoods_id(seckill_goods_id);
		soe.setOrder_id(orderInfoEntity.getId());
		seckillOrderEntityMapper.insertSelective(soe);
		
		return result.OK(null);
	}

}
