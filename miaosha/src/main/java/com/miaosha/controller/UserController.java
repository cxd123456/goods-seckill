package com.miaosha.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.miaosha.common.Result;
import com.miaosha.entity.MiaoshaUserEntity;
import com.miaosha.service.MiaoshaUserService;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private MiaoshaUserService miaoshaUserService;
	
	@RequestMapping("/info")
	@ResponseBody
	public Result<MiaoshaUserEntity> info(MiaoshaUserEntity user) {
		return Result.success(user);
	}
}
