package com.miaosha.config.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

/**
 * 消息发送者、生产者
 * 
 * @Time 2018年6月25日
 */
@Component
public class MQSender {
	
	private static final Logger LOG = LoggerFactory.getLogger(MQReceiver.class);

	@Autowired
	private AmqpTemplate amqpTemplate;
	
//	public void send(Object message) {
//		if (message == null) 
//			return;
//		LOG.info("----send message: " + message + "----");
//		amqpTemplate.convertAndSend(MQConfig.QUEUE, JSON.toJSONString(message));	// arg1 队列名称
//	}

	public void sendMiaoshaMessage(MiaoshaMessage message) {
		LOG.info("----send message: " + message + "----");
		if (message == null) return;
		amqpTemplate.convertAndSend(MQConfig.MIAOSHA_QUEUE, JSON.toJSONString(message));
	}
	
	
}
