package com.example.FinCore.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import com.example.FinCore.entity.Savings;
import com.example.FinCore.service.itfc.BalanceService;
import com.example.FinCore.service.itfc.CreateBalanceRequest;
import com.example.FinCore.vo.BudgetVO;
import com.example.FinCore.vo.request.AccountWithDateFilterRequest;
import com.example.FinCore.vo.request.GetBudgetByBalanceIdRequest;
import com.example.FinCore.vo.request.UpdateBalanceRequest;
import com.example.FinCore.vo.response.BasicResponse;
import com.example.FinCore.vo.response.BudgetListResponse;
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
				if(!userDao.existsById(req.account()))
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
	public BudgetResponse getBudget(GetBudgetByBalanceIdRequest req) 
	{
		LocalDate today = LocalDate.now();
		LocalDate check = LocalDate.of(req.year(), req.month(), today.getDayOfMonth());
		if(check.isAfter(today))
			return new BudgetResponse(ResponseMessages.FUTURE_SEARCH_DATE);
		
		List<Integer> idList = new ArrayList<>();
		idList.add(req.balanceId());
		List<Payment> paymentList = paymentDao.getPaymentListByBalanceIdList(idList);
		
//		預算餘額
		int nowBalance = 0;
//		總預算
		int totalBalance = 0;
//		固定收入
		int recurIncome = 0;
//		固定支出
		int recurExpenditure = 0;
//		被動收入
		int income = 0;
//		被動支出
		int expenditure = 0;
		int year = req.year();
		int month = req.month() + 1;
		if(month > 12)
		{
			year++;
			month -= 12;
		}
		final var endDate = LocalDate.of(year, month, 1);
		for(Payment payment : paymentList)
		{
			if(payment.isDeleted() || !payment.isOnTime(req.year(), req.month()))
				continue;
			
			int amount = payment.getAmount();
			if(payment.isIncome())
			{
				if(payment.hasPeriod())
				{
					if(payment.isClose(LocalDate.now()))
						amount = amount * payment.getRecurringTimes(endDate);
					
					totalBalance += amount;
					recurIncome += amount;
				}
				else
					income += amount;
				
				nowBalance += amount;
			}
			else
			{
				if(payment.hasPeriod())
				{
					if(payment.isClose(LocalDate.now()))
						amount = amount * payment.getRecurringTimes(endDate);
					
					totalBalance -= amount;
					recurExpenditure += amount;
				}
				else
					expenditure += amount;
				
				nowBalance -= amount;
			}
		}
		try
		{
//			為了避免發生 savingsDao 因為取不到值返回null，而 Java 的 int 型態無法接收 null 的例外事件
			int savings = savingsDao.getSavingsAmount(req.balanceId(), req.year(), req.month());
			nowBalance -= savings;
			totalBalance -= savings;
		}
		catch (Exception e)
		{
			return new BudgetResponse(ResponseMessages.NULL_SAVINGS_VALUE);
		}
		BudgetVO vo = new BudgetVO(
				req.balanceId(), 
				nowBalance, 
				totalBalance, 
				recurIncome, 
				recurExpenditure, 
				income,
				expenditure
				);
		return new BudgetResponse(ResponseMessages.SUCCESS, vo);
	}

	@Override
	public BudgetListResponse getBudgetByAccount(AccountWithDateFilterRequest req) 
	{
		if(!userDao.existsById(req.account()))
			return new BudgetListResponse(ResponseMessages.ACCOUNT_NOT_FOUND);
		
		List<Integer> balanceIdList = balanceDao.selectBalanceIdListByAccount(req.account());
		if(balanceIdList.isEmpty())
			return new BudgetListResponse(ResponseMessages.BALANCE_NOT_FOUND);
		
		List<Payment> paymentList = paymentDao.getPaymentListByBalanceIdList(balanceIdList);
		List<Savings> savingsList = savingsDao.getSavingsListByBalanceIdList(balanceIdList, req.year(), req.month());
		Map<Integer, Integer> savingsMap = new HashMap<>();
		for(Savings savings : savingsList)
			savingsMap.put(savings.getBalanceId(), savings.getAmount());
		
		Map<Integer, List<Integer>> budgetMap = new HashMap<>();
//		預算餘額
		int nowBalance = 0;
//		總預算
		int totalBalance = 0;
//		固定收入
		int recurIncome = 0;
//		固定支出
		int recurExpenditure = 0;
//		被動收入
		int income = 0;
//		被動支出
		int expenditure = 0;
		int year = req.year();
		int month = req.month() + 1;
		if(month > 12)
		{
			year++;
			month -= 12;
		}
		final var endDate = LocalDate.of(year, month, 1);
		for(Payment payment : paymentList)
		{
			if(payment.isDeleted() || !payment.isOnTime(req.year(), req.month()))
				continue;
			
			int balanceId = payment.getBalanceId();
			if(budgetMap.containsKey(balanceId))
			{
				List<Integer> tempList = budgetMap.get(balanceId);
				nowBalance = tempList.get(0);
				totalBalance = tempList.get(1);
				recurIncome = tempList.get(2);
				recurExpenditure = tempList.get(3);
				income = tempList.get(4);
				expenditure = tempList.get(5);
			}
			else
			{
				nowBalance = 0;
				totalBalance = 0;
				recurIncome = 0;
				recurExpenditure = 0;
				income = 0;
				expenditure = 0;
			}
			int amount = payment.getAmount();
			if(payment.isIncome())
			{
				if(payment.hasPeriod())
				{
					if(payment.isClose(LocalDate.now()))
						amount = amount * payment.getRecurringTimes(endDate);
					
					totalBalance += amount;
					recurIncome += amount;
				}
				else
					income += amount;
				
				nowBalance += amount;
			}
			else
			{
				if(payment.hasPeriod())
				{
					if(payment.isClose(LocalDate.now()))
						amount = amount * payment.getRecurringTimes(endDate);
					
					totalBalance -= amount;
					recurExpenditure += amount;
				}
				else
					expenditure += amount;
				
				nowBalance -= amount;
			}
			List<Integer> balanceValueList = new ArrayList<>();
			balanceValueList.add(nowBalance);
			balanceValueList.add(totalBalance);
			balanceValueList.add(recurIncome);
			balanceValueList.add(recurExpenditure);
			balanceValueList.add(income);
			balanceValueList.add(expenditure);
			budgetMap.put(balanceId, balanceValueList);
		}
		
		List<BudgetVO> voList = new ArrayList<>();
		for(Entry<Integer, List<Integer>> entry : budgetMap.entrySet())
		{
			List<Integer> balanceValueList = entry.getValue();
			int savings = savingsMap.get(entry.getKey()) == null ? 0 : savingsMap.get(entry.getKey());
			nowBalance = balanceValueList.get(0) - savings;
			totalBalance = balanceValueList.get(1) - savings;
			recurIncome = balanceValueList.get(2);
			recurExpenditure = balanceValueList.get(3);
			income = balanceValueList.get(4);
			expenditure = balanceValueList.get(5);
			BudgetVO vo = new BudgetVO(
					entry.getKey(), 
					nowBalance, 
					totalBalance,
					recurIncome,
					recurExpenditure,
					income,
					expenditure
					);
			voList.add(vo);
		}
		return new BudgetListResponse(ResponseMessages.SUCCESS, voList);
	}
	
	
}
