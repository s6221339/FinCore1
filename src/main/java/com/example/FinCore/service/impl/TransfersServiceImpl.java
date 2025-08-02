package com.example.FinCore.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.FinCore.constants.ResponseMessages;
import com.example.FinCore.dao.BalanceDao;
import com.example.FinCore.dao.FamilyDao;
import com.example.FinCore.dao.PaymentDao;
import com.example.FinCore.dao.TransfersDao;
import com.example.FinCore.dao.UserDao;
import com.example.FinCore.entity.Balance;
import com.example.FinCore.entity.Family;
import com.example.FinCore.entity.Payment;
import com.example.FinCore.entity.Transfers;
import com.example.FinCore.entity.User;
import com.example.FinCore.service.itfc.LoginService;
import com.example.FinCore.service.itfc.TransfersService;
import com.example.FinCore.vo.TransfersVO;
import com.example.FinCore.vo.request.CreateTransfersRequest;
import com.example.FinCore.vo.response.BasicResponse;
import com.example.FinCore.vo.response.TransfersListResponse;

import jakarta.transaction.Transactional;

@Service
public class TransfersServiceImpl implements TransfersService 
{
	
	@Autowired
	private TransfersDao transfersDao;
	
	@Autowired
	private BalanceDao balanceDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private PaymentDao paymentDao;
	
	@Autowired
	private FamilyDao familyDao;
	
	@Autowired
	private LoginService loginService;

	@Override
	public BasicResponse create(CreateTransfersRequest req) throws Exception
	{
		User currentLogin = loginService.getData();
		if(currentLogin == null)
			return new BasicResponse(ResponseMessages.PLEASE_LOGIN_FIRST);
		
		Optional<User> toAccOpt = userDao.findById(req.toAccount());
		if(toAccOpt.isEmpty())
			return new BasicResponse(ResponseMessages.ACCOUNT_NOT_FOUND);
					
		List<Family> allFamilies = familyDao.findAll();
		boolean flag = false;
		for(Family f : allFamilies)
			if(f.isMember(currentLogin, toAccOpt.get()))
			{
				flag = true;
				break;
			}
		if(!flag)
			return new BasicResponse(ResponseMessages.UNABLE_TRANSFERS_TO_DIFF_FAMILY_ACCOUNT);
		
		List<Balance> fromBlcList = balanceDao.getAllBalanceByAccount(currentLogin.getAccount());
		if(!fromBlcList.stream().map(t -> t.getBalanceId()).toList().contains(req.fromBalance()))
			return new BasicResponse(ResponseMessages.BALANCE_NOT_FOUND);
		
		LocalDate today = LocalDate.now();
		Transfers transfers = new Transfers(
				0, 
				currentLogin.getAccount(), req.fromBalance(), 
				req.toAccount(), 0, 
				req.amount(), 
				req.description() == null ? "" : req.description(), 
				today, today.getYear(), today.getMonthValue(), 
				false
				);
		transfersDao.save(transfers);
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
		transfersList.stream()
		.filter(t -> t.isConfirmed())
		.toList()
		.forEach(transfers -> voList.add(transfers.toVO()));
		return new TransfersListResponse(ResponseMessages.SUCCESS, voList);
	}
	
	@Transactional(rollbackOn = Exception.class)
	public BasicResponse confirm(int transfersId, int balanceId)
	{
		User currentUser = loginService.getData();
		if(currentUser == null)
			return new BasicResponse(ResponseMessages.PLEASE_LOGIN_FIRST);
		
		Optional<Transfers> transOpt = transfersDao.findById(transfersId);
		if(transOpt.isEmpty())
			return new BasicResponse(ResponseMessages.TRANSFERS_NOT_FOUND);
		
		List<Balance> balanceList = balanceDao.getAllBalanceByAccount(currentUser.getAccount());
		if(!balanceList.stream().map(t -> t.getBalanceId()).toList().contains(balanceId))
			return new BasicResponse(ResponseMessages.FORBIDDEN);
		
		Transfers transfers = transOpt.get();
		if(transfers.isConfirmed())
			return new BasicResponse(ResponseMessages.TRANSFERS_ALREADY_SET);
		
		transfers.setToBalance(balanceId);
		transfers.setConfirmed(true);
		transfersDao.save(transfers);
		
		Payment p_in = Payment.ofTransfersIn(balanceId, transfers.getDescription(), transfers.getAmount());
		paymentDao.save(p_in);
		
		Payment p_out = Payment.ofTransfersOut(transfers.getFromBalance(), transfers.getDescription(), transfers.getAmount());
		p_out.setPaymentId(paymentDao.getLastedId() + 1);
		paymentDao.save(p_out);
		return new BasicResponse(ResponseMessages.SUCCESS);
	}
	
	public TransfersListResponse getNotConfirmTransfers()
	{
		User currentUser = loginService.getData();
		if(currentUser == null)
			return new TransfersListResponse(ResponseMessages.PLEASE_LOGIN_FIRST);

		List<TransfersVO> result = transfersDao.findAll().stream()
				.filter(t -> t.getToAccount() != null)
				.filter(t -> t.getToAccount().equals(currentUser.getAccount()) && !t.isConfirmed())
				.map(t -> t.toVO())
				.toList();
		
		return new TransfersListResponse(ResponseMessages.SUCCESS, result);
	}
	
	public BasicResponse retract(int transfersId)
	{
		return runRetractOrReject(transfersId, true);
	}
	
	public BasicResponse reject(int transfersId)
	{
		return runRetractOrReject(transfersId, false);
	}
	
	/**
	 * retract() 和 reject() 的共同方法，用 switcher 切換模式
	 * @param transfersId 帳款轉移編號
	 * @param switcher 設為 true 時執行 retract()
	 * @return 基本回應資料
	 */
	private BasicResponse runRetractOrReject(int transfersId, boolean switcher)
	{
		User currentUser = loginService.getData();
		if(currentUser == null)
			return new BasicResponse(ResponseMessages.PLEASE_LOGIN_FIRST);

		Optional<Transfers> transfersOpt = transfersDao.findById(transfersId);
		if(transfersOpt.isEmpty())
			return new BasicResponse(ResponseMessages.TRANSFERS_NOT_FOUND);
		
		Transfers transfers = transfersOpt.get();
		if(transfers.isConfirmed())
			return new BasicResponse(ResponseMessages.TRANSFERS_ALREADY_SET);
		
		if(switcher && !transfers.getFromAccount().equals(currentUser.getAccount()))
			return new BasicResponse(ResponseMessages.FORBIDDEN);
		
		if(!switcher && !transfers.getToAccount().equals(currentUser.getAccount()))
			return new BasicResponse(ResponseMessages.FORBIDDEN);
		
		transfersDao.delete(transfers);
		return new BasicResponse(ResponseMessages.SUCCESS);
	}

}
