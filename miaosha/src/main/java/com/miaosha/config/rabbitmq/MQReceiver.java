package com.miaosha.config.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.miaosha.common.CodeMsg;
import com.miaosha.common.Result;
import com.miaosha.config.redis.RedisService;
import com.miaosha.entity.MiaoshaOrderEntity;
import com.miaosha.service.GoodsService;
import com.miaosha.service.MiaoshaService;
import com.miaosha.service.OrderService;
import com.miaosha.vo.GoodsVo;

/**
 * 消息接收者、消费者
 * 
 * @Time 2018年6月25日
 */
@Component
public class MQReceiver {

	private static final Logger LOG = LoggerFactory.getLogger(MQReceiver.class);

	@Autowired
	private GoodsService goodsService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private MiaoshaService miaoshaService;

	// @RabbitListener(queues = MQConfig.QUEUE)
	// public void receive(String message) {
	// LOG.info("========receive message: " + message + "=========");
	// }

	@RabbitListener(queues = MQConfig.MIAOSHA_QUEUE)
	public void receive(String message) {
		LOG.info("========receive message: " + message + "=========");

		MiaoshaMessage mm = RedisService.stringToBean(message, MiaoshaMessage.class);

		Long goodsId = mm.getGoodsId();
		Long userId = mm.getUserId();

		// 判断库存
		GoodsVo goodsVo = goodsService.getGoodsById(goodsId);
		if (goodsVo.getStock_count() <= 0) {
			return;
		}

		// 判断是否是重复秒杀
		MiaoshaOrderEntity order = orderService.getMiaoshaOrderByUserIdGoodsId(userId, goodsId);
		if (order != null) {
			return;
		}
		
		// 生成秒杀订单
		miaoshaService.miaosha(userId, goodsVo);

	}

}
