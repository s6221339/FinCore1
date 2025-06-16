package com.example.FinCore.vo.request;

import com.example.FinCore.constants.ConstantsMessage;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class AcceptInviteRequest {

	@NotBlank(message = ConstantsMessage.PARAM_ACCOUNT_ERROR)
	private String account;
	
	@Min(value = 1, message = ConstantsMessage.PARAM_FAMILY_ID_VALUE_ERROR )
	private int familyId;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public int getFamilyId() {
		return familyId;
	}

	public void setFamilyId(int familyId) {
		this.familyId = familyId;
	}
	
	
}
