package com.miaosha.service;

import com.miaosha.common.Result;
import com.miaosha.vo.LoginVo;

public interface MiaoshaUserService {

	Result<Boolean> login(LoginVo loginVo);
	
}
