package com.miaosha.config.redis;

/**
 * redis key 规范接口定义
 * 
 * @创建时间：2018年6月18日
 */
public interface RedisKeyPrefix {

	public int expireSeconds();
	
	public String getPrefix();
	
}
