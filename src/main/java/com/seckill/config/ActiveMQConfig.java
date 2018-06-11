package com.seckill.config;

import javax.jms.Queue;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;

@Configuration
@EnableJms
public class ActiveMQConfig {

	/**
	 * 商品秒杀队列queue
	 * @return
	 */
	@Bean("goodsSeckillQueue")
	public Queue queue() {
		return new ActiveMQQueue("goods.seckill.queue") ;
	}
	
}
