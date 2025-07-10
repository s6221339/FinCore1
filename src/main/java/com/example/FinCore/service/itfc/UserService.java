package com.example.FinCore.service.itfc;

import java.util.Map;

import com.example.FinCore.vo.request.RegisterUserRequest;
import com.example.FinCore.vo.request.UpdatePasswordUserRequest;
import com.example.FinCore.vo.request.UpdateUserRequest;
import com.example.FinCore.vo.request.loginRequest;
import com.example.FinCore.vo.response.BasicResponse;
import com.example.FinCore.vo.response.FamilyAvatarListResponse;
import com.example.FinCore.vo.response.MemberNameResponse;
import com.example.FinCore.vo.response.SubscriptionResponse;
import com.example.FinCore.vo.response.UserResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface UserService {

	/**
	 * 註冊會員
	 * @param req
	 * @return
	 */
	public BasicResponse register(RegisterUserRequest req);
	
	/**
	 * 修改會員資料
	 * @param req
	 * @return
	 */
	public BasicResponse update(UpdateUserRequest req);
	
	/**
	 * 註銷會員
	 * @param account
	 * @return
	 */
	public BasicResponse cancel(String account) throws Exception;
	
	/**
	 * 會員修該密碼
	 * @param req
	 * @return
	 */
	public BasicResponse updatePasswordUser(UpdatePasswordUserRequest req);
	
	/**
	 * 查詢會員資訊
	 * @param account
	 * @return
	 */
	public UserResponse getUser(String account);
	
	/**
	 * 查詢會員在哪個家庭群組
	 * @param account
	 * @return
	 */
	public FamilyAvatarListResponse getFamilyByAccount(String account) throws JsonProcessingException;
	
	/**
	 * 會員登入
	 * @param req
	 * @return
	 */
	public BasicResponse login(loginRequest req);

	/**
	 * 根據帳號查詢成員名稱
	 * @param account 使用者帳號
	 * @return 姓名（找不到會回傳 null）
	 */
	public MemberNameResponse getNameByAccount(String account);
	
	/**
	 * 更新會員訂閱狀態
	 * @param account 使用者帳號
	 * @param subscription 訂閱狀態
	 * @param expirationDate 訂閱到期日期
	 * @return
	 */
	public BasicResponse updateSubscription(String account, Boolean subscription);
	
	/**
	 * 查詢會員訂閱狀態
	 * @param account 使用者帳號
	 * @return
	 */
	public SubscriptionResponse getSubscription(String account);
	
	/**
	 * 產生綠界金流 ECPay 訂單參數
	 * @param account 會員帳號（未來可用於記錄訂單）
	 * @return ECPay 所需的表單欄位參數
	 */
	public Map<String, String> getECPayForm(String account);
	
	/**
	 * 處理綠界回傳付款結果並更新訂閱
	 * @param merchantTradeNo 訂單編號（內含 account）
	 * @param rtnCode 藍新付款回傳代碼
	 * @return 回傳給藍新的訊息
	 */
	public String handleECPayNotify(String merchantTradeNo, String rtnCode);
}
