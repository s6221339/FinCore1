package com.example.FinCore.vo.request;

import com.example.FinCore.constants.ConstantsMessage;

import jakarta.validation.constraints.Min;

public class OwnerResignAndAssignRequest {
	
	@Min(value = 1, message = ConstantsMessage.PARAM_FAMILY_ID_VALUE_ERROR)
	private int familyId;
	
    private String oldOwner;
    
    private String newOwner;

	public int getFamilyId() {
		return familyId;
	}

	public void setFamilyId(int familyId) {
		this.familyId = familyId;
	}

	public String getOldOwner() {
		return oldOwner;
	}

	public void setOldOwner(String oldOwner) {
		this.oldOwner = oldOwner;
	}

	public String getNewOwner() {
		return newOwner;
	}

	public void setNewOwner(String newOwner) {
		this.newOwner = newOwner;
	}
    
    
}
