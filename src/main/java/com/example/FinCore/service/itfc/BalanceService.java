package com.example.FinCore.service.itfc;

import com.example.FinCore.vo.request.UpdateBalanceRequest;
import com.example.FinCore.vo.response.BasicResponse;

public interface BalanceService 
{
	
	/**
	 * 創建帳戶服務。
	 * @param req 創建請求資料
	 * @return 基本回應資料
	 */
	public BasicResponse create(CreateBalanceRequest req);
	
	/**
	 * 更新帳戶服務。
	 * @param req 更新請求資料
	 * @return 基本回應資料
	 */
	public BasicResponse update(UpdateBalanceRequest req);
	
}
