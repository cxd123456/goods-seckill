package com.seckill.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSONObject;
import com.seckill.entity.GoodsEntity;
import com.seckill.mapper.GoodsEntityMapper;
import com.seckill.service.SeckillGoodsService;
import com.seckill.util.RedisUtil;
import com.seckill.util.ResultCode;
import com.sun.tools.classfile.StackMapTable_attribute.same_frame;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ApplicationTest {
	
//	@Autowired
//	private GoodsEntityMapper goodsEntityMapper;
	@Autowired
	private SeckillGoodsService seckillGoodsService;
	
	@Test
	public void test() {
//		GoodsEntity goodsEntity = goodsEntityMapper.selectByPrimaryKey(1L);
//		RedisUtil.setValue("goods_1", JSONObject.toJSONString(goodsEntity));
//		System.err.println(goodsEntity);
//		System.out.println("=================");
//		System.err.println(RedisUtil.getValue("goods_1"));
//		System.out.println(JSONObject.toJSONString(
//				seckillGoodsService.createGoodsOrder(13333333333L, 1L)));
		
		/**
		 * 多线程，模拟100个用户，抢购20个商品
		 */
		
		for (int i = 1; i <= 100; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					long threadId = Thread.currentThread().getId();
					if (!testCreateOrder(threadId)) {
						System.out.println("==========threadId用户抢购失败==========");
					} else {
						System.err.println("==========threadId用户抢购成功==========");
					}
				}
			}).start();
		}
	}
	
	public Boolean testCreateOrder(Long user_id) {
		ResultCode<String> resultCode = seckillGoodsService.createGoodsOrder(user_id, 1L);
		if ("ok".equals(resultCode.getCode())) {
			return true;
		} else {
			return false;
		}
	}

}
