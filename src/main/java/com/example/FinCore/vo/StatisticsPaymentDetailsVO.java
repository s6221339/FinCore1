package com.example.FinCore.vo;

import java.util.List;

public record StatisticsPaymentDetailsVO(
		int year,
		int month,
		List<PaymentDetailsWithBalanceInfoVO> infoList
		) {

}
