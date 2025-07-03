package com.example.FinCore.vo.response;

import java.util.List;

import com.example.FinCore.constants.ResponseMessages;
import com.example.FinCore.vo.StatisticsPaymentTypeVO;

/**
 * 承載以年月為單位的帳款類型統計資料
 */
public class StatisticsPersonalBalanceWithPaymentTypeResponse extends BasicResponse
{
	
	private List<StatisticsPaymentTypeVO> statisticsList;

	public StatisticsPersonalBalanceWithPaymentTypeResponse() {
		super();
	}

	public StatisticsPersonalBalanceWithPaymentTypeResponse(int code, String message) {
		super(code, message);
	}

	public StatisticsPersonalBalanceWithPaymentTypeResponse(ResponseMessages res) {
		super(res);
	}

	public StatisticsPersonalBalanceWithPaymentTypeResponse(int code, String message, List<StatisticsPaymentTypeVO> statisticsList) {
		super(code, message);
		this.statisticsList = statisticsList;
	}

	public StatisticsPersonalBalanceWithPaymentTypeResponse(ResponseMessages res, List<StatisticsPaymentTypeVO> statisticsList) {
		super(res);
		this.statisticsList = statisticsList;
	}

	public List<StatisticsPaymentTypeVO> getStatisticsList() {
		return statisticsList;
	}
	
}
