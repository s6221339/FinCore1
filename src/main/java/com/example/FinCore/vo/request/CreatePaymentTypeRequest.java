package com.example.FinCore.vo.request;

import com.example.FinCore.constants.ConstantsMessage;

import jakarta.validation.constraints.NotBlank;

public record CreatePaymentTypeRequest(
		@NotBlank(message = ConstantsMessage.PARAM_TYPE_BLANK_ERROR)
		String type,
		@NotBlank(message = ConstantsMessage.PARAM_ITEM_BLANK_ERROR)
		String item,
		@NotBlank(message = ConstantsMessage.PARAM_ACCOUNT_BLANK_ERROR)
		String account
		) {

}
