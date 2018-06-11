package com.seckill.mq.producer;

public interface GoodsSeckillProducer {

	/**
	 * 发送商品秒杀成功消息到指定消息队列.<br/>
	 * message: {user_id, seckill_goods_id}
	 * @param message
	 */
	public void sendSeckillSuccessMsg(String message);
	
}
