package com.miaosha.service;

import com.miaosha.entity.MiaoshaUserEntity;
import com.miaosha.entity.OrderInfoEntity;
import com.miaosha.vo.GoodsVo;

public interface MiaoshaService {

	OrderInfoEntity miaosha(Long userId, GoodsVo goodsVo);

}
