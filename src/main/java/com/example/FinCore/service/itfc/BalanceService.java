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
	
	/**
	 * 刪除帳戶服務。<br>
	 * ⚠️：該操作會將帳戶下的所有款項以及AI查詢紀錄一併清除，不可復原。
	 * @param balanceId 帳戶編號
	 * @return 基本回應資料
	 */
	public BasicResponse delete(int balanceId) throws Exception;
	
	/**
	 * 刪除指定帳號底下的所有帳戶。<br>
	 * ⚠️：該操作會將帳戶下的所有款項以及AI查詢紀錄一併清除，不可復原。
	 * @param account 指定帳號
	 * @return 基本回應資料
	 */
	public BasicResponse deleteByAccount(String account) throws Exception;
	
}
