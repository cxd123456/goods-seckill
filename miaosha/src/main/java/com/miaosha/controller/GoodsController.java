package com.miaosha.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.miaosha.entity.MiaoshaUserEntity;
import com.miaosha.service.GoodsService;
import com.miaosha.vo.GoodsVo;

/**
 * 商品展示controller
 * 
 * @创建时间：2018年6月20日
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {

	@Autowired
	private GoodsService goodsService;

	/**
	 * 商品列表展示
	 * @param model
	 * @param response
	 * @param miaoshaUserEntity
	 * @return
	 */
	@RequestMapping("to_list")
	public String toList(Model model, HttpServletResponse response,
//			@CookieValue(value = MiaoshaUserServiceImpl.COOKIE_NAME_TOKEN, required = false) String cookieToken,
//			@RequestParam(value = MiaoshaUserServiceImpl.COOKIE_NAME_TOKEN, required = false) String paramToken,
			MiaoshaUserEntity miaoshaUserEntity
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
	
	/**
	 * 商品详情
	 * @param model
	 * @param goodsId
	 * @param user
	 * @return
	 */
	@RequestMapping("to_detail/{goodsId}")
	public String toDetail(Model model, @PathVariable(value = "goodsId") Long goodsId, 
			MiaoshaUserEntity user){
		GoodsVo goods = goodsService.getGoodsById(goodsId);
		
		long startTime = goods.getStart_time().getTime();
		long endTime = goods.getEnd_time().getTime();
		long nowTime = System.currentTimeMillis();
		
		int miaoshaStatus = 0;
		int remainSeconds = 0;
		
		if (startTime > nowTime) {	// 秒杀未开始
			miaoshaStatus = 0;
			remainSeconds = (int)((startTime - nowTime) / 1000);
		} else if (nowTime > endTime) {	// 秒杀已结束
			miaoshaStatus = 2;
			remainSeconds = -1;
		} else {	// 秒杀进行中
			miaoshaStatus = 1;
			remainSeconds = 0;
		}
		
		model.addAttribute("user", user);
		model.addAttribute("goods", goods);
		model.addAttribute("miaoshaStatus", miaoshaStatus);
		model.addAttribute("remainSeconds", remainSeconds);
		return "goods_detail";
	}
	

}
