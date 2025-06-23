package com.example.FinCore.vo.request;

import com.example.FinCore.constants.ConstantsMessage;

import jakarta.validation.constraints.NotBlank;

public class RregisterUserRequest {

	@NotBlank(message = ConstantsMessage.PARAM_ACCOUNT_ERROR)
	private String account;

	@NotBlank(message = ConstantsMessage.PARAM_NAME_ERROR)
	private String name;

	@NotBlank(message = ConstantsMessage.PARAM_PASSWORD_ERROR)
	private String password;

	private String phone;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
