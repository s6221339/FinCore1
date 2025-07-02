package com.example.FinCore.vo.response;

import java.util.List;

import com.example.FinCore.constants.ResponseMessages;
import com.example.FinCore.vo.StatisticsIncomeAndOutlayVO;

public class StatisticsIncomeAndOutlayResponse extends BasicResponse 
{
	
	private List<StatisticsIncomeAndOutlayVO> voList;

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
		this.voList = voList;
	}

	public StatisticsIncomeAndOutlayResponse(ResponseMessages res, List<StatisticsIncomeAndOutlayVO> voList) {
		super(res);
		this.voList = voList;
	}

	public List<StatisticsIncomeAndOutlayVO> getVoList() {
		return voList;
	}
	
}
