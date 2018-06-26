package com.miaosha.service;

import java.util.List;

import com.miaosha.vo.GoodsVo;

public interface GoodsService {

	List<GoodsVo> selectGoodsVoList();

	GoodsVo getGoodsById(Long goodsId);

	Boolean reduceStock(GoodsVo goodsVo);

	
}
