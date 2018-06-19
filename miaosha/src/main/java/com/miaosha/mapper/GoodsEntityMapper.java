package com.miaosha.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.miaosha.entity.GoodsEntity;
import com.miaosha.vo.GoodsVo;

public interface GoodsEntityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(GoodsEntity record);

    int insertSelective(GoodsEntity record);

    GoodsEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GoodsEntity record);

    int updateByPrimaryKeyWithBLOBs(GoodsEntity record);

    int updateByPrimaryKey(GoodsEntity record);
    
    @Select("SELECT mg.miaosha_price, mg.stock_count, mg.start_time, mg.end_time, g.* FROM miaosha_goods mg LEFT JOIN goods g ON mg.goods_id = g.id")
    List<GoodsVo> selectGoodsVoList();
}