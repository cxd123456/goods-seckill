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
import com.seckill.util.Constants;
import com.seckill.util.RedisUtil;
import com.seckill.util.ResultCode;
import com.sun.tools.classfile.StackMapTable_attribute.same_frame;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ApplicationTest {
	
	
	@Test
	public void test() {
		String value = RedisUtil.getValue(Constants.STOCK_COUNT + 1);
		System.out.println(value);
	}
	

}
