package com.miaosha.mapper;

import com.miaosha.entity.OrderInfoEntity;

public interface OrderInfoEntityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrderInfoEntity record);

    int insertSelective(OrderInfoEntity record);

    OrderInfoEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderInfoEntity record);

    int updateByPrimaryKey(OrderInfoEntity record);
}