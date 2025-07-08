package com.example.FinCore.vo.request;

import com.example.FinCore.constants.ConstantsMessage;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class UpdatePwdByEmailRequest {

	@NotBlank(message = ConstantsMessage.EMAIL_IS_NECESSARY)
	@Email(regexp = ConstantsMessage.EMAIL_PATTERN, message = ConstantsMessage.EMAIL_ADDRESS_IS_INVALID)
	private String account;

	@NotBlank(message = ConstantsMessage.PARAM_PASSWORD_ERROR)
	@Pattern(regexp = ConstantsMessage.PASSWORD_PATTERN, message = ConstantsMessage.PARAM_PASSWORD_FORMAT_ERROR)
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
