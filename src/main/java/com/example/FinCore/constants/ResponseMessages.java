package com.example.FinCore.constants;

public enum ResponseMessages 
{
	
	SUCCESS(200, "成功！"),
	ACCOUNT_NOT_FOUND(404, "查無此帳號！"),
	BALANCE_NOT_FOUND(404, "查無此帳戶！"),
	ITEM_EXISTED(400, "已存在相同物件。"),
	PAST_RECORD_DATE(400, "該款項不能記錄在過去時間。"),
	FUTURE_RECORD_DATE(400, "該款項不能記錄在未來時間。");
	
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
