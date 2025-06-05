package com.example.FinCore.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.FinCore.dao.FamilyDao;
import com.example.FinCore.dao.UserDao;
import com.example.FinCore.service.itfc.UserService;
import com.example.FinCore.vo.response.BasicResponse;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private FamilyDao familyDao;

	@Autowired
	private UserDao userDao; 

	@Override
	public BasicResponse create() {
		
		return null;
	}

	@Override
	public BasicResponse update() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BasicResponse delete() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
