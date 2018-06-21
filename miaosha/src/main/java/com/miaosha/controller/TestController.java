package com.miaosha.controller;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.miaosha.common.CodeMsg;
import com.miaosha.common.Result;
import com.miaosha.config.redis.MiaoshaUserKey;
import com.miaosha.config.redis.RedisService;
import com.miaosha.config.redis.UserKey;
import com.miaosha.entity.User;
import com.miaosha.service.GoodsService;
import com.miaosha.service.UserService;
import com.miaosha.vo.GoodsVo;

@Controller
@RequestMapping("/test")
public class TestController {
	
	private static final Logger LOG = LoggerFactory.getLogger(MiaoshaController.class);
	
	@Autowired
	private UserService userService;
	@Autowired
	private RedisService redisService;
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;
	
	@RequestMapping("/success")
	@ResponseBody
	public Result<String> success(String id) {
		LOG.info("****id = " + id + "****");
		
//		redisService.set(MiaoshaUserKey.token, id, id);
//		redisTemplate.opsForValue().set(UUID.randomUUID().toString(), UUID.randomUUID().toString());
		List<GoodsVo> list = goodsService.selectGoodsVoList();
		
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
