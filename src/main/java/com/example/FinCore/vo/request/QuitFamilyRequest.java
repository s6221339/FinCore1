package com.example.FinCore.vo.request;

public class QuitFamilyRequest {
	
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
