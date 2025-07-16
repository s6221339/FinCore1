package com.example.FinCore.vo;

import java.util.List;

public record StatisticsPaymentTypeDetailsSummarizeVO(
		int year,
		int month,
		List<PaymentTypeDetailsInfoVO> paymentTypeInfoList
		) {

}
