package com.example.FinCore.vo;

import java.util.List;

public record PaymentTypeDetailsInfoVO(
		String type,
		List<PaymentItemAmountDetailVO> amountDetailList
		) {

}
