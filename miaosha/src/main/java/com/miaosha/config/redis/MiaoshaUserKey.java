package com.miaosha.config.redis;

public class MiaoshaUserKey extends BaseRedisKeyPrefix{

	// token有效期2天
	public static final int TOKEN_EXPIRE = 2 * 24 * 60 * 60;
	
	public static final MiaoshaUserKey token = new MiaoshaUserKey(TOKEN_EXPIRE, "tx");
	
	public MiaoshaUserKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}

}
