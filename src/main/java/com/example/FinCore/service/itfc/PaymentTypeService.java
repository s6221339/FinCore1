package com.example.FinCore.service.itfc;

import com.example.FinCore.vo.request.CreatePaymentTypeRequest;
import com.example.FinCore.vo.response.BasicResponse;

public interface PaymentTypeService 
{
	
	/**
	 * 創建款項類型服務，讓使用者可自定義款項類型並新增。
	 * @param req 請求資料
	 * @return 基本回應資料
	 */
	public BasicResponse createType(CreatePaymentTypeRequest req);
	
}
