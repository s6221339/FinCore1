package com.example.FinCore.vo.request;

import java.util.List;

import com.example.FinCore.constants.ConstantsMessage;

import jakarta.validation.constraints.NotEmpty;

public record RecoveryPaymentRequest(
		@NotEmpty(message = ConstantsMessage.EMPTY_COLLECTION_ERROR)
		List<Integer> paymentIdList
		) {

}
