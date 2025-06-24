package com.example.FinCore.vo.response;

import java.util.List;

import com.example.FinCore.constants.ResponseMessages;
import com.example.FinCore.vo.StatisticsVO;

public class StatisticsResponse extends BasicResponse
{

	List<StatisticsVO> statisticsList;

	public StatisticsResponse() {
		super();
	}

	public StatisticsResponse(int code, String message) {
		super(code, message);
	}

	public StatisticsResponse(ResponseMessages res) {
		super(res);
	}
	
	public StatisticsResponse(int code, String message, List<StatisticsVO> statisticsList) {
		super(code, message);
		this.statisticsList = statisticsList;
	}

	public StatisticsResponse(ResponseMessages res, List<StatisticsVO> statisticsList) {
		super(res);
		this.statisticsList = statisticsList;
	}

	public List<StatisticsVO> getStatisticsList() {
		return statisticsList;
	}
	
}
