package com.seckill.mq.producer.impl;

import javax.annotation.Resource;
import javax.jms.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

import com.seckill.mq.producer.GoodsSeckillProducer;

@Service
public class GoodsSeckillProducerImpl implements GoodsSeckillProducer {

	@Autowired
	private JmsMessagingTemplate jmsMessagingTemplate;
	@Resource(name = "goodsSeckillQueue")
	private Queue goodsSeckillQueue;
	
	@Override
	public void sendSeckillSuccessMsg(String message) {
		this.jmsMessagingTemplate.convertAndSend(this.goodsSeckillQueue,message);
	}

}
