package com.miaosha.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.miaosha.entity.GoodsEntity;

public class GoodsVo extends GoodsEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8039682723335636119L;
	
    private BigDecimal miaosha_price;

    private Integer stock_count;

    private Date start_time;

    private Date end_time;

	public BigDecimal getMiaosha_price() {
		return miaosha_price;
	}

	public void setMiaosha_price(BigDecimal miaosha_price) {
		this.miaosha_price = miaosha_price;
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

}
