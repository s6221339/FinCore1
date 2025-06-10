package com.example.FinCore.vo.response;

import java.util.List;

import com.example.FinCore.entity.Family;
import com.example.FinCore.vo.FamilyVO;

public class FamilyListResponse extends BasicResponse{

	private List<FamilyVO> familyList;

	public FamilyListResponse() {
		super();
	}
	
	public FamilyListResponse(int code, String message) {
		super(code, message);
	}
	
	public FamilyListResponse(int code, String message, List<FamilyVO> familyList) {
		super(code, message);
		this.familyList = familyList;
	}

	public List<FamilyVO> getFamilyList() {
		return familyList;
	}

	public void setFamilyList(List<FamilyVO> familyList) {
		this.familyList = familyList;
	}
	
	
}
