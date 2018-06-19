package com.miaosha.config.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Service
public class RedisService {
	
	@Autowired
	RedisConfig redisConfig;
	/**
	 * 配置JedisPool
	 * @param redisConfig redis配置参数
	 * @return
	 */
	@Bean
	public JedisPool jedisPool() {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxIdle(redisConfig.getPoolMaxIdle());
		jedisPoolConfig.setMaxTotal(redisConfig.getPoolMaxTotal());
		jedisPoolConfig.setMaxWaitMillis(redisConfig.getPoolMaxWait() * 1000);
		
		return new JedisPool(jedisPoolConfig, redisConfig.getHost(), redisConfig.getPort(),
				redisConfig.getTimeout() * 1000);
	}

	/**
	 * redis 获取值
	 * @param prefix 前缀
	 * @param key	key值
	 * @param clazz	获取的对象类型
	 * @return
	 */
	public <T> T get(RedisKeyPrefix prefix, String key, Class<T> clazz) {
		Jedis jedis = null;
		try {
			jedis = jedisPool().getResource();
			
			String value = jedis.get(prefix.getPrefix() + key);
			
			return stringToBean(value, clazz);
		} finally {
			returnToPool(jedis);
		}
	}

	/**
	 * redis 设置值
	 * @param prefix 前缀
	 * @param key	key值
	 * @param obj	value值
	 * @return
	 */
	public <T> boolean set(RedisKeyPrefix prefix, String key, T obj) {
		Jedis jedis = null;
		try {
			jedis = jedisPool().getResource();
			String value = beanToString(obj);
			if (value == null || "".equals(value)) {
				return false;
			}
			
			if (prefix.expireSeconds() <= 0) {
				jedis.setex(prefix.getPrefix() + key, prefix.expireSeconds(), value);
			} else {
				jedis.set(prefix.getPrefix() + key, value);
			}
			return true;
		} finally {
			returnToPool(jedis);
		}
	}
	
	/**
	 * 递增
	 * @param prefix
	 * @param key
	 * @return
	 */
	public Long incr(RedisKeyPrefix prefix, String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool().getResource();
			return jedis.incr(prefix.getPrefix() + key);
		} finally {
			returnToPool(jedis);
		}
	}
	
	/**
	 * 递减
	 * @param prefix
	 * @param key
	 * @return
	 */
	public Long decr(RedisKeyPrefix prefix, String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool().getResource();
			return jedis.decr(prefix.getPrefix() + key);
		} finally {
			returnToPool(jedis);
		}
	}
	
	/**
	 * 判断是否存在
	 * @param prefix
	 * @param key
	 * @return
	 */
	public Boolean exists(RedisKeyPrefix prefix, String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool().getResource();
			return jedis.exists(prefix.getPrefix() + key);
		} finally {
			returnToPool(jedis);
		}
	}
	
	private <T> String beanToString(T obj) {
		if (obj == null) {
			return null;
		}
		return JSON.toJSONString(obj);
	}

	@SuppressWarnings("unchecked")
	private <T> T stringToBean(String value, Class<T> clazz) {
		if (value == null || "".equals(value)) {
			return null;
		}
		
		if (clazz == int.class || clazz == Integer.class) {
			return (T)Integer.valueOf(value);
		} else if (clazz == long.class || clazz == Long.class) {
			return (T)Long.valueOf(value);
		} else if (clazz == String.class) {
			return (T) value;
		} else {
			return JSONObject.parseObject(value, clazz);
		}
		
	}

	private void returnToPool(Jedis jedis) {
		if (jedis != null) {
			jedis.close();
		}
	}
	
	public static void main(String[] args) {
//		System.out.println(JSON.toJSONString(1).getClass());
//		System.out.println(JSON.parseArray("1", String.class));
	}
	
}
