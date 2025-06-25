package com.example.FinCore.vo.request;

import java.util.List;

import com.example.FinCore.constants.ConstantsMessage;

import jakarta.validation.constraints.Min;

public record KickMemberRequest(
		
		@Min(value = 1, message = ConstantsMessage.PARAM_FAMILY_ID_VALUE_ERROR)
		int familyId, 
		
		String owner, 
		
		List<String> memberAccounts) {
	
}
