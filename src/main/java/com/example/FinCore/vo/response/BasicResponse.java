package com.example.FinCore.vo.response;

public class BasicResponse {
	
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
	
	

}
