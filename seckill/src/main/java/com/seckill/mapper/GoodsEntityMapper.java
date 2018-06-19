package com.seckill.mapper;

import com.seckill.entity.GoodsEntity;

public interface GoodsEntityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(GoodsEntity record);

    int insertSelective(GoodsEntity record);

    GoodsEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GoodsEntity record);

    int updateByPrimaryKeyWithBLOBs(GoodsEntity record);

    int updateByPrimaryKey(GoodsEntity record);
}