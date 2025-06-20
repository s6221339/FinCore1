package com.example.FinCore.vo.request;

import java.time.LocalDate;

public class UpdateUserRequest extends RregisterUserRequest {
	
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
