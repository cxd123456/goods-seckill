package com.seckill.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

	@Bean
	public RedisTemplate<?, ?> redisTemplate(@Autowired RedisTemplate<?, ?> redisTemplate) {
		redisTemplate.setDefaultSerializer(new StringRedisSerializer());
		return redisTemplate;
	}
	
}
