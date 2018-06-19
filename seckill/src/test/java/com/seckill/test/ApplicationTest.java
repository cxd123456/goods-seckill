package com.seckill.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSONObject;
import com.seckill.Application;
import com.seckill.mq.message.GoodsSeckillMessage;
import com.seckill.mq.producer.GoodsSeckillProducer;

@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
public class ApplicationTest {
	
	@Autowired
	private GoodsSeckillProducer goodsSeckillProducer;
	
	@Test
	public void test() {
		
		for (int i = 0; i < 200; i++) {
			GoodsSeckillMessage message = new GoodsSeckillMessage();
			message.setUser_id(Long.valueOf(i));
			message.setSeckill_goods_id(i * 10L);
			goodsSeckillProducer.sendSeckillSuccessMsg(JSONObject.toJSONString(message));
		}
		
	}
	

}
