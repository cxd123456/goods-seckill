package com.seckill.util;

public class ResultCode<T> {

	/** ok, error */
	private String code;
	
	private String msg;
	
	private T result;
	
	public ResultCode<T> OK(T result) {
		this.code = "ok";
		this.result = result;
		return this;
	}
	
	public ResultCode<T> ERROE(String msg) {
		this.code = "error";
		this.msg = msg;
		return this;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public T getResult() {
		return result;
	}
	public void setResult(T result) {
		this.result = result;
	}
	@Override
	public String toString() {
		return "ResultCode [code=" + code + ", msg=" + msg + ", result=" + result + "]";
	}
	
}
