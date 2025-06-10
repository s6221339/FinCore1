package com.example.FinCore.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.FinCore.constants.ResponseMessages;
import com.example.FinCore.dao.BalanceDao;
import com.example.FinCore.dao.FamilyDao;
import com.example.FinCore.dao.PaymentDao;
import com.example.FinCore.dao.SavingsDao;
import com.example.FinCore.dao.UserDao;
import com.example.FinCore.entity.Payment;
import com.example.FinCore.service.itfc.BalanceService;
import com.example.FinCore.service.itfc.CreateBalanceRequest;
import com.example.FinCore.vo.BudgetVO;
import com.example.FinCore.vo.request.GetBudgetRequest;
import com.example.FinCore.vo.request.UpdateBalanceRequest;
import com.example.FinCore.vo.response.BasicResponse;
import com.example.FinCore.vo.response.BudgetResponse;

import jakarta.transaction.Transactional;

@Service
public class BalanceServiceImpl implements BalanceService 
{
	
	@Autowired
	private BalanceDao balanceDao;
	
	@Autowired
	private FamilyDao familyDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private PaymentDao paymentDao;
	
	@Autowired
	private SavingsDao savingsDao;

	@Transactional(rollbackOn = Exception.class)
	@Override
	public BasicResponse create(CreateBalanceRequest req) throws Exception
	{
		try
		{
			LocalDate today = LocalDate.now();
			if(req.familyId() == 0 && !StringUtils.hasText(req.account()))
				return new BasicResponse(ResponseMessages.BALANCE_WITH_NO_OWNER);
			
			else if(req.familyId() != 0 && StringUtils.hasText(req.account()))
				return new BasicResponse(ResponseMessages.BALANCE_WITH_MULTIPLE_OWNER);
			
			else if(req.familyId() != 0)
			{
				if(!familyDao.existsById(req.familyId()))
					return new BasicResponse(ResponseMessages.FAMLIY_NOT_FOUND);

				balanceDao.createByFamliyId(req.familyId(), req.name(), today);
			}
			else if(StringUtils.hasText(req.account()))
			{
				if(userDao.existsById(req.account()))
					return new BasicResponse(ResponseMessages.ACCOUNT_NOT_FOUND);
				
				balanceDao.createByAccount(req.account(), req.name(), LocalDate.now());
			}
			int lastedBalanceId = balanceDao.getLastedId();
			int year = today.getYear();
			int month = today.getMonthValue();
			savingsDao.create(lastedBalanceId, year, month, 0);
			return new BasicResponse(ResponseMessages.SUCCESS);
		}
		catch (Exception e) 
		{
			throw e;
		}
	}

	@Transactional(rollbackOn = Exception.class)
	@Override
	public BasicResponse update(UpdateBalanceRequest req) throws Exception
	{
		try
		{
			if(!balanceDao.existsById(req.balanceId()))
				return new BasicResponse(ResponseMessages.BALANCE_NOT_FOUND);
			
			var today = LocalDate.now();
			int year = today.getYear();
			int month = today.getMonthValue();
			balanceDao.updateName(req.balanceId(), req.name());
			savingsDao.update(req.balanceId(), year, month, req.savings());
			return new BasicResponse(ResponseMessages.SUCCESS);
		}
		catch (Exception e) 
		{
			throw e;
		}
	}

	@Transactional(rollbackOn = Exception.class)
	@Override
	public BasicResponse delete(int balanceId) throws Exception
	{
		if(!balanceDao.existsById(balanceId))
			return new BasicResponse(ResponseMessages.BALANCE_NOT_FOUND);
		
//		balanceList 永遠只有一個值
		List<Integer> balanceIdList = new ArrayList<Integer>();
		balanceIdList.add(balanceId);
		return deleteOpration(balanceIdList);
	}

	@Transactional(rollbackOn = Exception.class)
	@Override
	public BasicResponse deleteByAccount(String account) throws Exception
	{
		if(!userDao.existsById(account))
			return new BasicResponse(ResponseMessages.ACCOUNT_NOT_FOUND);
		
		List<Integer> balanceIdList = balanceDao.selectBalanceIdListByAccount(account);
		return deleteOpration(balanceIdList);
	}
	
	/**
	 * 刪除操作
	 */
	private BasicResponse deleteOpration(List<Integer> balanceIdList) throws Exception
	{
		List<Integer> paymentIdList = paymentDao.getPaymentIdListByBalanceIdList(balanceIdList);
		try
		{
			paymentDao.deleteAllById(paymentIdList);
			savingsDao.deleteByBalanceIdList(balanceIdList);
			
//			TODO：AI查詢資料也要刪除
			
			balanceDao.deleteAllById(balanceIdList);
		}
		catch(Exception e)
		{
			throw e;
		}
		return new BasicResponse(ResponseMessages.SUCCESS);
	}

	@Override
	public BudgetResponse getBudget(GetBudgetRequest req) 
	{
		List<Integer> idList = new ArrayList<>();
		idList.add(req.balanceId());
		List<Payment> paymentList = paymentDao.getPaymentListByBalanceIdList(idList);
		
//		預算餘額
		int nowBalance = 0;
//		總預算
		int totalBalance = 0;
		for(Payment payment : paymentList)
		{
			int amount = payment.getAmount();
			if(payment.isIncome())
			{
				if(payment.hasPeriod())
					totalBalance += amount;
				
				nowBalance += amount;
			}
			else
			{
				if(payment.hasPeriod())
					totalBalance -= amount;
				
				nowBalance -= amount;
			}
		}
		int savings = savingsDao.getSavingsAmount(req.balanceId(), req.year(), req.month());
		nowBalance -= savings;
		totalBalance -= savings;
		BudgetVO vo = new BudgetVO(req.balanceId(), nowBalance, totalBalance);
		return new BudgetResponse(ResponseMessages.SUCCESS, vo);
	}

}
