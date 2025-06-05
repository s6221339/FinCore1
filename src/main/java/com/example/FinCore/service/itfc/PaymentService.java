package com.example.FinCore.service.itfc;

import com.example.FinCore.vo.request.CreatePaymentRequest;
import com.example.FinCore.vo.response.BasicResponse;

public interface PaymentService 
{
	
	/**
	 * 創建款項服務。
	 * @param req 創建請求資料
	 * @return 基本回應資料
	 */
	public BasicResponse create(CreatePaymentRequest req);
	
}
