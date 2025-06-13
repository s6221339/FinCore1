package com.example.FinCore.vo.response;

import java.util.List;

import com.example.FinCore.constants.ResponseMessages;
import com.example.FinCore.entity.Balance;

/**
 * 封裝帳戶資訊的回應資料
 */
public class BalanceListResponse extends BasicResponse
{
	
	private List<Balance> balanceList;

	public BalanceListResponse() {
		super();
	}

	public BalanceListResponse(int code, String message) {
		super(code, message);
	}
	
	public BalanceListResponse(int code, String message, List<Balance> balanceList) {
		super(code, message);
		this.balanceList = balanceList;
	}

	public BalanceListResponse(ResponseMessages res) {
		super(res);
	}
	
	public BalanceListResponse(ResponseMessages res, List<Balance> balanceList) {
		super(res);
		this.balanceList = balanceList;
	}

	public BalanceListResponse(List<Balance> balanceList) {
		super();
		this.balanceList = balanceList;
	}

	public List<Balance> getBalanceList() {
		return balanceList;
	}
	
}
