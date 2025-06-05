package com.example.FinCore.vo.response;

public class FamilyIdResponse extends BasicResponse{
	
	private int familyId;

	public FamilyIdResponse() {
		super();
	}

	public FamilyIdResponse(int code, String message) {
		super(code, message);
	}
	
	public FamilyIdResponse(int code, String message, int familyId) {
		super(code, message);
		this.familyId = familyId;
	}

	public int getFamilyId() {
		return familyId;
	}

	public void setFamilyId(int familyId) {
		this.familyId = familyId;
	}
	
	

}
