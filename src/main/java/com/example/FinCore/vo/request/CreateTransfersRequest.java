package com.example.FinCore.vo.request;

import com.example.FinCore.constants.ConstantsMessage;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreateTransfersRequest(
		@Min(value = 1, message = ConstantsMessage.BALANCE_ID_VALUE_ERROR)
		int fromBalance,
		
		@NotBlank(message = ConstantsMessage.PARAM_ACCOUNT_BLANK_ERROR)
		String toAccount,
		
		@Min(value = 0, message = ConstantsMessage.AMOUNT_NEGATIVE_ERROR)
		int amount,
		
		String description
		) {

}
