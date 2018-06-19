package com.miaosha.utils;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class ValidatorUtil {

	public static final String REGEX_MOBILE = "^1[3|4|5|7|8][0-9]\\d{8}$";
	
	public static boolean isMobile(String mobile) {
		if (StringUtils.isEmpty(mobile)) return false;
		return Pattern.matches(REGEX_MOBILE, mobile);
	}
	
}
