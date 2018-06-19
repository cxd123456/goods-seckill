package com.miaosha.common;

import java.io.Serializable;

/**
 * 响应状态码对应异常信息封装
 * 
 * @创建时间：2018年6月18日
 */
public class CodeMsg implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8374377780158590660L;
	
	private int code;
	private String msg;
	
	// 通用状态码
	public static final CodeMsg SUCCESS = new CodeMsg(0, "success");
	public static final CodeMsg SERVER_ERROR = new CodeMsg(500100, "服务端异常");
	
	// 登录模块
	
	// 订单模块
	
	// 秒杀模块
	
	// 商品模块
	
	private CodeMsg(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	
	public CodeMsg() {}
	
	public int getCode() {
		return code;
	}
	public String getMsg() {
		return msg;
	}
}
