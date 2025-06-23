package com.example.FinCore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.FinCore.constants.ApiDocConstants;
import com.example.FinCore.service.itfc.UserVerifyCodeService;
import com.example.FinCore.vo.request.UpdatePwdByEmailRequest;
import com.example.FinCore.vo.response.BasicResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Parameter;

@CrossOrigin
@RestController
@RequestMapping(value = "finbook/userVerifyCode/")
@Tag(name = "信箱驗證 API", description = "提供發送驗證信.認證驗證碼.重設密碼的API。")
public class UserVerifyCodeController {
	
	@Autowired
	private UserVerifyCodeService service;
	
	@PostMapping(value = "sendVerifyCode")
	@Operation(
			summary = ApiDocConstants.USER_VERIFY_CODE_SEND_VERIFY_CODE_SUMMARY,
			description = ApiDocConstants.USER_VERIFY_CODE_SEND_VERIFY_CODE_DESC + ApiDocConstants.TEST_PASS,
			method = "POST",
			parameters = {@Parameter(name = "account", description = "會員帳號(Email)")}
		)
		@ApiResponses({
			@ApiResponse(responseCode = "200", description = ApiDocConstants.SEND_SUCCESS),
			@ApiResponse(responseCode = "404", description = ApiDocConstants.ACCOUNT_NOT_FOUND)
		})
	public BasicResponse sendVerifyCode(@RequestParam("account") String account) {
		return service.sendVerifyCode(account);
	}

	@PostMapping(value = "checkVerifyCode")
	@Operation(
			summary = ApiDocConstants.USER_VERIFY_CODE_CHECK_VERIFY_CODE_SUMMARY,
			description = ApiDocConstants.USER_VERIFY_CODE_CHECK_VERIFY_CODE_DESC + ApiDocConstants.TEST_PASS,
			method = "POST",
			parameters = {
				@Parameter(name = "code", description = "驗證碼"),
				@Parameter(name = "account", description = "會員帳號(Email)")
			}
		)
		@ApiResponses({
			@ApiResponse(responseCode = "200", description = ApiDocConstants.VERIFY_SUCCESS),
			@ApiResponse(responseCode = "400", description = ApiDocConstants.USER_VERIFY_CODE_FAIL),
			@ApiResponse(responseCode = "404", description = ApiDocConstants.USER_VERIFY_CODE_NOT_FOUND)
		})
	 public BasicResponse checkVerifyCode(//
			 @RequestParam("code") String code,//
			 @RequestParam("account") String account ) {
	  return service.checkVerifyCode(code, account);
	 }

	@PostMapping(value = "updatePwdByEmail")
	@Operation(
			summary = ApiDocConstants.USER_VERIFY_CODE_UPDATE_PWD_BY_EMAIL_SUMMARY,
			description = ApiDocConstants.USER_VERIFY_CODE_UPDATE_PWD_BY_EMAIL_DESC + ApiDocConstants.TEST_PASS,
			method = "POST",
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "重設密碼請求資料:"
					+ ApiDocConstants.USER_VERIFY_CODE_UPDATE_PWD_BY_EMAIL_REQUEST_BODY_RULE))
	
		@ApiResponses({
			@ApiResponse(responseCode = "200", description = ApiDocConstants.UPDATE_SUCCESS),
			@ApiResponse(responseCode = "400", description = ApiDocConstants.USER_UPDATE_PWD_NOT_VERIFIED),
			@ApiResponse(responseCode = "404", description = ApiDocConstants.ACCOUNT_NOT_FOUND)
		})
	public BasicResponse updatePwdByEmail(@Valid @RequestBody UpdatePwdByEmailRequest req) {
		return service.updatePwdByEmail(req);
	}

}
