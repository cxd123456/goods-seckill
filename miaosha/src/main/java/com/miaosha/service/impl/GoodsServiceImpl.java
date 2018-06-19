package com.miaosha.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.miaosha.mapper.GoodsEntityMapper;
import com.miaosha.service.GoodsService;
import com.miaosha.vo.GoodsVo;

@Service
public class GoodsServiceImpl implements GoodsService {
	
	@Autowired
	private GoodsEntityMapper goodsEntityMapper;

	@Override
	public List<GoodsVo> selectGoodsVoList() {
		return goodsEntityMapper.selectGoodsVoList();
	}

	@Override
	public GoodsVo getGoodsById(Long goodsId) {
		return goodsEntityMapper.selectGoodsVoById(goodsId);
	}
	
	

}
