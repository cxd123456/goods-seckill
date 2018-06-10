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
	
	/**
	 * 秒杀入口
	 * @param user_id
	 * @return
	 */
	@RequestMapping("seckill")
	public ResultCode<String> seckill(Long user_id) {
		return seckillGoodsService.createGoodsOrder(user_id, 1L);
	}
	
}
