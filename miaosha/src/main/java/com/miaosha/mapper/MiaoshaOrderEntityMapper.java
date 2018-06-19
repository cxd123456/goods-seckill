package com.miaosha.mapper;

import com.miaosha.entity.MiaoshaOrderEntity;

public interface MiaoshaOrderEntityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MiaoshaOrderEntity record);

    int insertSelective(MiaoshaOrderEntity record);

    MiaoshaOrderEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MiaoshaOrderEntity record);

    int updateByPrimaryKey(MiaoshaOrderEntity record);
}