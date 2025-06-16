package com.example.FinCore.vo.request;

import com.example.FinCore.constants.ConstantsMessage;

import jakarta.validation.constraints.Min;

public record CreateTransfersRequest(
		@Min(value = 1, message = ConstantsMessage.BALANCE_ID_VALUE_ERROR)
		int fromBalance,
		
		@Min(value = 1, message = ConstantsMessage.BALANCE_ID_VALUE_ERROR)
		int toBalance,
		
		@Min(value = 0, message = ConstantsMessage.AMOUNT_NEGATIVE_ERROR)
		int amount,
		
		String description
		) {

}
