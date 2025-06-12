package com.example.FinCore.vo.response;

import com.example.FinCore.constants.ResponseMessages;
import com.example.FinCore.vo.BudgetVO;

/**
 * 該回應資料搭載了帳戶預算的相關資料。
 */
public class BudgetResponse extends BasicResponse
{
	
	private BudgetVO budgetVO;

	public BudgetResponse() {
		super();
	}

	public BudgetResponse(int code, String message) {
		super(code, message);
	}
	
	public BudgetResponse(int code, String message, BudgetVO budgetVO) {
		super(code, message);
		this.budgetVO = budgetVO;
	}

	public BudgetResponse(ResponseMessages res) {
		super(res);
	}
	
	public BudgetResponse(ResponseMessages res, BudgetVO budgetVO) {
		super(res);
		this.budgetVO = budgetVO;
	}

	public BudgetVO getBudgetVO() {
		return budgetVO;
	}
	
}
