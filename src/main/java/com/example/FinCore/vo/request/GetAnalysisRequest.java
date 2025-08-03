package com.example.FinCore.vo.request;

import com.example.FinCore.constants.ConstantsMessage;
import com.example.FinCore.vo.DateVO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record GetAnalysisRequest(
		@NotNull(message = ConstantsMessage.INVALID_DATE_ERROR)
		@Valid
		DateVO from,
		
		@NotNull(message = ConstantsMessage.INVALID_DATE_ERROR)
		@Valid
		DateVO to
		) {

}
