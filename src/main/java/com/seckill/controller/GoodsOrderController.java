package com.seckill.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seckill.service.SeckillGoodsService;
import com.seckill.util.ResultCode;

@RestController
public class GoodsOrderController {

	@Autowired
	private SeckillGoodsService seckillGoodsService;
	
	@RequestMapping("seckill")
	public ResultCode<String> seckill(Long user_id) {
		return seckillGoodsService.createGoodsOrder(user_id, 1L);
	}
	
	
//	@RequestMapping("testSeckill")
	public ResultCode<String> testSeckill() throws Exception {
		ResultCode<String> result = new ResultCode<>();
		
		long startTime = System.currentTimeMillis();
		
		for (int i = 1; i <= 1000; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					long threadId = Thread.currentThread().getId();
					if (!testCreateOrder(threadId)) {
						System.out.println("==========" + threadId + "用户抢购失败==========");
					} else {
						System.err.println("==========" + threadId + "用户抢购成功==========");
					}
				}
			}).start();
		}
		
		System.out.println("****" + (System.currentTimeMillis() - startTime) + "*****");
		
		return result.OK((System.currentTimeMillis() - startTime) + "");
	}
	
	public Boolean testCreateOrder(Long user_id) {
		try {
			ResultCode<String> resultCode = seckillGoodsService.createGoodsOrder(user_id, 1L);
			if ("ok".equals(resultCode.getCode())) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}
	
}
