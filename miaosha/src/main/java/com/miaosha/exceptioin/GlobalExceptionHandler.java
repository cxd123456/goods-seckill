package com.miaosha.exceptioin;

import java.util.List;

import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.miaosha.common.CodeMsg;
import com.miaosha.common.Result;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(Exception.class)
	public Result<String> exceptionHandler(Exception e) {
		e.printStackTrace();
		return Result.error(CodeMsg.SERVER_ERROR);
	}
	
	@ExceptionHandler(BindException.class)
	public Result<String> bindExceptionHandler(BindException e) {
		e.printStackTrace();
		List<ObjectError> allErrors = e.getAllErrors();
		ObjectError objectError = allErrors.get(0);
		String defaultMessage = objectError.getDefaultMessage();
		return Result.error(CodeMsg.BIND_ERROR.fillArgs(defaultMessage));
	}
	
	@ExceptionHandler(GlobalException.class)
	public Result<String> globalExceptionHandler(GlobalException e) {
		e.printStackTrace();
		return Result.error(e.getCodeMsg());
	}

}
