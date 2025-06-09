package com.example.FinCore.service.impl;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.FinCore.constants.ResponseMessages;
import com.example.FinCore.dao.BalanceDao;
import com.example.FinCore.dao.FamilyDao;
import com.example.FinCore.dao.UserDao;
import com.example.FinCore.service.itfc.BalanceService;
import com.example.FinCore.service.itfc.CreateBalanceRequest;
import com.example.FinCore.vo.request.UpdateBalanceRequest;
import com.example.FinCore.vo.response.BasicResponse;

@Service
public class BalanceServiceImpl implements BalanceService 
{
	
	@Autowired
	private BalanceDao balanceDao;
	
	@Autowired
	private FamilyDao familyDao;
	
	@Autowired
	private UserDao userDao;

	@Override
	public BasicResponse create(CreateBalanceRequest req) 
	{
		
		if(req.familyId() == 0 && !StringUtils.hasText(req.account()))
			return new BasicResponse(ResponseMessages.BALANCE_WITH_NO_OWNER);
		
		else if(req.familyId() != 0 && StringUtils.hasText(req.account()))
			return new BasicResponse(ResponseMessages.BALANCE_WITH_MULTIPLE_OWNER);
		
		else if(req.familyId() != 0)
		{
			if(!familyDao.existsById(req.familyId()))
				return new BasicResponse(ResponseMessages.FAMLIY_NOT_FOUND);
			
			balanceDao.createByFamliyId(req.familyId(), req.name(), LocalDate.now());
		}
		else if(StringUtils.hasText(req.account()))
		{
			if(userDao.existsById(req.account()))
				return new BasicResponse(ResponseMessages.ACCOUNT_NOT_FOUND);
			
			balanceDao.createByAccount(req.account(), req.name(), LocalDate.now());
		}
		return new BasicResponse(ResponseMessages.SUCCESS);
	}

	@Override
	public BasicResponse update(UpdateBalanceRequest req) 
	{
		if(!balanceDao.existsById(req.balanceId()))
			return new BasicResponse(ResponseMessages.BALANCE_NOT_FOUND);
		
		balanceDao.updateName(req.balanceId(), req.name());
		return new BasicResponse(ResponseMessages.SUCCESS);
	}

}
