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
	
	private byte[] avatar;
	
	/**
	 * 前端傳 Base64 格式頭像（如 data:image/png;base64,xxxx）
	 */
	private String avatarString; // 新增，接收 base64
	
	public String getAvatarString() { return avatarString; }
	
	public void setAvatarString(String avatarString) { this.avatarString = avatarString; }

	public LocalDate getBirthday() {
		return birthday;
	}

	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}

	public byte[] getAvatar() {
		return avatar;
	}

	public void setAvatar(byte[] avatar) {
		this.avatar = avatar;
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
