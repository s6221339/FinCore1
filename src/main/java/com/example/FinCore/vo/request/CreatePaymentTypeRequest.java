package com.example.FinCore.vo.request;

import com.example.FinCore.constants.ConstantsMessage;

import jakarta.validation.constraints.NotBlank;

/**
 * 該請求用來創建款項類型，包含類型、細項、帳號三個資料，三個資料皆不得為空值。
 */
public record CreatePaymentTypeRequest(
		@NotBlank(message = ConstantsMessage.PARAM_TYPE_BLANK_ERROR)
		String type,
		
		@NotBlank(message = ConstantsMessage.PARAM_ITEM_BLANK_ERROR)
		String item,
		
		@NotBlank(message = ConstantsMessage.PARAM_ACCOUNT_BLANK_ERROR)
		String account
		) {

}
