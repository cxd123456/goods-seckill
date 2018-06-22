package com.miaosha.config.redis;

public class UserKey extends BaseRedisKeyPrefix{
	
	public static final UserKey getById = new UserKey(0, "id");
	public static final UserKey getByName = new UserKey(0, "name");

	private UserKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}
	
}
