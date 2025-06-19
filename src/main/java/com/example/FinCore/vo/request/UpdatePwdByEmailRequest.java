package com.example.FinCore.vo.request;

import com.example.FinCore.constants.ConstantsMessage;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdatePwdByEmailRequest {

	@NotBlank(message = ConstantsMessage.EMAIL_IS_NECESSARY)
	@Email(regexp = ConstantsMessage.EMAIL_PATTERN, message = ConstantsMessage.EMAIL_ADDRESS_IS_INVALID)
	private String account;

	// 1. 必須輸入新密碼。2. 密碼長度介於8~16間。3. 新密碼格式檢查。
	@NotBlank(message = ConstantsMessage.NEW_PASSWORD_IS_NECESSARY)
	@Size(min = 8, max = 16, message = ConstantsMessage.PARAM_PASSWORD_LENGTH_ERROR)
//	@Pattern(regexp = ConstantsMessage.PASSWORD_PATTERN, message = ConstantsMessage.PARAM_PASSWORD_COMPLEXITY_ERROR)
	private String newPassword;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
}
