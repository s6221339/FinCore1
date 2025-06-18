package com.example.FinCore.service.itfc;

import java.time.LocalDateTime;

import com.example.FinCore.vo.request.CreateUserRequest;
import com.example.FinCore.vo.request.UpdatePasswordUserRequest;
import com.example.FinCore.vo.request.UpdatePwdByEmailRequest;
import com.example.FinCore.vo.request.UpdateUserRequest;
import com.example.FinCore.vo.request.loginRequest;
import com.example.FinCore.vo.response.BasicResponse;
import com.example.FinCore.vo.response.FamilyListResponse;
import com.example.FinCore.vo.response.UserResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

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
	
	public BasicResponse updatePwdByEmail(UpdatePwdByEmailRequest req);
	
	public BasicResponse sendVerificationLetter(String account);

	public BasicResponse checkVerification(String code, String account);
	
}
