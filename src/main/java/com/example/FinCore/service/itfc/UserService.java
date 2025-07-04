package com.example.FinCore.service.itfc;

import com.example.FinCore.vo.request.RregisterUserRequest;
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
	public BasicResponse register(RregisterUserRequest req);
	
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
	
}
