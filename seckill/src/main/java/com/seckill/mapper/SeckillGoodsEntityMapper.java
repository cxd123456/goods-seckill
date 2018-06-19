package com.seckill.mapper;

import com.seckill.entity.SeckillGoodsEntity;

public interface SeckillGoodsEntityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SeckillGoodsEntity record);

    int insertSelective(SeckillGoodsEntity record);

    SeckillGoodsEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SeckillGoodsEntity record);

    int updateByPrimaryKey(SeckillGoodsEntity record);

    SeckillGoodsEntity selectEnableGoods(Long seckill_goods_id);
    
    int updateSeckillGoodsStockCount(Long seckill_goods_id);
    
    SeckillGoodsEntity selectGoodsForCreateOrder(Long seckill_goods_id);
}