package com.example.FinCore.vo;

import java.time.LocalDate;

public record TransfersVO(
		int transfersId,
		int fromBalanceId,
		int toBalanceId,
		int amount,
		String description,
		LocalDate createDate
		) {

}
