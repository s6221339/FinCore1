package com.example.FinCore.vo;

public record IncomeAndOutlayWithBalanceInfoVO(
		BalanceInfoVO balanceInfo,
		FamilyInfoVO familyInfo,
		int income,
		int outlay
		) {

}
