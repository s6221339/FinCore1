package com.example.FinCore.service.itfc;

import com.example.FinCore.vo.request.CreateUserRequest;
import com.example.FinCore.vo.request.UpdateUserRequest;
import com.example.FinCore.vo.response.BasicResponse;

public interface UserService {

	/**
	 * 新增會員
	 * @param req
	 * @return
	 */
	public BasicResponse create(CreateUserRequest req);
	
	/**
	 * 修改會員資料
	 * @param req
	 * @return
	 */
	public BasicResponse update(UpdateUserRequest req);
	
	/**
	 * 刪除會員
	 * @param account
	 * @return
	 */
	public BasicResponse delete(String account);
}
