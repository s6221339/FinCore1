package com.example.FinCore.vo;

import java.util.List;

/**
 * 該值對象儲存一個帳戶底下的所有款項資料。
 */
public record BalanceWithPaymentVO(
		int balanceId,
		List<PaymentInfoVO> paymentInfoList
		) 
{
	
}
