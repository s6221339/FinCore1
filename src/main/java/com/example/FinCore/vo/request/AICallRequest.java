package com.example.FinCore.vo.request;

import org.hibernate.validator.constraints.Range;

import com.example.FinCore.constants.ConstantsMessage;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record AICallRequest(
		@NotBlank(message = ConstantsMessage.PARAM_ACCOUNT_BLANK_ERROR)
		String account, 
		
		@Min(value = 2001, message = ConstantsMessage.INVALID_DATE_ERROR) 
		int year, 
		
		@Range(min = 1, max = 12, message = ConstantsMessage.INVALID_DATE_ERROR)
		int month,
		
		Boolean forcedWrite
		) {

}
