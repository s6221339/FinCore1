package com.example.FinCore.vo.request;

import java.time.LocalDate;

import com.example.FinCore.constants.ConstantsMessage;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class UpdateUserRequest extends CreateUserRequest {
	
	private LocalDate birthday;
	
	private byte[] avatar;

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
	
}
