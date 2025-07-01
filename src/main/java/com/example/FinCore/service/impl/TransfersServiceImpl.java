package com.example.FinCore.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
import com.example.FinCore.entity.User;
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

	@Transactional(rollbackOn = Exception.class)
	@Override
	public BasicResponse create(CreateTransfersRequest req) throws Exception
	{
		if(req.fromBalance() == req.toBalance())
			return new BasicResponse(ResponseMessages.SAME_BALANCE_OPERATION);
		
		Balance from = balanceDao.getReferenceById(req.fromBalance());
		Balance to = balanceDao.getReferenceById(req.toBalance());
		if(from == null || to == null)
			return new BasicResponse(ResponseMessages.BALANCE_NOT_FOUND);
		
		if(from.belongToFamily() || to.belongToFamily())
			return new BasicResponse(ResponseMessages.UNABLE_TRANSFERS_TO_FAMILY_BALANCE);
		
		if(from.belongToAccount() && to.belongToAccount() && from.getAccount() != to.getAccount())
		{
			List<Family> allFamilyList = familyDao.findAll();
			/* 用來確認是否找到同一個群組的旗標 */
			boolean flag = false;
			for(Family f : allFamilyList)
				if(f.isMember(from.getAccount(), to.getAccount()))
				{
					flag = true;
					break;
				}
			
			if(!flag)
				return new BasicResponse(ResponseMessages.UNABLE_TRANSFERS_TO_DIFF_FAMILY_ACCOUNT);
		}
		
		LocalDate today = LocalDate.now();
		transfersDao.create(
				req.fromBalance(), 
				req.toBalance(), 
				req.amount(), 
				req.description(), 
				today, today.getYear(), today.getMonthValue()
				);
		
		Payment p_in = Payment.ofTransfersIn(req.toBalance(), req.description(), req.amount());
		paymentDao.save(p_in);
		
		Payment p_out = Payment.ofTransfersOut(req.fromBalance(), req.description(), req.amount());
//		必須要手動設定 PaymentId，否則會併發「NonUniqueObjectException」
//		參照：https://www.cnblogs.com/xiaotiaosi/p/6489573.html
		p_out.setPaymentId(paymentDao.getLastedId() + 1);
		paymentDao.save(p_out);
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
