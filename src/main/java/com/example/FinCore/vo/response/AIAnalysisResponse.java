package com.example.FinCore.vo.response;

import java.util.List;

import com.example.FinCore.constants.ResponseMessages;
import com.example.FinCore.vo.AIAnalysisVO;

public class AIAnalysisResponse extends BasicResponse 
{
	
	private List<AIAnalysisVO> analysisList;

	public AIAnalysisResponse() {
		super();
	}

	public AIAnalysisResponse(int code, String message) {
		super(code, message);
	}

	public AIAnalysisResponse(ResponseMessages res) {
		super(res);
	}
	
	public AIAnalysisResponse(int code, String message, List<AIAnalysisVO> analysisList) {
		super(code, message);
		this.analysisList = analysisList;
	}

	public AIAnalysisResponse(ResponseMessages res, List<AIAnalysisVO> analysisList) {
		super(res);
		this.analysisList = analysisList;
	}

	public List<AIAnalysisVO> getAnalysisList() {
		return analysisList;
	}
	
}
