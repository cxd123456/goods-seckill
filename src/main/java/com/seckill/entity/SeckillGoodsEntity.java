package com.seckill.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class SeckillGoodsEntity implements Serializable {
    private Long id;

    private Long goods_id;

    private BigDecimal seckill_price;

    private Integer stock_count;

    private Date start_time;

    private Date end_time;
    
    private String goods_name;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(Long goods_id) {
        this.goods_id = goods_id;
    }

    public BigDecimal getSeckill_price() {
        return seckill_price;
    }

    public void setSeckill_price(BigDecimal seckill_price) {
        this.seckill_price = seckill_price;
    }

    public Integer getStock_count() {
        return stock_count;
    }

    public void setStock_count(Integer stock_count) {
        this.stock_count = stock_count;
    }

    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }
    
    public String getGoods_name() {
		return goods_name;
	}

	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", goods_id=").append(goods_id);
        sb.append(", seckill_price=").append(seckill_price);
        sb.append(", stock_count=").append(stock_count);
        sb.append(", start_time=").append(start_time);
        sb.append(", end_time=").append(end_time);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}