package com.seckill.service;

import com.seckill.util.ResultCode;

public interface SeckillGoodsService {

	/**
	 * 下单，减库存
	 * @param user_id	用户id
	 * @param seckill_goods_id	秒杀商品id
	 * @return
	 */
	ResultCode<String> createGoodsOrder(Long user_id, Long seckill_goods_id);
	
}
