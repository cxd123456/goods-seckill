package com.miaosha.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

import com.miaosha.utils.ValidatorUtil;

public class IsMobileValidator implements ConstraintValidator<IsMobile, String>{

	private boolean require = false;
	
	@Override
	public void initialize(IsMobile constraintAnnotation) {
		require = constraintAnnotation.require();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (require) {
			return ValidatorUtil.isMobile(value);
		} else {
			if (StringUtils.isEmpty(value)) {
				return true;
			} else {
				return ValidatorUtil.isMobile(value);
			}
		}
	}
	
}
