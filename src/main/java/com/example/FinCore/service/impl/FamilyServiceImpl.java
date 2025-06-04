package com.example.FinCore.service.impl;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.FinCore.dao.FamilyDao;
import com.example.FinCore.dao.UserDao;
import com.example.FinCore.entity.Family;
import com.example.FinCore.service.itfc.FamilyService;
import com.example.FinCore.vo.request.CreateFamilyRequest;
import com.example.FinCore.vo.request.DeleteRequest;
import com.example.FinCore.vo.request.UpdateRequest;
import com.example.FinCore.vo.response.BasicResponse;

public class FamilyServiceImpl implements FamilyService{

	@Autowired
	private FamilyDao familyDao;
	
	@Autowired
	private UserDao userDao;
	
	@Override
	public BasicResponse create(CreateFamilyRequest req) {
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
