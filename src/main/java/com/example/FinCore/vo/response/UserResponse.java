package com.example.FinCore.vo.response;

import com.example.FinCore.constants.ResponseMessages;
import com.example.FinCore.vo.UserVO;

public class UserResponse extends BasicResponse {

	private UserVO userVO;

	public UserResponse() {
		super();
	}

	public UserResponse(int code, String message) {
		super(code, message);
	}

	public UserResponse(int code, String message, UserVO userVO) {
		super(code, message);
		this.userVO = userVO;
	}

	public UserResponse(ResponseMessages res) {
		super(res);
	}

	public UserResponse(ResponseMessages res, UserVO userVO) {
		super(res);
		this.userVO = userVO;
	}

	public UserVO getUserVO() {
		return userVO;
	}

}
