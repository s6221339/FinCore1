package com.example.FinCore.vo.request;

import com.example.FinCore.constants.ConstantsMessage;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record GetBudgetByBalanceIdRequest(
		@Min(value = 1, message = ConstantsMessage.BALANCE_ID_VALUE_ERROR)
		int balanceId,
		
		@Min(value = 0, message = ConstantsMessage.INVALID_DATE_ERROR)
		@Max(value = 9999, message = ConstantsMessage.INVALID_DATE_ERROR)
		int year,
		
		@Min(value = 1, message = ConstantsMessage.INVALID_DATE_ERROR)
		@Max(value = 12, message = ConstantsMessage.INVALID_DATE_ERROR)
		int month
		) {

}
