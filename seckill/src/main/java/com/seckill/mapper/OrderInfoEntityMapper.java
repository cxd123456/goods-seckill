package com.seckill.mapper;

import com.seckill.entity.OrderInfoEntity;

public interface OrderInfoEntityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrderInfoEntity record);

    Long insertSelective(OrderInfoEntity record);

    OrderInfoEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderInfoEntity record);

    int updateByPrimaryKey(OrderInfoEntity record);
}