package com.miaosha.service.impl;

import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.miaosha.common.CodeMsg;
import com.miaosha.common.Result;
import com.miaosha.config.redis.MiaoshaUserKey;
import com.miaosha.config.redis.RedisService;
import com.miaosha.entity.MiaoshaUserEntity;
import com.miaosha.exceptioin.GlobalException;
import com.miaosha.mapper.MiaoshaUserEntityMapper;
import com.miaosha.service.MiaoshaUserService;
import com.miaosha.vo.LoginVo;

@Service
public class MiaoshaUserServiceImpl implements MiaoshaUserService {

	public static final String COOKIE_NAME_TOKEN = "token";

	@Autowired
	private MiaoshaUserEntityMapper miaoshaUserEntityMapper;
	@Autowired
	private RedisService redisService;

	@Override
	public Result<Boolean> login(LoginVo loginVo, HttpServletResponse response) {
		MiaoshaUserEntity user = miaoshaUserEntityMapper.selectByPrimaryKey(Long.valueOf(loginVo.getMobile()));
		if (user == null) {
			throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
		}

		String md5DigestAsHex = DigestUtils.md5DigestAsHex((loginVo.getPassword() + user.getSalt()).getBytes());
		if (!md5DigestAsHex.equals(user.getPassword())) {
			throw new GlobalException(CodeMsg.PASSWORD_ERROR);
		}

		// 生成token并缓存到redis
		String token = UUID.randomUUID().toString().replace("-", "");
//		redisService.set(MiaoshaUserKey.token, token, user);
//
//		// 把token设置到cookie
//		Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
//		cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
//		cookie.setPath("/");
//		response.addCookie(cookie);
		addCookie(token, user, response);

		return Result.success(true);
	}

	@Override
	public MiaoshaUserEntity getByToken(String token, HttpServletResponse response) {

		if (StringUtils.isEmpty(token)) {
			return null;
		}

		MiaoshaUserEntity miaoshaUserEntity = redisService.get(MiaoshaUserKey.token, token, MiaoshaUserEntity.class);

		if (miaoshaUserEntity != null) {
			// 延长token有效期
			addCookie(token, miaoshaUserEntity, response);
		}

		return miaoshaUserEntity;
	}

	private void addCookie(String token, MiaoshaUserEntity user, HttpServletResponse response) {
		redisService.set(MiaoshaUserKey.token, token, user);

		// 把token设置到cookie
		Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
		cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
		cookie.setPath("/");
		response.addCookie(cookie);
	}

}
