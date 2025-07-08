package com.example.FinCore.vo.request;

import com.example.FinCore.constants.ConstantsMessage;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UpdatePasswordUserRequest(
		
		@NotBlank(message = ConstantsMessage.PARAM_ACCOUNT_ERROR)
		String account,
		
		@NotBlank(message = ConstantsMessage.PARAM_PASSWORD_ERROR)
		String oldPassword,
		
		@NotBlank(message = ConstantsMessage.PARAM_PASSWORD_ERROR)
	    @Pattern(regexp = ConstantsMessage.PASSWORD_PATTERN, message = ConstantsMessage.PARAM_PASSWORD_FORMAT_ERROR)
		String newPassword
		) {

}
