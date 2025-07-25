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
import com.example.FinCore.entity.Balance;
import com.example.FinCore.entity.Family;
import com.example.FinCore.entity.Payment;
import com.example.FinCore.entity.Savings;
import com.example.FinCore.entity.SavingsPK;
import com.example.FinCore.service.itfc.BalanceService;
import com.example.FinCore.vo.BudgetVO;
import com.example.FinCore.vo.request.AccountWithDateFilterRequest;
import com.example.FinCore.vo.request.CreateBalanceRequest;
import com.example.FinCore.vo.request.GetBudgetByBalanceIdRequest;
import com.example.FinCore.vo.request.UpdateBalanceRequest;
import com.example.FinCore.vo.response.BalanceListResponse;
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
//			如果有名稱資料則更新
			if(StringUtils.hasText(req.name()))
				balanceDao.updateName(req.balanceId(), req.name());
//			如果有儲蓄資料則更新
			if(req.savings() >= 0)
			{
				SavingsPK pk = new SavingsPK(req.balanceId(), year, month);
				if(savingsDao.existsById(pk))
					savingsDao.update(req.balanceId(), year, month, req.savings());
				
				else
					savingsDao.create(req.balanceId(), year, month, req.savings());	
			}
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

	/**
	 * 刪除指定帳號底下的所有帳戶。<br>
	 * ⚠️：該操作會將帳戶下的所有關聯資料一併清除，不可復原。
	 * @param account 指定帳號
	 * @return 基本回應資料
	 */
//	考量該操作不應該被單獨呼叫而應作為附帶操作，關閉此API
	@Transactional(rollbackOn = Exception.class)
	public BasicResponse deleteByAccount(String account) throws Exception
	{
		if(!userDao.existsById(account))
			return new BasicResponse(ResponseMessages.ACCOUNT_NOT_FOUND);
		
		List<Integer> balanceIdList = balanceDao.getBalanceIdListByAccount(account);
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
					if(payment.isCloseDate(LocalDate.now()))
						amount = amount * payment.getRecurringCount(endDate);
					
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
					if(payment.isCloseDate(LocalDate.now()))
						amount = amount * payment.getRecurringCount(endDate);
					
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
		
		List<Integer> balanceIdList = balanceDao.getBalanceIdListByAccount(req.account());
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
					if(payment.isCloseDate(LocalDate.now()))
						amount = amount * payment.getRecurringCount(endDate);
					
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
					if(payment.isCloseDate(LocalDate.now()))
						amount = amount * payment.getRecurringCount(endDate);
					
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

	@Override
	public BalanceListResponse getPersonalBalance(String account) 
	{
		if(!userDao.existsById(account))
			return new BalanceListResponse(ResponseMessages.ACCOUNT_NOT_FOUND);
		
		List<Balance> balanceList = balanceDao.getAllBalanceByAccount(account);
		return new BalanceListResponse(ResponseMessages.SUCCESS, balanceList);
	}
	
	@Override
	public BalanceListResponse getFamilyBalance(String account) 
	{
		if(!userDao.existsById(account))
			return new BalanceListResponse(ResponseMessages.ACCOUNT_NOT_FOUND);
		
		var familyIdList = getFamilyContainsAccount(account);
		List<Balance> balanceList = balanceDao.getAllBalanceByFamilyIdList(familyIdList);
		return new BalanceListResponse(ResponseMessages.SUCCESS, balanceList);
	}

	/**
	 * 取得指定帳號所在的群組編號。
	 * @param account 帳號
	 * @return 該帳號所在的群組編號，是一個列表
	 */
	private List<Integer> getFamilyContainsAccount(String account)
	{
		List<Family> familyList = familyDao.selectAll();
		List<Integer> idList = new ArrayList<>();
		var copy = new ArrayList<>(familyList);
//		過濾不需要的群組，僅留下存在該帳號的群組
		copy.forEach(family -> {
//			標記是否找到目標
			boolean flag = false;
//			檢查群組持有者是否為指定帳號
			if(family.getOwner().equals(account))
				flag = true;
			
//			檢查群組成員名單是否包含指定帳號
			if(!flag)
				if(family.toMemberList().contains(account))
					flag = true;
//			如果最後結果找不到則移除該群組
			if(!flag)
				familyList.remove(family);
		});
		familyList.forEach(family -> {
			idList.add(family.getId());
		});
		return idList;
	}
	
	
}
