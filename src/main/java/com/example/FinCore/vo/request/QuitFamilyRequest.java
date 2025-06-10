package com.example.FinCore.vo.request;

import com.example.FinCore.constants.ConstantsMessage;

import jakarta.validation.constraints.Min;

public class QuitFamilyRequest {
	
	@Min(value = 1, message = ConstantsMessage.PARAM_FAMILY_ID_VALUE_ERROR)
	private int familyId;
	
    private String memberAccount;

	public int getFamilyId() {
		return familyId;
	}

	public void setFamilyId(int familyId) {
		this.familyId = familyId;
	}

	public String getMemberAccount() {
		return memberAccount;
	}

	public void setMemberAccount(String memberAccount) {
		this.memberAccount = memberAccount;
	}

    
}
