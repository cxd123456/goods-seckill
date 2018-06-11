package com.seckill.mq.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import com.seckill.service.SeckillGoodsService;

@Service
public class GoodsSeckillConsumer {
	
	@Autowired
	private SeckillGoodsService seckillGoodsService;

	@JmsListener(destination = "goods.seckill.queue")
	public void receiveGoodsSeckillMessage(String msg) {
//		GoodsSeckillMessage message = JSONObject.parseObject(msg, GoodsSeckillMessage.class);
		seckillGoodsService.createGoodsOrder3ForConsumer(msg);
		System.out.println("===消费消息===" + msg);
	}
	
}
