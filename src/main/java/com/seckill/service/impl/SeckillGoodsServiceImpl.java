package com.seckill.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.seckill.entity.OrderInfoEntity;
import com.seckill.entity.SeckillGoodsEntity;
import com.seckill.entity.SeckillOrderEntity;
import com.seckill.mapper.OrderInfoEntityMapper;
import com.seckill.mapper.SeckillGoodsEntityMapper;
import com.seckill.mapper.SeckillOrderEntityMapper;
import com.seckill.mq.message.GoodsSeckillMessage;
import com.seckill.mq.producer.GoodsSeckillProducer;
import com.seckill.service.SeckillGoodsService;
import com.seckill.util.Constants;
import com.seckill.util.NumberUtil;
import com.seckill.util.RedisUtil;
import com.seckill.util.ResultCode;

@Service
public class SeckillGoodsServiceImpl implements SeckillGoodsService {

	@Autowired
	private SeckillGoodsEntityMapper seckillGoodsEntityMapper;
	@Autowired
	private SeckillOrderEntityMapper seckillOrderEntityMapper;
	@Autowired
	private OrderInfoEntityMapper orderInfoEntityMapper;
	@Autowired
	private GoodsSeckillProducer goodsSeckillProducer;

	/**
	 * 初级goods秒杀思路：请求直接打到mysql
	 * 
	 * 只用mysql，这种方案在模拟5000次秒杀请求需要耗时30秒。
	 * 
	 * 如果在真实环境中，算上网络阻塞，可能时间更长，其实这时候数据库已经吃不消了。
	 * 
	 * 需要短时间内应对大量的读请求，和写请求
	 * 
	 */
	@Transactional
	@Override
	public ResultCode<String> createGoodsOrder1(Long user_id, Long seckill_goods_id) {
		ResultCode<String> result = new ResultCode<>();

		user_id = Long.valueOf(System.currentTimeMillis() + NumberUtil.sexNumber());

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
		 * 这里使用mysql排他锁，控制抢购按顺序进行
		 * 排他锁，参考：https://www.cnblogs.com/boblogsbo/p/5602122.html
		 * mysql在innoDB引擎下，默认update，insert，delete语句都会添加排他锁
		 * 在操作同一行数据时，在排他锁未释放的情况下，该行数据无法再被添加任何锁(排他锁和共享锁) 所以，这里修改库存，是在排他锁下，按顺序减库存，会出现阻塞的情况
		 * 
		 * 排他锁另一种方式：SELECT * from goods where ID =1 for update; 使用查询排他锁的方式保证库存的准确性
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

	// ========================================
	// ============= 优化方案一，使用redis ========
	// ========================================

	/**
	 * 优化方案一，在请求过来后，使用redis挡住大部分的用户请求。
	 * 用户请求，首先会预减redis库存，在redis库存不足的情况下，请求都会被拒绝，而不会查询数据库，
	 * 
	 * 对数据库的操作，仅仅是在redis库存充足的情况下，进来的用户请求，也就是和库存同量的请求操作数据库
	 * 
	 * 
	 */
	@Transactional
	@Override
	public ResultCode<String> createGoodsOrder2(Long user_id, Long seckill_goods_id) {
		ResultCode<String> result = new ResultCode<>();

		user_id = Long.valueOf(System.currentTimeMillis() + NumberUtil.sexNumber());

		// 1. redis预减库存
		/*
		 * redis decr是悲观锁，是单线程的 redis读写请求每秒几万，很适合处理并发 利用这个特性，控制减库存，可以很快的处理高并发请求
		 */
		Long decr = RedisUtil.decr(Constants.STOCK_COUNT + seckill_goods_id);
		if (decr <= 0)
			return result.ERROE("库存不足");

		// 2. 判断是否购买过
		if (seckillOrderEntityMapper.selectSeckillOrder(user_id, seckill_goods_id) != null) {
			RedisUtil.incr(Constants.STOCK_COUNT + seckill_goods_id);
			return result.ERROE("该用户已经购买过");
		}

		// 4. 减库存mysql
		if (seckillGoodsEntityMapper.updateSeckillGoodsStockCount(seckill_goods_id) <= 0)
			return result.ERROE("抢购失败");

		// 3. 生成订单
		SeckillGoodsEntity goods = seckillGoodsEntityMapper.selectGoodsForCreateOrder(seckill_goods_id);
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

		System.err.println("=========购买成功========");

		return result.OK(null);
	}

	// ========================================
	// ===== 优化方案二，使用redis + activeMQ =====
	// ========================================

	@Override
	public ResultCode<String> createGoodsOrder3(Long user_id, Long seckill_goods_id) {
		ResultCode<String> result = new ResultCode<>();

		user_id = Long.valueOf(System.currentTimeMillis() + NumberUtil.sexNumber());

		// 1. redis预减库存
		Long decr = RedisUtil.decr(Constants.STOCK_COUNT + seckill_goods_id);
		if (decr <= 0)
			return result.ERROE("库存不足");

		GoodsSeckillMessage message = new GoodsSeckillMessage();
		message.setUser_id(user_id);
		message.setSeckill_goods_id(seckill_goods_id);

		// 2. 发送user_id和seckill_goods_id到消息队列
		goodsSeckillProducer.sendSeckillSuccessMsg(JSONObject.toJSONString(message));

		return result.OK(null);
	}

	@Transactional
	@Override
	public ResultCode<String> createGoodsOrder3ForConsumer(String message) {
		ResultCode<String> result = new ResultCode<>();
		GoodsSeckillMessage goodsSeckillMessage = JSONObject.parseObject(message, GoodsSeckillMessage.class);
		Long user_id = goodsSeckillMessage.getUser_id();
		Long seckill_goods_id = goodsSeckillMessage.getSeckill_goods_id();

		// 2. 判断是否购买过
		if (seckillOrderEntityMapper.selectSeckillOrder(user_id, seckill_goods_id) != null) {
			RedisUtil.incr(Constants.STOCK_COUNT + seckill_goods_id);
			return result.ERROE("该用户已经购买过");
		}

		// 4. 减库存mysql
		if (seckillGoodsEntityMapper.updateSeckillGoodsStockCount(seckill_goods_id) <= 0)
			return result.ERROE("抢购失败");

		// 3. 生成订单
		SeckillGoodsEntity goods = seckillGoodsEntityMapper.selectGoodsForCreateOrder(seckill_goods_id);
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
