package com.seckill.service;

import com.seckill.util.ResultCode;

public interface SeckillGoodsService {

	/**
	 * 初级方案，应对并发较为低的抢购<br/>
	 * 
	 * 下单，减库存
	 * @param user_id	用户id
	 * @param seckill_goods_id	秒杀商品id
	 * @return
	 */
	ResultCode<String> createGoodsOrder1(Long user_id, Long seckill_goods_id);
	
	/**
	 * 优化方案一，使用redis，挡住大部分的用户请求
	 * @param user_id
	 * @param seckill_goods_id
	 * @return
	 */
	ResultCode<String> createGoodsOrder2(Long user_id, Long seckill_goods_id);
	
	/**
	 * 优化方案二，使用redis和ActiveMQ队列
	 * @param user_id
	 * @param seckill_goods_id
	 * @return
	 */
	ResultCode<String> createGoodsOrder3(Long user_id, Long seckill_goods_id);
	/**
	 * ActiveMQ队列消费者Consumer调用服务，为了方案二
	 * @param user_id
	 * @param seckill_goods_id
	 * @return
	 */
	ResultCode<String> createGoodsOrder3ForConsumer(String message);
	
}
