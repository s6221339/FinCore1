package com.example.FinCore.vo;

import java.util.List;

public record PaymentDetailsWithBalanceInfoVO(
		BalanceInfoVO balanceInfo,
		FamilyInfoVO familyInfo,
		List<PaymentDetailsInfoVO> paymentInfo
		) {

}
