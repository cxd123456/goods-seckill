package com.miaosha.service;

import com.miaosha.entity.MiaoshaOrderEntity;
import com.miaosha.entity.MiaoshaUserEntity;
import com.miaosha.entity.OrderInfoEntity;
import com.miaosha.vo.GoodsVo;

public interface OrderService {

	MiaoshaOrderEntity getMiaoshaOrderByUserIdGoodsId(Long userId, Long goodsId);

	OrderInfoEntity createOrder(MiaoshaUserEntity user, GoodsVo goodsVo);

}
