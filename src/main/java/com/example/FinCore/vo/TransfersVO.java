package com.example.FinCore.vo;

import java.time.LocalDate;

public record TransfersVO(
		int transfersId,
		String fromAccount,
		int fromBalanceId,
		String toAccount,
		int toBalanceId,
		int amount,
		String description,
		LocalDate createDate
		) {

}
