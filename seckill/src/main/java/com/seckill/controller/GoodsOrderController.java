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
	
	@RequestMapping("test")
	public ResultCode<String> test() {
//		String value = RedisUtil.getValue(Constants.STOCK_COUNT + 1);
		
		return new ResultCode<String>().OK(null);
	}
	
	/**
	 * @param user_id
	 * @return
	 */
	@RequestMapping("seckill")
	public ResultCode<String> seckill1() {
		// 初级方案秒杀入口
//		ResultCode<String> result = seckillGoodsService.createGoodsOrder1(1L, 1L);
		// 秒杀方案优化一
//		ResultCode<String> result = seckillGoodsService.createGoodsOrder2(1L, 1L);
		// 秒杀方案优化二
		ResultCode<String> result = seckillGoodsService.createGoodsOrder3(1L, 1L);
		if ("ok".equals(result.getCode())) {
			System.err.println("============秒杀成功===============");
		} else {
			System.out.println("============秒杀失败===============");
		}
		return result;
	}
	
}
