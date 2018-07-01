package com.miaosha.service;

import java.awt.image.BufferedImage;

import com.miaosha.entity.OrderInfoEntity;
import com.miaosha.vo.GoodsVo;

public interface MiaoshaService {

	OrderInfoEntity miaosha(Long userId, GoodsVo goodsVo);

	Long getMiaoshaResult(Long userId, Long goodsId);

	boolean checkPath(String path, Long userId, Long goodsId);

	BufferedImage createMiaoshaVerfyCode(Long userId, Long goodsId);

    boolean checkVerifyCode(Long userId, Long goodsId, Integer verifyCode);
}
