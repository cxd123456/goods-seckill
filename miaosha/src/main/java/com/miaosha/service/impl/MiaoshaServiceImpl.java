package com.miaosha.service.impl;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.miaosha.config.redis.MiaoshaKey;
import com.miaosha.config.redis.RedisService;
import com.miaosha.entity.MiaoshaOrderEntity;
import com.miaosha.entity.OrderInfoEntity;
import com.miaosha.service.GoodsService;
import com.miaosha.service.MiaoshaService;
import com.miaosha.service.OrderService;
import com.miaosha.vo.GoodsVo;

/**
 * 秒杀service
 * 
 * @创建时间：2018年6月20日
 */
@Service
public class MiaoshaServiceImpl implements MiaoshaService {
	
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private RedisService redisService;

	@Transactional
	@Override
	public OrderInfoEntity miaosha(Long userId, GoodsVo goodsVo) {
		// 1.减库存 
		Boolean result = goodsService.reduceStock(goodsVo);
		if (result) {
			// 2.生成订单 order_info, miaosha_order
			return orderService.createOrder(userId, goodsVo);
		} else {
			setGoodsOver(goodsVo.getId());
			return null;
		}
	}

	@Override
	public Long getMiaoshaResult(Long userId, Long goodsId) {
		MiaoshaOrderEntity order = orderService.getMiaoshaOrderByUserIdGoodsId(userId, goodsId);
		if (order != null) {
			return order.getOrder_id();
		} else {
			Boolean isOver = getGoodsOver(goodsId);
			if (isOver) {
				return -1L;
			} else {
				return 0L;
			}
		}
	}

	@Override
	public boolean checkPath(String path, Long userId, Long goodsId) {
		String pathOld = redisService.get(MiaoshaKey.getMiaoshaPath, userId + "_" + goodsId, String.class);
		return path.equals(pathOld);
	}


	private void setGoodsOver(Long goodsId) {
		redisService.set(MiaoshaKey.isGoodsOver, goodsId.toString(), Boolean.TRUE);
		
	}
	
	private Boolean getGoodsOver(Long goodsId) {
		return redisService.exists(MiaoshaKey.isGoodsOver, goodsId.toString());
	}

	@Override
	public BufferedImage createMiaoshaVerfyCode(Long userId, Long goodsId) {
		
		int width = 80;
		int height = 32;
		//create the image
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		// set the background color
		g.setColor(new Color(0xDCDCDC));
		g.fillRect(0, 0, width, height);
		// draw the border
		g.setColor(Color.black);
		g.drawRect(0, 0, width - 1, height - 1);
		// create a random instance to generate the codes
		Random rdm = new Random();
		// make some confusion
		for (int i = 0; i < 50; i++) {
			int x = rdm.nextInt(width);
			int y = rdm.nextInt(height);
			g.drawOval(x, y, 0, 0);
		}
		// generate a random code
		String verifyCode = generateVerifyCode(rdm);
		g.setColor(new Color(0, 100, 0));
		g.setFont(new Font("Candara", Font.BOLD, 24));
		g.drawString(verifyCode, 8, 24);
		g.dispose();
		//把验证码存到redis中
		int rnd = calc(verifyCode);
		redisService.set(MiaoshaKey.getMiaoshaVerifyCode, userId+","+goodsId, rnd);
		//输出图片	
		return image;
	}

	@Override
	public boolean checkVerifyCode(Long userId, Long goodsId, Integer verifyCode) {
		Integer redisVerifyCode = redisService.get(MiaoshaKey.getMiaoshaVerifyCode, userId + "," + goodsId, Integer.class);
		if (redisVerifyCode == null || verifyCode == null || !redisVerifyCode.equals(verifyCode)) {
            return false;
        }
        redisService.del(MiaoshaKey.getMiaoshaVerifyCode, userId + "," + goodsId);
		return true;
	}

	private static int calc(String verifyCode) {
		try {
			ScriptEngineManager manager = new ScriptEngineManager();
			ScriptEngine engine = manager.getEngineByName("JavaScript");
			return (int) engine.eval(verifyCode);
		} catch (ScriptException e) {
			e.printStackTrace();
			return 0;
		}
	}

	private static char[] ops = new char[] {'+', '-', '*'};
	
	/**
	 *  + - *
	 */
	private static String generateVerifyCode(Random rdm) {
		
		int num1 = rdm.nextInt(10);
		int num2 = rdm.nextInt(10);
		int num3 = rdm.nextInt(10);
		
		char op1 = ops[rdm.nextInt(3)];
		char op2 = ops[rdm.nextInt(3)];
		
		String exp = "" + num1 + op1 + num2 + op2 + num3;
		
		return exp;
	}

    public static void main(String[] args) {
	    Integer i = new Integer(1);
        System.out.println(1 == i);
    }


}
