package com.example.FinCore.constants;

public enum ResponseMessages 
{
	
	SUCCESS(200, "成功！"),
	ACCOUNT_NOT_FOUND(404, "查無此帳號！"),
	ITEM_EXISTED(400, "已存在相同物件。");
	
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
