package com.example.FinCore.vo.request;

import com.example.FinCore.constants.ConstantsMessage;

import jakarta.validation.constraints.Min;

public record UpdateBalanceRequest(
		@Min(value = 1, message = ConstantsMessage.BALANCE_ID_VALUE_ERROR)
		int balanceId,
		
		String name,
		
		int savings
		) {

}
