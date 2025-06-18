package com.example.FinCore.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.FinCore.constants.ResponseMessages;
import com.example.FinCore.dao.BalanceDao;
import com.example.FinCore.dao.PaymentDao;
import com.example.FinCore.dao.SavingsDao;
import com.example.FinCore.dao.UserDao;
import com.example.FinCore.entity.Payment;
import com.example.FinCore.service.itfc.PaymentService;
import com.example.FinCore.vo.BalanceWithPaymentVO;
import com.example.FinCore.vo.PaymentInfoVO;
import com.example.FinCore.vo.RecurringPeriodVO;
import com.example.FinCore.vo.request.AccountWithDateFilterRequest;
import com.example.FinCore.vo.request.CreatePaymentRequest;
import com.example.FinCore.vo.request.RecoveryPaymentRequest;
import com.example.FinCore.vo.request.UpdatePaymentRequest;
import com.example.FinCore.vo.response.BasicResponse;
import com.example.FinCore.vo.response.SearchPaymentResponse;

import jakarta.transaction.Transactional;

@Service
public class PaymentServiceImpl implements PaymentService 
{
	
	@Autowired
	private PaymentDao paymentDao; 
	
	@Autowired
	private BalanceDao balanceDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private SavingsDao savingsDao;

	@Override
	@Transactional(rollbackOn = Exception.class)
	public BasicResponse create(CreatePaymentRequest req) throws Exception
	{
		if(!balanceDao.existsById(req.balanceId()))
			return new BasicResponse(ResponseMessages.BALANCE_NOT_FOUND);
		
		var period = req.recurringPeriod();
		LocalDate today = LocalDate.now();
//		如果 req 沒有提供紀錄日期，就預設是創建的當下
		LocalDate generatedRecordDate = req.recordDate() == null ? LocalDate.now() : req.recordDate();
		var res = checkDate(generatedRecordDate, period);
		if(res != null)
			return res;
		
		int year = generatedRecordDate.getYear();
		int month = generatedRecordDate.getMonthValue();
		int day = generatedRecordDate.getDayOfMonth();
		if(savingsDao.getCountById(req.balanceId(), year, month) == 0)
			savingsDao.create(req.balanceId(), year, month, 0);
		
		paymentDao.create(
				req.balanceId(), 
				req.description(), 
				req.type(), 
				req.item(), 
				req.amount(), 
				period.year(), 
				period.month(),
				period.day(), 
				today,
				generatedRecordDate, 
				year, month, day);
		return new BasicResponse(ResponseMessages.SUCCESS);
	}

	@Override
	public BasicResponse delete(int paymentId) 
	{
		if(!paymentDao.existsById(paymentId))
			return new BasicResponse(ResponseMessages.PAYMENT_NOT_FOUND);
		
		paymentDao.updateDeleteDate(paymentId, LocalDate.now());
		return new BasicResponse(ResponseMessages.SUCCESS);
	}
	
	@Override
	public BasicResponse update(UpdatePaymentRequest req) throws Exception 
	{
		Payment entity = paymentDao.getEntity(req.paymentId());
		if(entity == null)
			return new BasicResponse(ResponseMessages.PAYMENT_NOT_FOUND);
		
		if(entity.isDeleted())
			return new BasicResponse(ResponseMessages.PAYMENT_HAS_BEEN_DELETED);
		
		var period = req.recurringPeriod();
		LocalDate recordDate = req.recordDate();
		var res = checkDate(recordDate, period);
		if(res != null)
			return res;
		
		int year = recordDate.getYear();
		int month = recordDate.getMonthValue();
		int day = recordDate.getDayOfMonth();
		int balanceId = paymentDao.getBalanceId(req.paymentId());
		if(savingsDao.getCountById(balanceId, year, month) == 0)
			savingsDao.create(balanceId, year, month, 0);
		
		paymentDao.update(
				req.paymentId(), 
				req.description(), 
				req.type(), 
				req.item(), 
				req.amount(), 
				period.year(), 
				period.month(), 
				period.day(), 
				recordDate, 
				year, month, day);
		return new BasicResponse(ResponseMessages.SUCCESS);
	}
	
	/**
	 * 檢查日期合法性，項目如下：
	 * <ol>
	 * 	<li>如果該款項是固定款項，但記帳時間在過去（包含今天）時不通過</li>
	 * 	<li>如果該款項是非固定款項，但記帳時間在未來（不包含今天）時不通過</li>
	 * </ol>
	 * @param date 
	 * @param period
	 * @return
	 */
	private BasicResponse checkDate(LocalDate date, RecurringPeriodVO period)
	{
		LocalDate today = LocalDate.now();
		if(period.hasPeriod() && !date.isAfter(today))
			return new BasicResponse(ResponseMessages.PAST_RECORD_DATE);
		
		if(!period.hasPeriod() && date.isAfter(today))
			return new BasicResponse(ResponseMessages.FUTURE_RECORD_DATE);
		
		return null;
	}

	@Override
	public SearchPaymentResponse getPaymentInfoByAccount(String account) 
	{
		if(!userDao.existsById(account))
			return new SearchPaymentResponse(ResponseMessages.ACCOUNT_NOT_FOUND);
		
		var resultList = getPaymentInfoOpration(account, -1, 0);
		
		return new SearchPaymentResponse(ResponseMessages.SUCCESS, resultList);
	}
	
	@Override
	public SearchPaymentResponse getPaymentInfoWithDateFilter(AccountWithDateFilterRequest req) 
	{
		if(!userDao.existsById(req.account()))
			return new SearchPaymentResponse(ResponseMessages.ACCOUNT_NOT_FOUND);
		
		var resultList = getPaymentInfoOpration(req.account(), req.year(), req.month());
		
		return new SearchPaymentResponse(ResponseMessages.SUCCESS, resultList);
	}
	
	private List<BalanceWithPaymentVO> getPaymentInfoOpration(String account, int year, int month)
	{
		List<Integer> balanceIdList = balanceDao.getBalanceIdListByAccount(account);
		List<Payment> paymentList = paymentDao.getPaymentListByBalanceIdList(balanceIdList);
		List<BalanceWithPaymentVO> resultList = new ArrayList<>();
		Map<Integer, List<PaymentInfoVO>> map = new HashMap<>();
		generateBalanceWithPaymentMap(map, paymentList, year, month);
		for(Entry<Integer, List<PaymentInfoVO>> entry : map.entrySet())
			resultList.add(new BalanceWithPaymentVO(entry.getKey(), entry.getValue()));
		return resultList;
	}
	
	/**
	 * 設定 BalanceWithPaymentMap，會將同一個帳戶的款項設定在一起
	 * @param map 要設定的 Map
	 * @param paymentList 款項列表
	 */
	private void generateBalanceWithPaymentMap(Map<Integer, List<PaymentInfoVO>> map, List<Payment> paymentList, int yearFilter, int monthFilter)
	{
		for(Payment payment : paymentList)
		{
//			DeleteDate 存在代表該款項已標記刪除，不進行設定
			if(payment.isDeleted())
				continue;
			
			if((yearFilter == -1 && monthFilter == 0) || !payment.isOnTime(yearFilter, monthFilter))
				continue;
			
			var period = new RecurringPeriodVO(
					payment.getRecurringPeriodYear(), 
					payment.getRecurringPeriodMonth(), 
					payment.getRecurringPeriodDay()
					);
			var paymentInfo = new PaymentInfoVO(
					payment.getPaymentId(),
					payment.getDescription(), 
					payment.getType(), 
					payment.getItem(), 
					payment.getAmount(), 
					period, 
					payment.getRecordDate()
					);
			List<PaymentInfoVO> voList = 
					map.containsKey(payment.getBalanceId()) ? map.get(payment.getBalanceId()) : new ArrayList<>();
			voList.add(paymentInfo);
			map.put(payment.getBalanceId(), voList);
		}
	}

	@Override
	public BasicResponse recovery(RecoveryPaymentRequest req) 
	{
		for(int id : req.paymentIdList())
			if(!paymentDao.existsById(id))
				return new BasicResponse(
						ResponseMessages.PAYMENT_NOT_FOUND.getCode(), 
						ResponseMessages.PAYMENT_NOT_FOUND.getMessage() + "：" + id
						);
		
		paymentDao.undoDeleted(req.paymentIdList());
		return new BasicResponse(ResponseMessages.SUCCESS);
	}

}
