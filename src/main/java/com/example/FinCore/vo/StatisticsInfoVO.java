package com.example.FinCore.vo;

import java.util.List;

/**
 * 每個 InfoVO 囊括了其帳戶資料與群組資料（如果來自群組），以及最重要的
 * 款項統計資料列表
 */
public record StatisticsInfoVO(
		BalanceInfoVO balanceInfo,
		FamilyInfoVO familyInfo,
		List<PaymentAmountVO> paymentAmountList
		) {

}
