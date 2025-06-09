package com.example.FinCore.vo.response;

import java.util.List;

import com.example.FinCore.entity.Family;

public class FamilyListResponse extends BasicResponse{

	private List<Family> familyList;

	public FamilyListResponse() {
		super();
	}
	
	public FamilyListResponse(int code, String message) {
		super(code, message);
	}
	
	public FamilyListResponse(int code, String message, List<Family> familyList) {
		super(code, message);
		this.familyList = familyList;
	}

	public List<Family> getFamilyList() {
		return familyList;
	}

	public void setFamilyList(List<Family> familyList) {
		this.familyList = familyList;
	}
	
	
}
