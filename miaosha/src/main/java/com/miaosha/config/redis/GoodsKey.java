package com.miaosha.config.redis;

public class GoodsKey extends BaseRedisKeyPrefix{
	
	public static final GoodsKey getGoodsList = new GoodsKey(0, "list");

	public GoodsKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}

}
