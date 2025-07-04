package com.example.FinCore.vo;

import java.util.List;

public record StatisticsIncomeAndOutlayWithBalanceInfoVO(
		int year,
		int month,
		List<IncomeAndOutlayWithBalanceInfoVO> incomeAndOutlayInfoVOList
		) {

}
