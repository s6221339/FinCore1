package com.example.FinCore.service.itfc;

import com.example.FinCore.vo.request.AccountWithDateFilterRequest;
import com.example.FinCore.vo.request.CreateBalanceRequest;
import com.example.FinCore.vo.request.GetBudgetByBalanceIdRequest;
import com.example.FinCore.vo.request.UpdateBalanceRequest;
import com.example.FinCore.vo.response.BalanceListResponse;
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
	 * 更新帳戶服務，可更新帳戶名稱與當月儲蓄設定。如果更新名稱留空則不更新名稱、儲蓄負值時不更新儲蓄。
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
	public BudgetListResponse getBudgetByAccount(AccountWithDateFilterRequest req);
	
	/**
	 * 使用帳號取得所有關聯個人帳戶
	 * @param account 指定帳號
	 * @return 包括了所有帳戶資料的回應資料
	 */
	public BalanceListResponse getPersonalBalance(String account);
	
	/**
	 * 使用帳號取得所有關聯個人帳戶
	 * @param account 指定帳號
	 * @return 包括了所有帳戶資料的回應資料
	 */
	public BalanceListResponse getFamilyBalance(String account);
	
}
