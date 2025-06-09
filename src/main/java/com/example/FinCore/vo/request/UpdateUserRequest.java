package com.example.FinCore.vo.request;

import com.example.FinCore.constants.ConstantsMessage;

import jakarta.validation.constraints.Min;

public class UpdateUserRequest extends CreateUserRequest {

	@Min(value = 1, message = ConstantsMessage.PARAM_ACCOUNT_ERROR)
	private String account;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
	
	
}
