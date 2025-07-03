package com.example.FinCore.vo.response;

import java.util.List;

import com.example.FinCore.constants.ResponseMessages;
import com.example.FinCore.vo.StatisticsVO;

public class StatisticsLookupAllBalanceResponse extends BasicResponse
{

	List<StatisticsVO> statisticsList;

	public StatisticsLookupAllBalanceResponse() {
		super();
	}

	public StatisticsLookupAllBalanceResponse(int code, String message) {
		super(code, message);
	}

	public StatisticsLookupAllBalanceResponse(ResponseMessages res) {
		super(res);
	}
	
	public StatisticsLookupAllBalanceResponse(int code, String message, List<StatisticsVO> statisticsList) {
		super(code, message);
		this.statisticsList = statisticsList;
	}

	public StatisticsLookupAllBalanceResponse(ResponseMessages res, List<StatisticsVO> statisticsList) {
		super(res);
		this.statisticsList = statisticsList;
	}

	public List<StatisticsVO> getStatisticsList() {
		return statisticsList;
	}
	
}
