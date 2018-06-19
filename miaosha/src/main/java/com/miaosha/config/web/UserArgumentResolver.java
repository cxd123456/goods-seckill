package com.miaosha.config.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.miaosha.entity.MiaoshaUserEntity;
import com.miaosha.service.MiaoshaUserService;
import com.miaosha.service.impl.MiaoshaUserServiceImpl;

@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver{
	
	@Autowired
	private MiaoshaUserService miaoshaUserService;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		Class<?> clazz = parameter.getParameterType();
		return clazz == MiaoshaUserEntity.class;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
		
		String paramToken = request.getParameter(MiaoshaUserServiceImpl.COOKIE_NAME_TOKEN);
		String cookieToken = getCookieValue(request, MiaoshaUserServiceImpl.COOKIE_NAME_TOKEN);
		
		String token = null;
		
		if (StringUtils.isEmpty(paramToken)) {
			if (!StringUtils.isEmpty(cookieToken)) {
				token = cookieToken;
			}
		} else {
			token = paramToken;
		}
		
		if (StringUtils.isEmpty(token)) {
			return null;
		}

		return miaoshaUserService.getByToken(token, response);
	}

	private String getCookieValue(HttpServletRequest request, String cookieNameToken) {
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(cookieNameToken)) {
				return cookie.getValue();
			}
		}
		return null;
	}

}
