package com.miaosha.common;

import java.io.Serializable;

/**
 * 服务接口统一返回格式
 * 
 * @创建时间：2018年6月17日
 */
public class Result<T> implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5870610836447616239L;
	
	private int code;
	private String msg;
	private T data;
	
	public static <T> Result<T> success(T data) {
		return new Result<T>(data);
	}
	
	public static <T> Result<T> error(CodeMsg codeMsg) {
		return new Result<T>(codeMsg);
	}
	
	private Result(T data) {
		this.code = 0;
		this.msg = "success";
		this.data = data;
	}
	
	private Result(CodeMsg codeMsg) {
		if (codeMsg == null) 
			return;
		this.code = codeMsg.getCode();
		this.msg = codeMsg.getMsg();
	}
	
	public Result() {}
	
	public int getCode() {
		return code;
	}
	public String getMsg() {
		return msg;
	}
	public T getData() {
		return data;
	}

}
