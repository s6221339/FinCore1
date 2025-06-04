package com.example.FinCore.vo.response;

import com.example.FinCore.constants.ResponseMessages;

/**
 * 最基本的回應封包資料，僅有狀態碼與訊息。
 */
public class BasicResponse 
{
	
	private int code;
	
	private String message;
	
	public BasicResponse() {
		super();
	}

	public BasicResponse(int code, String message) {
		super();
		this.code = code;
		this.message = message;
	}
	
	public BasicResponse(ResponseMessages res)
	{
		code = res.getCode();
		message = res.getMessage();
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "{code: " + code + ", message: '" + message + "'}";
	}
	
	
	
}
