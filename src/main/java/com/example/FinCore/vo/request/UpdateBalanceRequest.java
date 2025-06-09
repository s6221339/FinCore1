package com.example.FinCore.vo.request;

import com.example.FinCore.constants.ConstantsMessage;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record UpdateBalanceRequest(
		@Min(value = 1, message = ConstantsMessage.BALANCE_ID_VALUE_ERROR)
		int balanceId,
		
		@NotBlank(message = ConstantsMessage.EMPTY_NAME_ERROR)
		String name
		) {

}
