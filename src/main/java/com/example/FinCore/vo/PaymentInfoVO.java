package com.example.FinCore.vo;

import java.time.LocalDate;

/**
 * 紀載款項的所有基本資料
 */
public record PaymentInfoVO(
		int paymentId,
		String description, 
		String type, 
		String item, 
		int amount, 
		RecurringPeriodVO recurringPeriod, 
		LocalDate recordDate,
		int lifeTime
		) 
{
	
}
