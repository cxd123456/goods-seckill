package com.miaosha.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.miaosha.entity.MiaoshaGoodsEntity;
import com.miaosha.mapper.GoodsEntityMapper;
import com.miaosha.mapper.MiaoshaGoodsEntityMapper;
import com.miaosha.service.GoodsService;
import com.miaosha.vo.GoodsVo;

@Service
public class GoodsServiceImpl implements GoodsService {
	
	@Autowired
	private GoodsEntityMapper goodsEntityMapper;
	@Autowired
	private MiaoshaGoodsEntityMapper miaoshaGoodsEntityMapper;

	@Override
	public List<GoodsVo> selectGoodsVoList() {
		return goodsEntityMapper.selectGoodsVoList();
	}

	@Override
	public GoodsVo getGoodsById(Long goodsId) {
		return goodsEntityMapper.selectGoodsVoById(goodsId);
	}

	@Override
	public void reduceStock(GoodsVo goodsVo) {
		MiaoshaGoodsEntity miaoshaGoodsEntity = new MiaoshaGoodsEntity();
		miaoshaGoodsEntity.setGoods_id(goodsVo.getId());
		miaoshaGoodsEntityMapper.reduceStock(miaoshaGoodsEntity);
	}
	
	

}
