package com.miaosha.vo;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.miaosha.validator.IsMobile;

public class LoginVo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3066588663427990384L;
	
	@NotNull
	@IsMobile
	private String mobile;
	@NotEmpty
	private String password;
	
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString() {
		return "LoginVo [mobile=" + mobile + ", password=" + password + "]";
	}

}
