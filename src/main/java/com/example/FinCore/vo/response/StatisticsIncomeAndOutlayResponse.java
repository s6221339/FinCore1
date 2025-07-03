package com.example.FinCore.vo.response;

import java.util.List;

import com.example.FinCore.constants.ResponseMessages;
import com.example.FinCore.vo.StatisticsIncomeAndOutlayVO;

public class StatisticsIncomeAndOutlayResponse extends BasicResponse 
{
	
	private List<StatisticsIncomeAndOutlayVO> statisticsList;

	public StatisticsIncomeAndOutlayResponse() {
		super();
	}

	public StatisticsIncomeAndOutlayResponse(int code, String message) {
		super(code, message);
	}

	public StatisticsIncomeAndOutlayResponse(ResponseMessages res) {
		super(res);
	}
	
	public StatisticsIncomeAndOutlayResponse(int code, String message, List<StatisticsIncomeAndOutlayVO> voList) {
		super(code, message);
		this.statisticsList = voList;
	}

	public StatisticsIncomeAndOutlayResponse(ResponseMessages res, List<StatisticsIncomeAndOutlayVO> voList) {
		super(res);
		this.statisticsList = voList;
	}

	public List<StatisticsIncomeAndOutlayVO> getStatisticsList() {
		return statisticsList;
	}
	
}
