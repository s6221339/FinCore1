package com.example.FinCore.vo.request;

import com.example.FinCore.constants.ConstantsMessage;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record StatisticsRequest(
		@NotBlank(message = ConstantsMessage.PARAM_ACCOUNT_BLANK_ERROR)
		String account,
		
		@Max(value = 9999, message = ConstantsMessage.INVALID_DATE_ERROR)
		@Min(value = 2001, message = ConstantsMessage.INVALID_DATE_ERROR)
		int year,
		
		@Max(value = 12, message = ConstantsMessage.INVALID_DATE_ERROR)
		@Min(value = 0, message = ConstantsMessage.INVALID_DATE_ERROR)
		int month
		) {

}
