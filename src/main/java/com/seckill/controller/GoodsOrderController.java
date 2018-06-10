package com.seckill.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seckill.service.SeckillGoodsService;
import com.seckill.util.Constants;
import com.seckill.util.RedisUtil;
import com.seckill.util.ResultCode;

@RestController
public class GoodsOrderController {

	@Autowired
	private SeckillGoodsService seckillGoodsService;
	
	@RequestMapping("setRedisStockCount")
	public ResultCode<String> setRedisStockCount() {
		
		Long seckill_goods_id = 1L;
		
		Integer goods_count = 20;
		
		RedisUtil.setValue(Constants.STOCK_COUNT + seckill_goods_id, goods_count.toString());
		
		return new ResultCode<String>().OK(goods_count.toString());
	}
	
	@RequestMapping("test")
	public ResultCode<String> test() {
		String value = RedisUtil.getValue(Constants.STOCK_COUNT + 1);
		return new ResultCode<String>().OK(value);
	}
	
	/**
	 * @param user_id
	 * @return
	 */
	@RequestMapping("seckill")
	public ResultCode<String> seckill1(Long user_id) {
		// 初级方案秒杀入口
//		return seckillGoodsService.createGoodsOrder1(user_id, 1L);
		return seckillGoodsService.createGoodsOrder2(user_id, 1L);
	}
	
}
