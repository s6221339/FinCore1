package com.example.FinCore.service.itfc;

import com.example.FinCore.vo.request.CreateUserRequest;
import com.example.FinCore.vo.request.UpdatePasswordUserRequest;
import com.example.FinCore.vo.request.UpdateUserRequest;
import com.example.FinCore.vo.response.BasicResponse;

public interface UserService {

	/**
	 * 註冊會員
	 * @param req
	 * @return
	 */
	public BasicResponse register(CreateUserRequest req);
	
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
	public BasicResponse cancel(String account);
	
	/**
	 * 會員更新密碼
	 * @param req
	 * @return
	 */
	public BasicResponse updatePasswordUser(UpdatePasswordUserRequest req);
	
	/**
	 * 1.更新密碼 a.帳號 b.舊密碼 c.新密碼
	 * 2.查詢會員資料
	 * 3.查詢會員在哪個家庭群組
	 */
}
