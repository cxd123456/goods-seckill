package com.miaosha.mapper;

import org.apache.ibatis.annotations.Update;

import com.miaosha.entity.MiaoshaGoodsEntity;

public interface MiaoshaGoodsEntityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MiaoshaGoodsEntity record);

    int insertSelective(MiaoshaGoodsEntity record);

    MiaoshaGoodsEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MiaoshaGoodsEntity record);

    int updateByPrimaryKey(MiaoshaGoodsEntity record);

    @Update("UPDATE miaosha_goods SET stock_count = stock_count -1 WHERE goods_id = #{goods_id}")
	int reduceStock(MiaoshaGoodsEntity miaoshaGoodsEntity);
}