package com.example.FinCore.vo.response;

import java.util.List;

import com.example.FinCore.constants.ResponseMessages;
import com.example.FinCore.vo.BudgetVO;

public class BudgetListResponse extends BasicResponse
{

	private List<BudgetVO> budgetList;

	public BudgetListResponse() {
		super();
	}

	public BudgetListResponse(int code, String message) {
		super(code, message);
	}
	
	public BudgetListResponse(int code, String message, List<BudgetVO> budgetList) {
		super(code, message);
		this.budgetList = budgetList;
	}

	public BudgetListResponse(ResponseMessages res) {
		super(res);
	}
	
	public BudgetListResponse(ResponseMessages res, List<BudgetVO> budgetList) {
		super(res);
		this.budgetList = budgetList;
	}

	public List<BudgetVO> getBudgetList() {
		return budgetList;
	}

}
