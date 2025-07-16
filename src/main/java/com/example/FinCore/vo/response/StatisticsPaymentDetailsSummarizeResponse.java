package com.example.FinCore.vo.response;

import java.util.List;

import com.example.FinCore.constants.ResponseMessages;
import com.example.FinCore.vo.StatisticsPaymentTypeDetailsSummarizeVO;

public class StatisticsPaymentDetailsSummarizeResponse extends BasicResponse 
{
	
	private List<StatisticsPaymentTypeDetailsSummarizeVO> statisticsList;

	public StatisticsPaymentDetailsSummarizeResponse() {
		super();
	}

	public StatisticsPaymentDetailsSummarizeResponse(int code, String message) {
		super(code, message);
	}

	public StatisticsPaymentDetailsSummarizeResponse(ResponseMessages res) {
		super(res);
	}
	
	public StatisticsPaymentDetailsSummarizeResponse(int code, String message, List<StatisticsPaymentTypeDetailsSummarizeVO> statisticsList) {
		super(code, message);
		this.statisticsList = statisticsList;
	}

	public StatisticsPaymentDetailsSummarizeResponse(ResponseMessages res, List<StatisticsPaymentTypeDetailsSummarizeVO> statisticsList) {
		super(res);
		this.statisticsList = statisticsList;
	}

	public List<StatisticsPaymentTypeDetailsSummarizeVO> getStatisticsList() {
		return statisticsList;
	}

}
