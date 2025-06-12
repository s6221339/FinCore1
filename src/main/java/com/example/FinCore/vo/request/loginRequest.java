package com.example.FinCore.vo.request;

import com.example.FinCore.constants.ConstantsMessage;

import jakarta.validation.constraints.NotBlank;

public record loginRequest(
		
		@NotBlank(message = ConstantsMessage.PARAM_ACCOUNT_ERROR)
		String account,
		
		@NotBlank(message = ConstantsMessage.PARAM_PASSWORD_ERROR)
		String password
		) {

}
