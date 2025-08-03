package com.example.FinCore.vo;

import org.hibernate.validator.constraints.Range;

import com.example.FinCore.constants.ConstantsMessage;

import jakarta.validation.constraints.Min;

public record DateVO(
		@Min(value = 2001, message = ConstantsMessage.INVALID_DATE_ERROR)
		Integer year,
		
		@Range(max = 12, min = 0, message = ConstantsMessage.INVALID_DATE_ERROR)
		Integer month,
		
		@Min(value = 1, message = ConstantsMessage.INVALID_DATE_ERROR)
		Integer day
		) {

}
