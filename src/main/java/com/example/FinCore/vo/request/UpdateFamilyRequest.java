package com.example.FinCore.vo.request;

import com.example.FinCore.constants.ConstantsMessage;

import jakarta.validation.constraints.Min;

public class UpdateFamilyRequest extends CreateFamilyRequest {
	
	@Min(value = 1, message = ConstantsMessage.FAMILY_ID_VALUE_ERROR)
	private int familyId;

	public int getFamilyId() {
		return familyId;
	}

	public void setFamilyId(int familyId) {
		this.familyId = familyId;
	}

	
}
