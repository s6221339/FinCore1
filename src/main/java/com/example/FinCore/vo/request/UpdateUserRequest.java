package com.example.FinCore.vo.request;

import java.time.LocalDate;

import com.example.FinCore.constants.ConstantsMessage;

import jakarta.validation.constraints.NotBlank;

public class UpdateUserRequest {

	@NotBlank(message = ConstantsMessage.PARAM_ACCOUNT_ERROR)
	private String account;

	@NotBlank(message = ConstantsMessage.PARAM_NAME_ERROR)
	private String name;

	private String phone;
	
	private LocalDate birthday;
	
	private String avatar; // 新增，接收 base64，例如 data:image/png;base64

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public LocalDate getBirthday() {
		return birthday;
	}

	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}

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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	
	
}
