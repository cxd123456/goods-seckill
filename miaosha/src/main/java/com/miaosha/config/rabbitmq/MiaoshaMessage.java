package com.miaosha.config.rabbitmq;

import java.io.Serializable;

public class MiaoshaMessage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8211771619195758947L;
	
	private Long goodsId;
	private Long userId;
	
	public MiaoshaMessage() {
	}
	
	public MiaoshaMessage(Long goodsId, Long userId) {
		super();
		this.goodsId = goodsId;
		this.userId = userId;
	}
	public Long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
