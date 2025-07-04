package com.example.FinCore.vo.response;

import java.util.List;

import com.example.FinCore.constants.ResponseMessages;
import com.example.FinCore.vo.StatisticsVO;

public class StatisticsLookupPaymentTypeWithAllBalanceResponse extends BasicResponse
{

	List<StatisticsVO> statisticsList;

	public StatisticsLookupPaymentTypeWithAllBalanceResponse() {
		super();
	}

	public StatisticsLookupPaymentTypeWithAllBalanceResponse(int code, String message) {
		super(code, message);
	}

	public StatisticsLookupPaymentTypeWithAllBalanceResponse(ResponseMessages res) {
		super(res);
	}
	
	public StatisticsLookupPaymentTypeWithAllBalanceResponse(int code, String message, List<StatisticsVO> statisticsList) {
		super(code, message);
		this.statisticsList = statisticsList;
	}

	public StatisticsLookupPaymentTypeWithAllBalanceResponse(ResponseMessages res, List<StatisticsVO> statisticsList) {
		super(res);
		this.statisticsList = statisticsList;
	}

	public List<StatisticsVO> getStatisticsList() {
		return statisticsList;
	}
	
}
