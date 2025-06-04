package com.example.FinCore.service.itfc;

import com.example.FinCore.vo.request.CreatePaymentTypeRequest;
import com.example.FinCore.vo.response.BasicResponse;
import com.example.FinCore.vo.response.GetPaymentTypeListResponse;

public interface PaymentTypeService 
{
	
	/**
	 * 創建款項類型服務，讓使用者可自定義款項類型並新增。
	 * @param req 請求資料
	 * @return 基本回應資料
	 */
	public BasicResponse createType(CreatePaymentTypeRequest req);
	
	/**
	 * 取得由指定新增者創建的所有款項類型，其中包含「預設項目」。
	 * @param account 指定新增者
	 * @return 一個回應封包資料，包含相關的類型資料
	 */
	public GetPaymentTypeListResponse getTypeByAccount(String account);
	
}
