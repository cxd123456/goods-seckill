package com.miaosha.service;

import javax.servlet.http.HttpServletResponse;

import com.miaosha.common.Result;
import com.miaosha.entity.MiaoshaUserEntity;
import com.miaosha.vo.LoginVo;

public interface MiaoshaUserService {

	Result<Boolean> login(LoginVo loginVo, HttpServletResponse response);
	
	MiaoshaUserEntity getByToken(String token, HttpServletResponse response);
}
