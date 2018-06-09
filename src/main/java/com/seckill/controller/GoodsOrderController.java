package com.seckill.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seckill.service.SeckillGoodsService;
import com.seckill.util.ResultCode;

@RestController
public class GoodsOrderController {

	@Autowired
	private SeckillGoodsService seckillGoodsService;
	
	@RequestMapping("testSeckill")
	public ResultCode<String> testSeckill() {
		ResultCode<String> result = new ResultCode<>();
		
		for (int i = 1; i <= 100; i++) {
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
		return result.OK(null);
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
