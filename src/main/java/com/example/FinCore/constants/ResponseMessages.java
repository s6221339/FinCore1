package com.example.FinCore.constants;

public enum ResponseMessages 
{
	
	SUCCESS(200, "成功！");
	
	private int code;
	
	private String message;

	private ResponseMessages(int code, String message) 
	{
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
	
}
