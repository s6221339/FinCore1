package com.example.FinCore.vo.response;

import java.util.List;

import com.example.FinCore.constants.ResponseMessages;
import com.example.FinCore.vo.StatisticsPaymentDetailsVO;

public class StatisticsPaymentDetailsWithBalanceResponse extends BasicResponse
{
	
	private List<StatisticsPaymentDetailsVO> statisticsList;

	public StatisticsPaymentDetailsWithBalanceResponse() {
		super();
	}

	public StatisticsPaymentDetailsWithBalanceResponse(int code, String message) {
		super(code, message);
	}

	public StatisticsPaymentDetailsWithBalanceResponse(ResponseMessages res) {
		super(res);
	}
	
	public StatisticsPaymentDetailsWithBalanceResponse(int code, String message, List<StatisticsPaymentDetailsVO> statisticsList) {
		super(code, message);
		this.statisticsList = statisticsList;
	}

	public StatisticsPaymentDetailsWithBalanceResponse(ResponseMessages res, List<StatisticsPaymentDetailsVO> statisticsList) {
		super(res);
		this.statisticsList = statisticsList;
	}

	public List<StatisticsPaymentDetailsVO> getStatisticsList() {
		return statisticsList;
	}

}
