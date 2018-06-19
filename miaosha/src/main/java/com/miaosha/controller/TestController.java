package com.miaosha.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.miaosha.common.CodeMsg;
import com.miaosha.common.Result;
import com.miaosha.config.redis.RedisService;
import com.miaosha.config.redis.UserKey;
import com.miaosha.entity.User;
import com.miaosha.service.UserService;

@Controller
@RequestMapping("/test")
public class TestController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private RedisService redisService;
	
	@RequestMapping("/success")
	@ResponseBody
	public Result<String> success() {
		return Result.success("ok");
	}
	
	@RequestMapping("/error")
	@ResponseBody
	public Result<String> error() {
		return Result.error(CodeMsg.SERVER_ERROR);
	}
	
	@RequestMapping("/thymeleaf")
	public String thymeleaf(Model model) {
		model.addAttribute("name", "zhangsan");
		return "hello";
	}
	
	@RequestMapping("/getUserById")
	@ResponseBody
	public Result<User> getUserById(int id) {
		return Result.success(userService.getUserById(id));
	}
	
	@RequestMapping("/insert")
	@ResponseBody
	public Result<User> insert() {
		userService.Insert();
		return Result.success(null);
	}
	
	@RequestMapping("/redisGet")
	@ResponseBody
	public Result<User> redisGet() {
		User user = new User();
		user.setId(1);
		user.setName("lisi");
		redisService.set(UserKey.getById, "1", user);
		
		redisService.incr(UserKey.getById, "2");
		System.out.println(redisService.get(UserKey.getById, "2", String.class));
		return Result.success(redisService.get(UserKey.getById, "1", User.class));
	}

}
