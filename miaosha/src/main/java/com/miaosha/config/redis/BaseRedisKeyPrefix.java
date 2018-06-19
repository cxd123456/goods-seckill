package com.miaosha.config.redis;

public abstract class BaseRedisKeyPrefix implements RedisKeyPrefix{

	private int expireSeconds;
	
	private String prefix;
	
	public BaseRedisKeyPrefix(int expireSeconds, String prefix) {
		super();
		this.expireSeconds = expireSeconds;
		this.prefix = prefix;
	}

	@Override
	public int expireSeconds() {	// 默认0代表永不过期
		return expireSeconds;
	}

	@Override
	public String getPrefix() {
		return this.getClass().getSimpleName() + ":" + prefix + ":";
	}

}
