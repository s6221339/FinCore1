package com.example.FinCore.vo.response;

import java.util.List;

import com.example.FinCore.constants.ResponseMessages;
import com.example.FinCore.entity.Savings;

public class SavingsListResponse extends BasicResponse
{
	
	private List<Savings> savingsList;

	public SavingsListResponse() {
		super();
	}

	public SavingsListResponse(int code, String message) {
		super(code, message);
	}
	
	public SavingsListResponse(int code, String message, List<Savings> savingsList) {
		super(code, message);
		this.savingsList = savingsList;
	}

	public SavingsListResponse(ResponseMessages res) {
		super(res);
	}
	
	public SavingsListResponse(ResponseMessages res, List<Savings> savingsList) {
		super(res);
		this.savingsList = savingsList;
	}

	public List<Savings> getSavingsList() {
		return savingsList;
	}
	
}
