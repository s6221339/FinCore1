package com.example.FinCore.vo.response;

import java.util.List;

import com.example.FinCore.constants.ResponseMessages;
import com.example.FinCore.vo.StatisticsIncomeAndOutlayWithBalanceInfoVO;

public class StatisticsIncomeAndOutlayWithBalanceInfoResponse extends BasicResponse 
{
	
	List<StatisticsIncomeAndOutlayWithBalanceInfoVO> statisticsList;

	public StatisticsIncomeAndOutlayWithBalanceInfoResponse() 
	{
		super();
	}

	public StatisticsIncomeAndOutlayWithBalanceInfoResponse(int code, String message) {
		super(code, message);
	}

	public StatisticsIncomeAndOutlayWithBalanceInfoResponse(ResponseMessages res) {
		super(res);
	}
	
	public StatisticsIncomeAndOutlayWithBalanceInfoResponse(int code, String message, List<StatisticsIncomeAndOutlayWithBalanceInfoVO> statisticsList) {
		super(code, message);
		this.statisticsList = statisticsList;
	}

	public StatisticsIncomeAndOutlayWithBalanceInfoResponse(ResponseMessages res, List<StatisticsIncomeAndOutlayWithBalanceInfoVO> statisticsList) {
		super(res);
		this.statisticsList = statisticsList;
	}

	public List<StatisticsIncomeAndOutlayWithBalanceInfoVO> getStatisticsList() {
		return statisticsList;
	}
	
}
