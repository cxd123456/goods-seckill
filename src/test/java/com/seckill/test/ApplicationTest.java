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
		
		for (int i = 1; i < 20; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					
				}
			});
		}
	}
	
	public void testCreateOrder(Long user_id) {
		seckillGoodsService.createGoodsOrder(13333333333L, 1L);
	}

}
