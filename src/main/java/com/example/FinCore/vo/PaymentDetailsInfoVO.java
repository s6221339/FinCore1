package com.example.FinCore.vo;

import java.util.List;

public record PaymentDetailsInfoVO(
		String type,
		List<PaymentDetailVO> details
		) {

}
