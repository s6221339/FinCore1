package com.example.FinCore.vo.response;

import com.example.FinCore.constants.ResponseMessages;

public class AICallbackResponse extends BasicResponse 
{
	
	private String text;

	public AICallbackResponse() {
		super();
	}

	public AICallbackResponse(int code, String message) {
		super(code, message);
	}

	public AICallbackResponse(ResponseMessages res) {
		super(res);
	}
	
	public AICallbackResponse(int code, String message, String text) {
		super(code, message);
		this.text = text;
	}

	public AICallbackResponse(ResponseMessages res, String text) {
		super(res);
		this.text = text;
	}

	public String getText() {
		return text;
	}
	
}
