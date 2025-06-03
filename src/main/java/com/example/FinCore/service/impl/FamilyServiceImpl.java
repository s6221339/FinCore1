package com.example.FinCore.service.impl;

import java.time.LocalDate;

import com.example.FinCore.service.itfc.FamilyService;
import com.example.FinCore.vo.request.CreateFamilyRequest;
import com.example.FinCore.vo.request.DeleteRequest;
import com.example.FinCore.vo.request.UpdateRequest;
import com.example.FinCore.vo.response.BasicResponse;

public class FamilyServiceImpl implements FamilyService{

	
	@Override
	public BasicResponse create(CreateFamilyRequest req) {
		//檢查群組成員有沒有重複
		//
		LocalDate.now();
		return null;
	}

	@Override
	public BasicResponse update(UpdateRequest req) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BasicResponse delete(DeleteRequest req) {
		// TODO Auto-generated method stub
		return null;
	}

}
