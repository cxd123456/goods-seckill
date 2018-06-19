package com.miaosha.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.miaosha.entity.MiaoshaUserEntity;
import com.miaosha.service.GoodsService;
import com.miaosha.service.MiaoshaUserService;
import com.miaosha.service.impl.MiaoshaUserServiceImpl;
import com.miaosha.vo.GoodsVo;

@Controller
@RequestMapping("/goods")
public class GoodsController {

	@Autowired
	private MiaoshaUserService miaoshaUserService;
	@Autowired
	private GoodsService goodsService;

	@RequestMapping("to_list")
	public String toList(Model model, HttpServletResponse response
//			@CookieValue(value = MiaoshaUserServiceImpl.COOKIE_NAME_TOKEN, required = false) String cookieToken,
//			@RequestParam(value = MiaoshaUserServiceImpl.COOKIE_NAME_TOKEN, required = false) String paramToken,
//			MiaoshaUserEntity miaoshaUserEntity
			) {

//		String token = null;
//
//		if (StringUtils.isEmpty(paramToken)) {
//			if (!StringUtils.isEmpty(cookieToken)) {
//				token = cookieToken;
//			}
//		} else {
//			token = paramToken;
//		}
//		
//		if (StringUtils.isEmpty(token)) {
//			return "/login";
//		}
//
//		MiaoshaUserEntity miaoshaUserEntity = miaoshaUserService.getByToken(token, response);

//		if (miaoshaUserEntity == null) {
//			return "/login";
//		}
//
//		model.addAttribute("user", miaoshaUserEntity);
		
		// 查询商品列表
		List<GoodsVo> list = goodsService.selectGoodsVoList();
		
		model.addAttribute("goodsList", list);

		return "goods_list";
	}

}
