package com.seckill.service.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.seckill.entity.SeckillUserEntity;
import com.seckill.mapper.SeckillUserEntityMapper;
import com.seckill.service.LoginService;
import com.seckill.util.RedisUtil;
import com.seckill.util.ResultCode;

@Service
public class LoginServiceImpl implements LoginService {

	@Autowired
	private SeckillUserEntityMapper seckillUserEntityMapper;

	@Override
	public ResultCode<String> registerUser(SeckillUserEntity user) {
		ResultCode<String> result = new ResultCode<>();

		if (user.getId() == null)
			return result.ERROE("手机号不能为空");
		if (user.getPassword() == null)
			return result.ERROE("密码不能为空");

		SeckillUserEntity userEntity = seckillUserEntityMapper.selectByPrimaryKey(user.getId());
		if (userEntity != null) {
			return result.ERROE("手机号已经被注册");
		}

		user.setNickname(UUID.randomUUID().toString().substring(0, 8));
		user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));

		seckillUserEntityMapper.insertSelective(user);

		return result.OK(null);
	}

	@Override
	public ResultCode<String> loginUser(String sessionID, SeckillUserEntity user) {
		ResultCode<String> result = new ResultCode<>();

		if (user.getId() == null)
			return result.ERROE("手机号不能为空");
		if (user.getPassword() == null)
			return result.ERROE("密码不能为空");
		
		SeckillUserEntity userEntity = seckillUserEntityMapper.selectByPrimaryKey(user.getId());
		if (userEntity == null)
			return result.ERROE("手机号未注册");
		
		if (!DigestUtils.md5DigestAsHex(user.getPassword().getBytes()).equals(userEntity.getPassword())) {
			return result.ERROE("密码不正确");
		}
		
		RedisUtil.setValue("user_login_" + sessionID, user.getId().toString());

		return result.OK(null);
	}

	public static void main(String[] args) {
		System.out.println(DigestUtils.md5DigestAsHex("123".getBytes()));
	}

}
