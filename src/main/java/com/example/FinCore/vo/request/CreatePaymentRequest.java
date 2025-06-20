package com.example.FinCore.vo.request;

import java.time.LocalDate;

import com.example.FinCore.constants.ConstantsMessage;
import com.example.FinCore.vo.RecurringPeriodVO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreatePaymentRequest(
		@Min(value = 1, message = ConstantsMessage.BALANCE_ID_VALUE_ERROR)
		int balanceId,
		
		String description,
		
		@NotBlank(message = ConstantsMessage.PARAM_TYPE_BLANK_ERROR)
		String type,
		
		@NotBlank(message = ConstantsMessage.PARAM_ITEM_BLANK_ERROR)
		String item,
		
		@Min(value = 0, message = ConstantsMessage.AMOUNT_NEGATIVE_ERROR)
		int amount,
		
		@Valid
		RecurringPeriodVO recurringPeriod,
		
		LocalDate recordDate
		) {

}
