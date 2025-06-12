package com.example.FinCore.vo;

public record BudgetVO(
		int balanceId,
		int settlement,
		int budget,
		int recurIncome,
		int recurExpenditure,
		int income,
		int expenditure
		) {

}
