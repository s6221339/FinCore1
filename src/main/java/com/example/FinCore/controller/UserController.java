package com.example.FinCore.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.FinCore.constants.ApiDocConstants;
import com.example.FinCore.service.itfc.UserService;
import com.example.FinCore.vo.request.CreateUserRequest;
import com.example.FinCore.vo.request.UpdatePasswordUserRequest;
import com.example.FinCore.vo.request.UpdatePwdByEmailRequest;
import com.example.FinCore.vo.request.UpdateUserRequest;
import com.example.FinCore.vo.request.loginRequest;
import com.example.FinCore.vo.response.BasicResponse;
import com.example.FinCore.vo.response.FamilyListResponse;
import com.example.FinCore.vo.response.UserResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping(value = "finbook/user/")
@Tag(name = "會員 API", description = "提供註冊、註銷、更新等操作的API。")
public class UserController {

	@Autowired
	private UserService service;

	@PostMapping(value = "register")
	@Operation(summary = "註冊新會員", description = "建立一個會員資料，註冊的會員帳號不可重複<br>"
			+ ApiDocConstants.TEST_PASS, method = "POST", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "註冊請求資料，規則："
					+ ApiDocConstants.USER_REGISTER_REQUEST_BODY_RULE))
	@ApiResponses({ @ApiResponse(responseCode = "200", description = ApiDocConstants.CREATE_SUCCESS),
			@ApiResponse(responseCode = "400", description = ApiDocConstants.ACCOUNT_EXIST) })
	public BasicResponse register(@Valid @RequestBody CreateUserRequest req) {
		return service.register(req);
	}

	@PostMapping(value = "update")
	@Operation(summary = "更新會員資料", description = "更新已存在的會員資料<br>"
			+ ApiDocConstants.TEST_PASS, method = "POST", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "更新請求資料，規則：<br>"
					+ ApiDocConstants.USER_UPDATE_REQUEST_BODY_RULE))
	@ApiResponses({ @ApiResponse(responseCode = "200", description = ApiDocConstants.UPDATE_SUCCESS),
			@ApiResponse(responseCode = "400", description = ApiDocConstants.UPDATE_USER_FAIL),
			@ApiResponse(responseCode = "404", description = ApiDocConstants.ACCOUNT_NOT_EXIST) })
	public BasicResponse update(@Valid @RequestBody UpdateUserRequest req) {
		return service.update(req);
	}

	@PostMapping(value = "cancel")

	@Operation(summary = "註銷會員資料", description = "刪除會員資料，該操作會將所有關聯資料－包含帳戶、設定等等全數刪除且無法復原，須謹慎操作。<br>"
			+ ApiDocConstants.TEST_PASS, method = "POST", parameters = {
					@Parameter(name = "account", description = "指定要刪除的帳號") })
	@ApiResponses({ @ApiResponse(responseCode = "200", description = ApiDocConstants.DELETE_SUCCESS),
			@ApiResponse(responseCode = "404", description = ApiDocConstants.ACCOUNT_NOT_EXIST) })
	public BasicResponse cancel(@RequestParam("account") String account) throws Exception {
		return service.cancel(account);
	}

	@PostMapping(value = "updatePasswordUser")
	@Operation(summary = "更新會員密碼", description = "更新會員密碼，在會員更新之前要求要給入原本的密碼並檢查，若不通過時操作失敗。<br>"
			+ ApiDocConstants.TEST_PASS, method = "POST", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "更新請求資料，規則：<br>"
					+ ApiDocConstants.USER_UPDATE_PW_REQUEST_BODY_RULE))
	@ApiResponses({ @ApiResponse(responseCode = "200", description = ApiDocConstants.UPDATE_SUCCESS),
			@ApiResponse(responseCode = "404", description = ApiDocConstants.ACCOUNT_NOT_EXIST) })
	public BasicResponse updatePasswordUser(@Valid @RequestBody UpdatePasswordUserRequest req) {
		return service.updatePasswordUser(req);
	}

	@PostMapping(value = "getUser")
	@Operation(summary = "取得會員資料", description = "取得單一筆會員資料。<br>"
			+ ApiDocConstants.TEST_PASS, method = "POST", parameters = {
					@Parameter(name = "account", description = "指定要搜尋的帳號") })
	@ApiResponses({ @ApiResponse(responseCode = "200", description = ApiDocConstants.SEARCH_SUCCESS),
			@ApiResponse(responseCode = "404", description = ApiDocConstants.ACCOUNT_NOT_EXIST) })
	public UserResponse getUser(@RequestParam("account") String account) {
		return service.getUser(account);
	}

	@PostMapping(value = "getFamilyByAccount")
	@Operation(summary = "取得查詢會員在哪個家庭群組資料", description = "查詢會員是哪個家庭群組的成員或者是家庭群組的owner。<br>"
			+ ApiDocConstants.TEST_PASS, method = "POST", parameters = {
					@Parameter(name = "account", description = "指定要搜尋的帳號") })
	@ApiResponses({ @ApiResponse(responseCode = "200", description = ApiDocConstants.SEARCH_SUCCESS),
			@ApiResponse(responseCode = "404", description = ApiDocConstants.ACCOUNT_NOT_EXIST) })
	public FamilyListResponse getFamilyByAccount(@RequestParam("account") String account)
			throws JsonProcessingException {
		return service.getFamilyByAccount(account);
	}

	@PostMapping(value = "login")
	@Operation(summary = "會員登入", description = "會員登入確認是否有此帳號，並檢查密碼是否有誤。<br>"
			+ ApiDocConstants.TEST_PASS, method = "POST", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "登入請求資料，規則：<br>"
					+ ApiDocConstants.USER_LOGIN_REQUEST_BODY_RULE))
	@ApiResponses({ @ApiResponse(responseCode = "200", description = ApiDocConstants.SEARCH_SUCCESS),
			@ApiResponse(responseCode = "404", description = ApiDocConstants.ACCOUNT_NOT_EXIST) })
	public BasicResponse login(@Valid @RequestBody loginRequest req) {
		return service.login(req);
	}

	@PostMapping(value = "sendVerificationLetter")
	public BasicResponse sendVerificationLetter(@RequestParam("account") String account) {
		return service.sendVerificationLetter(account);
	}

	@PostMapping(value = "checkVerification")
	 public BasicResponse checkVerification(//
			 @RequestParam("code") String code,//
			 @RequestParam("account") String account ) {
	  return service.checkVerification(code, account);
	 }

	@PostMapping(value = "updatePwdByEmail")
	public BasicResponse updatePwdByEmail(@Valid @RequestBody UpdatePwdByEmailRequest req) {
		return service.updatePwdByEmail(req);
	}

}
