package com.example.FinCore.service.itfc;

import com.example.FinCore.vo.request.RregisterUserRequest;
import com.example.FinCore.vo.request.UpdatePasswordUserRequest;
import com.example.FinCore.vo.request.UpdateUserRequest;
import com.example.FinCore.vo.request.loginRequest;
import com.example.FinCore.vo.response.BasicResponse;
import com.example.FinCore.vo.response.FamilyListResponse;
import com.example.FinCore.vo.response.MemberNameResponse;
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
	public FamilyListResponse getFamilyByAccount(String account) throws JsonProcessingException;
	
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
	 * 使用者登出（如需真正後端驗證，請配合 session 或 token blacklist 等方式）
	 * @param account 使用者帳號
	 * @return 登出結果
	 */
	BasicResponse logout(String account);
	
}
