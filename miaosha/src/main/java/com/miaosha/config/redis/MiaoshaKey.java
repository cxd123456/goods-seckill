package com.miaosha.config.redis;

public class MiaoshaKey extends BaseRedisKeyPrefix{

	public static final MiaoshaKey isGoodsOver = new MiaoshaKey(0, "go");
	
	public MiaoshaKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}

}
