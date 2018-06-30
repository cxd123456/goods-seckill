package com.miaosha.config.redis;

public class MiaoshaKey extends BaseRedisKeyPrefix{

	public static final MiaoshaKey isGoodsOver = new MiaoshaKey(0, "go");
	public static final MiaoshaKey getMiaoshaPath = new MiaoshaKey(60, "go");
	public static final MiaoshaKey getMiaoshaVerifyCode = new MiaoshaKey(500, "vc");

	public MiaoshaKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}

}
