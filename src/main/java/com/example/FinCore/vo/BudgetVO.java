package com.example.FinCore.vo;

public record BudgetVO(
		int balanceId,
		int nowBalance,
		int totalBalance,
		int recurIncome,
		int recurExpenditure,
		int income,
		int expenditure
		) {

}
