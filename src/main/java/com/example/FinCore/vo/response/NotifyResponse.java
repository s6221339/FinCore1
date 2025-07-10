package com.example.FinCore.vo.response;

import com.example.FinCore.constants.ResponseMessages;

public class NotifyResponse extends BasicResponse{
	
	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public NotifyResponse(String status) {
		super();
		this.status = status;
	}

	public NotifyResponse() {
		super();
		
	}

	public NotifyResponse(int code, String message, String status) {
		super(code, message);
		this.status = status;
	}

	public NotifyResponse(ResponseMessages res, String status) {
		super(res);
		this.status = status;
	}
	
	

}
