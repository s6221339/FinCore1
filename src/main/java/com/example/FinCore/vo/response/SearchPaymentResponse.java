package com.example.FinCore.vo.response;

import java.util.List;

import com.example.FinCore.constants.ResponseMessages;
import com.example.FinCore.vo.BalanceWithPaymentVO;

/**
 * 該回應主要回傳「一組帳號底下所有帳戶對應的所有款項」。
 */
public class SearchPaymentResponse extends BasicResponse
{
	
	List<BalanceWithPaymentVO> balanceWithPaymentList;

	public SearchPaymentResponse() {
		super();
	}

	public SearchPaymentResponse(int code, String message) {
		super(code, message);
	}
	
	public SearchPaymentResponse(int code, String message, List<BalanceWithPaymentVO> balanceWithPaymentList) {
		super(code, message);
		this.balanceWithPaymentList = balanceWithPaymentList;
	}

	public SearchPaymentResponse(ResponseMessages res) {
		super(res);
	}
	
	public SearchPaymentResponse(ResponseMessages res, List<BalanceWithPaymentVO> balanceWithPaymentList) {
		super(res);
		this.balanceWithPaymentList = balanceWithPaymentList;
	}

	public List<BalanceWithPaymentVO> getBalanceWithPaymentList() {
		return balanceWithPaymentList;
	}
	
}
