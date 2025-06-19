package com.example.FinCore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
@Tag(name = "信箱驗證 API", description = "提供發送驗證信.認證驗證碼的API。")
public class UserVerifyCodeController {
	
	@Autowired
	private UserVerifyCodeService service;
	
	@PostMapping(value = "sendVerificationLetter")
	@Operation(
			summary = "發送驗證信",
			description = "發送一組驗證碼到指定會員的 email 信箱，10 分鐘內有效。",
			method = "POST",
			parameters = {@Parameter(name = "account", description = "會員帳號(Email)")}
		)
		@ApiResponses({
			@ApiResponse(responseCode = "200", description = "驗證信發送成功"),
			@ApiResponse(responseCode = "404", description = "查無此帳號")
		})
	public BasicResponse sendVerificationLetter(@RequestParam("account") String account) {
		return service.sendVerificationLetter(account);
	}

	@PostMapping(value = "checkVerification")
	@Operation(
			summary = "認證驗證碼",
			description = "比對指定會員的驗證碼是否正確且未過期，成功則設帳號為已驗證。",
			method = "POST",
			parameters = {
				@Parameter(name = "code", description = "驗證碼"),
				@Parameter(name = "account", description = "會員帳號(Email)")
			}
		)
		@ApiResponses({
			@ApiResponse(responseCode = "200", description = "驗證成功"),
			@ApiResponse(responseCode = "400", description = "驗證失敗或驗證碼錯誤"),
			@ApiResponse(responseCode = "404", description = "查無此驗證資料")
		})
	 public BasicResponse checkVerification(//
			 @RequestParam("code") String code,//
			 @RequestParam("account") String account ) {
	  return service.checkVerification(code, account);
	 }

	@PostMapping(value = "updatePwdByEmail")
	@Operation(
			summary = "忘記密碼－重設密碼",
			description = "會員經過信箱驗證成功後，使用此 API 進行新密碼設定。",
			method = "POST",
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "重設密碼請求資料")
		)
		@ApiResponses({
			@ApiResponse(responseCode = "200", description = "密碼重設成功"),
			@ApiResponse(responseCode = "400", description = "尚未驗證，禁止重設密碼"),
			@ApiResponse(responseCode = "404", description = "查無此帳號")
		})
	public BasicResponse updatePwdByEmail(@Valid @RequestBody UpdatePwdByEmailRequest req) {
		return service.updatePwdByEmail(req);
	}

}
