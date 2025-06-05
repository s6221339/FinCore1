package com.example.FinCore.service.impl;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.FinCore.constants.ResponseMessages;
import com.example.FinCore.dao.BalanceDao;
import com.example.FinCore.dao.PaymentDao;
import com.example.FinCore.service.itfc.PaymentService;
import com.example.FinCore.vo.request.CreatePaymentRequest;
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
		
		LocalDate today = LocalDate.now();
//		如果 req 沒有提供紀錄日期，就預設是創建的當下
		LocalDate generatedRecordDate = req.recordDate() == null ? today : req.recordDate();
//		如果該款項是固定款項，但記帳時間在過去時不通過
		if(req.recurringPeriod() != 0 && generatedRecordDate.isBefore(today))
			return new BasicResponse(ResponseMessages.PAST_RECORD_DATE);
		
//		如果該款項是非固定款項，但記帳時間在未來時不通過
		if(req.recurringPeriod() == 0 && generatedRecordDate.isAfter(today))
			return new BasicResponse(ResponseMessages.FUTURE_RECORD_DATE);
		
		int year = generatedRecordDate.getYear();
		int month = generatedRecordDate.getMonthValue();
		int day = generatedRecordDate.getDayOfMonth();
		
		paymentDao.create(
				req.balanceId(), 
				req.description(), 
				req.type(), 
				req.item(), 
				req.amount(), 
				req.recurringPeriod(), 
				today,
				generatedRecordDate, 
				year, 
				month, 
				day);
		return new BasicResponse(ResponseMessages.SUCCESS);
	}

}
