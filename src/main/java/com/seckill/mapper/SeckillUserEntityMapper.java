package com.seckill.mapper;

import com.seckill.entity.SeckillUserEntity;

public interface SeckillUserEntityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SeckillUserEntity record);

    int insertSelective(SeckillUserEntity record);

    SeckillUserEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SeckillUserEntity record);

    int updateByPrimaryKey(SeckillUserEntity record);
}