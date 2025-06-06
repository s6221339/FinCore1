package com.example.FinCore.vo.request;

import java.time.LocalDate;

import com.example.FinCore.constants.ConstantsMessage;
import com.example.FinCore.vo.RecurringPeriodVO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 更新款項的請求資料
 */
public record UpdatePaymentRequest(
		@Min(value = 1, message = ConstantsMessage.PAYMENT_ID_VALUE_ERROR)
		int paymentId,
		
		String description,
		
		@NotBlank(message = ConstantsMessage.PARAM_TYPE_BLANK_ERROR)
		String type,
		
		@NotBlank(message = ConstantsMessage.PARAM_ITEM_BLANK_ERROR)
		String item,
		
		@Min(value = 0, message = ConstantsMessage.AMOUNT_NEGATIVE_ERROR)
		int amount,
		
		@Valid
		RecurringPeriodVO recurringPeriod,
		
		@NotNull(message = ConstantsMessage.INVALID_RECORD_DATE)
		LocalDate recordDate
		) {

}
