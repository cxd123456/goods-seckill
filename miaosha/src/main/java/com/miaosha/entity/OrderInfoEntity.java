package com.miaosha.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class OrderInfoEntity implements Serializable {
    private Long id;

    private Long user_id;

    private Long goods_id;

    private Long delivery_addr_id;

    private String goods_name;

    private Integer goods_count;

    private BigDecimal goods_price;

    private Byte order_channel;

    private Byte status;

    private Date crate_time;

    private Date pay_time;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(Long goods_id) {
        this.goods_id = goods_id;
    }

    public Long getDelivery_addr_id() {
        return delivery_addr_id;
    }

    public void setDelivery_addr_id(Long delivery_addr_id) {
        this.delivery_addr_id = delivery_addr_id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name == null ? null : goods_name.trim();
    }

    public Integer getGoods_count() {
        return goods_count;
    }

    public void setGoods_count(Integer goods_count) {
        this.goods_count = goods_count;
    }

    public BigDecimal getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(BigDecimal goods_price) {
        this.goods_price = goods_price;
    }

    public Byte getOrder_channel() {
        return order_channel;
    }

    public void setOrder_channel(Byte order_channel) {
        this.order_channel = order_channel;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Date getCrate_time() {
        return crate_time;
    }

    public void setCrate_time(Date crate_time) {
        this.crate_time = crate_time;
    }

    public Date getPay_time() {
        return pay_time;
    }

    public void setPay_time(Date pay_time) {
        this.pay_time = pay_time;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", user_id=").append(user_id);
        sb.append(", goods_id=").append(goods_id);
        sb.append(", delivery_addr_id=").append(delivery_addr_id);
        sb.append(", goods_name=").append(goods_name);
        sb.append(", goods_count=").append(goods_count);
        sb.append(", goods_price=").append(goods_price);
        sb.append(", order_channel=").append(order_channel);
        sb.append(", status=").append(status);
        sb.append(", crate_time=").append(crate_time);
        sb.append(", pay_time=").append(pay_time);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}