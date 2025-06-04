package com.example.FinCore.vo.response;

import java.util.List;

import com.example.FinCore.constants.ResponseMessages;
import com.example.FinCore.vo.PaymentTypeVO;

/**
 * 該回應封包資料用來傳送蒐集到的款項類型資料陣列。
 */
public class GetPaymentTypeListResponse extends BasicResponse
{
	
	private List<PaymentTypeVO> paymentTypeList;

	public GetPaymentTypeListResponse() {
		super();
	}

	public GetPaymentTypeListResponse(int code, String message) {
		super(code, message);
	}
	
	public GetPaymentTypeListResponse(int code, String message, List<PaymentTypeVO> paymentTypeList) {
		super(code, message);
		this.paymentTypeList = paymentTypeList;
	}

	public GetPaymentTypeListResponse(ResponseMessages res) {
		super(res);
	}
	
	public GetPaymentTypeListResponse(ResponseMessages res, List<PaymentTypeVO> paymentTypeList) {
		super(res);
		this.paymentTypeList = paymentTypeList;
	}

	public List<PaymentTypeVO> getPaymentTypeList() {
		return paymentTypeList;
	}
	
}
