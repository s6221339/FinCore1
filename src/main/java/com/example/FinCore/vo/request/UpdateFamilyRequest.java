package com.example.FinCore.vo.request;

import com.example.FinCore.constants.ConstantsMessage;

import jakarta.validation.constraints.Min;

public record UpdateFamilyRequest(
		
		@Min(value = 1, message = ConstantsMessage.PARAM_FAMILY_ID_ERROR)
		int familyId, 
		
		String name) {
	
}
