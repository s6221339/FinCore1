package com.example.FinCore.service.itfc;

import com.example.FinCore.vo.request.GetBudgetByAccountRequest;
import com.example.FinCore.vo.request.GetBudgetByBalanceIdRequest;
import com.example.FinCore.vo.request.UpdateBalanceRequest;
import com.example.FinCore.vo.response.BasicResponse;
import com.example.FinCore.vo.response.BudgetListResponse;
import com.example.FinCore.vo.response.BudgetResponse;

public interface BalanceService 
{
	
	/**
	 * 創建帳戶服務。
	 * @param req 創建請求資料
	 * @return 基本回應資料
	 */
	public BasicResponse create(CreateBalanceRequest req) throws Exception;
	
	/**
	 * 更新帳戶服務，可更新帳戶名稱與當月儲蓄設定。
	 * @param req 更新請求資料
	 * @return 基本回應資料
	 */
	public BasicResponse update(UpdateBalanceRequest req) throws Exception;
	
	/**
	 * 刪除帳戶服務。<br>
	 * ⚠️：該操作會將帳戶下的所有關聯資料一併清除，不可復原。
	 * @param balanceId 帳戶編號
	 * @return 基本回應資料
	 */
	public BasicResponse delete(int balanceId) throws Exception;
	
	/**
	 * 刪除指定帳號底下的所有帳戶。<br>
	 * ⚠️：該操作會將帳戶下的所有關聯資料一併清除，不可復原。
	 * @param account 指定帳號
	 * @return 基本回應資料
	 */
	public BasicResponse deleteByAccount(String account) throws Exception;
	
	/**
	 * 取得該帳戶的預算餘額與總預算。
	 * @param req 請求資料
	 * @return 帳戶底下相關預算資料的回應資料
	 */
	public BudgetResponse getBudget(GetBudgetByBalanceIdRequest req);
	
	/**
	 * 取得指定帳號底下「所有帳戶」的預算餘額與總預算
	 * @param req 請求資料
	 * @return 包括了帳號底下的所有帳戶預算資料的回應資料
	 */
	public BudgetListResponse getBudgetByAccount(GetBudgetByAccountRequest req);
	
}
