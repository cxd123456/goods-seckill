package com.miaosha.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.miaosha.common.CodeMsg;
import com.miaosha.common.Result;
import com.miaosha.entity.MiaoshaUserEntity;
import com.miaosha.exceptioin.GlobalException;
import com.miaosha.mapper.MiaoshaUserEntityMapper;
import com.miaosha.service.MiaoshaUserService;
import com.miaosha.vo.LoginVo;

@Service
public class MiaoshaUserServiceImpl implements MiaoshaUserService {
	
	@Autowired
	private MiaoshaUserEntityMapper miaoshaUserEntityMapper;

	@Override
	public Result<Boolean> login(LoginVo loginVo) {
		MiaoshaUserEntity user = miaoshaUserEntityMapper.selectByPrimaryKey(Long.valueOf(loginVo.getMobile()));
		if (user == null) {
			throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
		}
		
		String md5DigestAsHex = DigestUtils.md5DigestAsHex((loginVo.getPassword() + user.getSalt()).getBytes());
		if (!md5DigestAsHex.equals(user.getPassword())) {
			throw new GlobalException(CodeMsg.PASSWORD_ERROR);
		}
		return Result.success(true);
	}
	
	
	

}
