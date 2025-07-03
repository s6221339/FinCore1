package com.example.FinCore.vo;

import java.util.List;

public record StatisticsPaymentTypeVO(
		int year,
		int month,
		List<PaymentAmountVO> paymentInfo
		) {

}
