package com.example.FinCore.vo.response;

import java.util.List;

import com.example.FinCore.constants.ResponseMessages;
import com.example.FinCore.vo.FamilyAvatarVO;

public class FamilyAvatarListResponse extends BasicResponse {

	private List<FamilyAvatarVO> familyList;

	public FamilyAvatarListResponse() {
		super();
	}
	
	public FamilyAvatarListResponse(int code, String message) {
		super(code, message);
	}
	
	public FamilyAvatarListResponse(int code, String message, List<FamilyAvatarVO> familyList) {
		super(code, message);
		this.familyList = familyList;
	}

	public FamilyAvatarListResponse(ResponseMessages res) {
		super(res);
	}
	
	public FamilyAvatarListResponse(ResponseMessages res, List<FamilyAvatarVO> familyList) {
		super(res);
		this.familyList = familyList;
	}


	public List<FamilyAvatarVO> getFamilyList() {
		return familyList;
	}

	public void setFamilyList(List<FamilyAvatarVO> familyList) {
		this.familyList = familyList;
	}
	
	
}