package com.seckill.mq.message;

import java.io.Serializable;
/**
 * 商品秒杀消息携带
 * @创建时间：2018年6月11日
 */
public class GoodsSeckillMessage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2670114322714117341L;
	
	private Long user_id;
	private Long seckill_goods_id;
	
	public Long getUser_id() {
		return user_id;
	}
	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}
	public Long getSeckill_goods_id() {
		return seckill_goods_id;
	}
	public void setSeckill_goods_id(Long seckill_goods_id) {
		this.seckill_goods_id = seckill_goods_id;
	}
	
}
