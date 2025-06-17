package com.example.FinCore.service.itfc;

import com.example.FinCore.vo.response.SavingsListResponse;

public interface SavingsService 
{
	
	/**
	 * 取得指定帳號的所有儲蓄設定。
	 * @param account 帳號
	 * @return 包含儲蓄設定列表的回應資料
	 */
	public SavingsListResponse getAll(String account);
	
}
