package com.example.FinCore.service.impl;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.FinCore.constants.ResponseMessages;
import com.example.FinCore.dao.BalanceDao;
import com.example.FinCore.dao.PaymentDao;
import com.example.FinCore.entity.Payment;
import com.example.FinCore.service.itfc.PaymentService;
import com.example.FinCore.vo.RecurringPeriodVO;
import com.example.FinCore.vo.request.CreatePaymentRequest;
import com.example.FinCore.vo.request.UpdatePaymentRequest;
import com.example.FinCore.vo.response.BasicResponse;

@Service
public class PaymentServiceImpl implements PaymentService 
{
	
	@Autowired
	private PaymentDao paymentDao; 
	
	@Autowired
	private BalanceDao balanceDao;

	@Override
	public BasicResponse create(CreatePaymentRequest req) 
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
	public BasicResponse update(UpdatePaymentRequest req) 
	{
		Payment entity = paymentDao.getEntity(req.paymentId());
		if(entity == null)
			return new BasicResponse(ResponseMessages.PAYMENT_NOT_FOUND);
		
		if(entity.getDeleteDate() != null)
			return new BasicResponse(ResponseMessages.PAYMENT_HAS_BEEN_DELETED);
		
		var period = req.recurringPeriod();
		LocalDate recordDate = req.recordDate();
		var res = checkDate(recordDate, period);
		if(res != null)
			return res;
		
		int year = recordDate.getYear();
		int month = recordDate.getMonthValue();
		int day = recordDate.getDayOfMonth();
		
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
	 * 	<li>如果該款項是固定款項，但記帳時間在過去時不通過</li>
	 * 	<li>如果該款項是非固定款項，但記帳時間在未來時不通過</li>
	 * </ol>
	 * @param date
	 * @param period
	 * @return
	 */
	private BasicResponse checkDate(LocalDate date, RecurringPeriodVO period)
	{
		LocalDate today = LocalDate.now();
		if(period.hasPeriod() && date.isBefore(today))
			return new BasicResponse(ResponseMessages.PAST_RECORD_DATE);
		
		if(!period.hasPeriod() && date.isAfter(today))
			return new BasicResponse(ResponseMessages.FUTURE_RECORD_DATE);
		
		return null;
	}

}
