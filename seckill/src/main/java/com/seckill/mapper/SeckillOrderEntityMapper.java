package com.seckill.mapper;

import org.apache.ibatis.annotations.Param;

import com.seckill.entity.SeckillOrderEntity;

public interface SeckillOrderEntityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SeckillOrderEntity record);

    int insertSelective(SeckillOrderEntity record);

    SeckillOrderEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SeckillOrderEntity record);

    int updateByPrimaryKey(SeckillOrderEntity record);
    
    SeckillOrderEntity selectSeckillOrder(@Param("user_id")Long user_id, @Param("goods_id")Long goods_id);
}