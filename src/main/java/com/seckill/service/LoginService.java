package com.seckill.service;

import com.seckill.entity.SeckillUserEntity;
import com.seckill.util.ResultCode;

public interface LoginService {

	/**
	 * 注册
	 * @param user
	 * @return
	 */
	ResultCode<String> registerUser(SeckillUserEntity user);
	
	/**
	 * 登录
	 * @param user
	 * @return
	 */
	ResultCode<String> loginUser(String sessionID, SeckillUserEntity user);
	
}
