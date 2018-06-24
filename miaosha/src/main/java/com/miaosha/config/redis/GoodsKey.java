package com.miaosha.config.redis;

public class GoodsKey extends BaseRedisKeyPrefix{
	
	public static final GoodsKey getGoodsList = new GoodsKey(60, "gl");

	public GoodsKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}

}
