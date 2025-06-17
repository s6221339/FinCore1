package com.example.FinCore.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.FinCore.constants.ResponseMessages;
import com.example.FinCore.dao.BalanceDao;
import com.example.FinCore.dao.SavingsDao;
import com.example.FinCore.dao.UserDao;
import com.example.FinCore.entity.Savings;
import com.example.FinCore.service.itfc.SavingsService;
import com.example.FinCore.vo.response.SavingsListResponse;

@Service
public class SavingsServiceImpl implements SavingsService 
{
	
	@Autowired
	private SavingsDao savingsDao;
	
	@Autowired
	private BalanceDao balanceDao;
	
	@Autowired
	private UserDao userDao;
	
	@Override
	public SavingsListResponse getAll(String account) 
	{
		if(!userDao.existsById(account))
			return new SavingsListResponse(ResponseMessages.ACCOUNT_NOT_FOUND);
		
		List<Integer> balanceIdList = balanceDao.getBalanceIdListByAccount(account);
		List<Savings> savingsList = savingsDao.getAllByBalanceIdList(balanceIdList);
		return new SavingsListResponse(ResponseMessages.SUCCESS, savingsList);
	}

}
