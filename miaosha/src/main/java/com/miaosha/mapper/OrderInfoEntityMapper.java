package com.miaosha.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.miaosha.entity.MiaoshaOrderEntity;
import com.miaosha.entity.OrderInfoEntity;

public interface OrderInfoEntityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrderInfoEntity record);

    int insertSelective(OrderInfoEntity record);

    OrderInfoEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderInfoEntity record);

    int updateByPrimaryKey(OrderInfoEntity record);

    @Select("SELECT * FROM order_info WHERE user_id = #{userId} AND goods_id = #{goodsId}")
	MiaoshaOrderEntity getMiaoshaOrderByUserIdGoodsId(@Param("userId") Long userId, @Param("goodsId") Long goodsId);
}