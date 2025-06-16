package com.example.FinCore.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.FinCore.constants.ResponseMessages;
import com.example.FinCore.dao.BalanceDao;
import com.example.FinCore.dao.TransfersDao;
import com.example.FinCore.dao.UserDao;
import com.example.FinCore.entity.User;
import com.example.FinCore.service.itfc.TransfersService;
import com.example.FinCore.vo.TransfersVO;
import com.example.FinCore.vo.request.CreateTransfersRequest;
import com.example.FinCore.vo.response.BasicResponse;
import com.example.FinCore.vo.response.TransfersListResponse;

@Service
public class TransfersServiceImpl implements TransfersService 
{
	
	@Autowired
	private TransfersDao transfersDao;
	
	@Autowired
	private BalanceDao balanceDao;
	
	@Autowired
	private UserDao userDao;

	@Override
	public BasicResponse create(CreateTransfersRequest req) 
	{
		if(req.fromBalance() == req.toBalance())
			return new BasicResponse(ResponseMessages.SAME_BALANCE_OPERATION);
		
		if(!balanceDao.existsById(req.fromBalance()) || !balanceDao.existsById(req.toBalance()))
			return new BasicResponse(ResponseMessages.BALANCE_NOT_FOUND);
		
		LocalDate today = LocalDate.now();
		transfersDao.create(
				req.fromBalance(), 
				req.toBalance(), 
				req.amount(), 
				req.description(), 
				today, today.getYear(), today.getMonthValue()
				);
		return new BasicResponse(ResponseMessages.SUCCESS);
	}

	@Override
	public BasicResponse delete(String account, int id) 
	{
		if(!transfersDao.existsById(id))
			return new BasicResponse(ResponseMessages.TRANSFERS_NOT_FOUND);
		
		User user = userDao.selectById(account);
		if(user == null)
			return new BasicResponse(ResponseMessages.ACCOUNT_NOT_FOUND);
		
		if(!user.isSuperAdmin())
			return new BasicResponse(ResponseMessages.NO_PERMISSION);
		
		transfersDao.deleteById(id);
		return new BasicResponse(ResponseMessages.SUCCESS);
	}

	@Override
	public BasicResponse deleteByBalanceId(int from, int to) 
	{
		if(from == to)
			return new BasicResponse(ResponseMessages.SAME_BALANCE_OPERATION);
		
		if(balanceDao.existsById(from) || balanceDao.existsById(to))
			return new BasicResponse(ResponseMessages.BALANCE_ACTIVATION);
		
		transfersDao.deleteByBalanceId(from, to);
		return new BasicResponse(ResponseMessages.SUCCESS);
	}

	@Override
	public TransfersListResponse getAllByBalanceId(int balanceId) 
	{
		if(!balanceDao.existsById(balanceId))
			return new TransfersListResponse(ResponseMessages.BALANCE_NOT_FOUND);
		
		var transfersList = transfersDao.getAllTransfersByBalanceId(balanceId);
		List<TransfersVO> voList = new ArrayList<>();
		transfersList.forEach(transfers -> {
			voList.add(new TransfersVO(
					transfers.getId(), 
					transfers.getFromBalance(), 
					transfers.getToBalance(), 
					transfers.getAmount(), 
					transfers.getDescription(), 
					transfers.getCreateDate()
					));
		});
		return new TransfersListResponse(ResponseMessages.SUCCESS, voList);
	}

}
