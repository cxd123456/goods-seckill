package com.seckill.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class GoodsEntity implements Serializable {
    private Long id;

    private String goods_name;

    private String goods_title;

    private String goods_img;

    private BigDecimal goods_price;

    private Integer goods_stock;

    private String goods_detail;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name == null ? null : goods_name.trim();
    }

    public String getGoods_title() {
        return goods_title;
    }

    public void setGoods_title(String goods_title) {
        this.goods_title = goods_title == null ? null : goods_title.trim();
    }

    public String getGoods_img() {
        return goods_img;
    }

    public void setGoods_img(String goods_img) {
        this.goods_img = goods_img == null ? null : goods_img.trim();
    }

    public BigDecimal getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(BigDecimal goods_price) {
        this.goods_price = goods_price;
    }

    public Integer getGoods_stock() {
        return goods_stock;
    }

    public void setGoods_stock(Integer goods_stock) {
        this.goods_stock = goods_stock;
    }

    public String getGoods_detail() {
        return goods_detail;
    }

    public void setGoods_detail(String goods_detail) {
        this.goods_detail = goods_detail == null ? null : goods_detail.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", goods_name=").append(goods_name);
        sb.append(", goods_title=").append(goods_title);
        sb.append(", goods_img=").append(goods_img);
        sb.append(", goods_price=").append(goods_price);
        sb.append(", goods_stock=").append(goods_stock);
        sb.append(", goods_detail=").append(goods_detail);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}