package com.miaosha.config.redis;

public class OrderKey extends BaseRedisKeyPrefix {

	public static final OrderKey order = new OrderKey(0, "order");
	public static final OrderKey getMiaoshaOrderByUidGid = new OrderKey(0, "moug");
	
	public OrderKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}
	
}
