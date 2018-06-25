package com.miaosha.config.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 消息接收者、消费者
 * 
 * @Time 2018年6月25日
 */
@Component
public class MQReceiver {
	
	private static final Logger LOG = LoggerFactory.getLogger(MQReceiver.class);

	@RabbitListener(queues = MQConfig.QUEUE)
	public void receive(String message) {
		LOG.info("========receive message: " + message + "=========");
	}
	
	
}
