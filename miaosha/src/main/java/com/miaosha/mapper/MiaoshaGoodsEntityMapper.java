package com.miaosha.mapper;

import com.miaosha.entity.MiaoshaGoodsEntity;

public interface MiaoshaGoodsEntityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MiaoshaGoodsEntity record);

    int insertSelective(MiaoshaGoodsEntity record);

    MiaoshaGoodsEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MiaoshaGoodsEntity record);

    int updateByPrimaryKey(MiaoshaGoodsEntity record);
}