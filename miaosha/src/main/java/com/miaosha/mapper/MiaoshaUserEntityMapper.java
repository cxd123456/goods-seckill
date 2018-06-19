package com.miaosha.mapper;

import com.miaosha.entity.MiaoshaUserEntity;

public interface MiaoshaUserEntityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MiaoshaUserEntity record);

    int insertSelective(MiaoshaUserEntity record);

    MiaoshaUserEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MiaoshaUserEntity record);

    int updateByPrimaryKey(MiaoshaUserEntity record);
}