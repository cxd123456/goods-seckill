package com.miaosha.exceptioin;

import com.miaosha.common.CodeMsg;

public class GlobalException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private CodeMsg codeMsg;
	
	public GlobalException(CodeMsg codeMsg) {
		
		super(codeMsg.toString());
		
		this.codeMsg = codeMsg;
		
	}

	public CodeMsg getCodeMsg() {
		return codeMsg;
	}

	public void setCodeMsg(CodeMsg codeMsg) {
		this.codeMsg = codeMsg;
	}
	
}
