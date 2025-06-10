package com.example.FinCore.vo.response;

import com.example.FinCore.constants.ResponseMessages;
import com.example.FinCore.entity.Family;

public class FamilyIdResponse extends BasicResponse{
	
	private Family family;

	public FamilyIdResponse() {
		super();
	}

	public FamilyIdResponse(Family family) {
		super();
		this.family = family;
	}

	public FamilyIdResponse(int code, String message) {
		super(code, message);
	}
	
	public FamilyIdResponse(int code, String message, Family family) {
		super(code, message);
		this.family = family;
	}

	public FamilyIdResponse(ResponseMessages res) {
		super(res);
	}

	public FamilyIdResponse(ResponseMessages res, Family family) {
		super(res);
		this.family = family;
	}

	public Family getFamily() {
		return family;
	}

	public void setFamily(Family family) {
		this.family = family;
	}
	
}
