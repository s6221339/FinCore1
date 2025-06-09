package com.example.FinCore.service.itfc;

import com.example.FinCore.vo.request.CreateUserRequest;
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
	
	//修改密碼 1.帳號 2.舊密碼 3.新密碼
}
