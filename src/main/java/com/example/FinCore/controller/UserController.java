package com.example.FinCore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.FinCore.constants.ApiDocConstants;
import com.example.FinCore.constants.ResponseMessages;
import com.example.FinCore.service.itfc.UserService;
import com.example.FinCore.vo.request.RegisterUserRequest;
import com.example.FinCore.vo.request.UpdatePasswordUserRequest;
import com.example.FinCore.vo.request.UpdateUserRequest;
import com.example.FinCore.vo.request.loginRequest;
import com.example.FinCore.vo.response.BasicResponse;
import com.example.FinCore.vo.response.FamilyAvatarListResponse;
import com.example.FinCore.vo.response.MemberNameResponse;
import com.example.FinCore.vo.response.SubscriptionResponse;

import com.example.FinCore.vo.response.UserResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping(value = "finbook/user/")
@Tag(name = "會員 API", description = "提供註冊、註銷、更新等操作的API。")
public class UserController {

	@Autowired
	private UserService service;

	@PostMapping(value = "register")
	@Operation(
		    summary = ApiDocConstants.USER_REGISTER_SUMMARY,
		    description = ApiDocConstants.USER_REGISTER_DESC,
		    method = "POST",
		    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
		        description = "會員註冊請求資料規則：" + ApiDocConstants.USER_REGISTER_REQUEST_BODY_RULE
		    )
		)
		@ApiResponses({
		    @ApiResponse(responseCode = "200", description = ApiDocConstants.CREATE_SUCCESS),
		    @ApiResponse(responseCode = "400", description = ApiDocConstants.ACCOUNT_EXIST),
		})
	public BasicResponse register(@Valid @RequestBody RegisterUserRequest req) {
		return service.register(req);
	}

	@PostMapping(value = "update")
	@Operation(
		    summary = ApiDocConstants.USER_UPDATE_SUMMARY,
		    description = ApiDocConstants.USER_UPDATE_DESC,
		    method = "POST",
		    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
		        description = "會員資料更新請求資料規則：" + ApiDocConstants.USER_UPDATE_REQUEST_BODY_RULE
		    )
		)
		@ApiResponses({
		    @ApiResponse(responseCode = "200", description = ApiDocConstants.UPDATE_SUCCESS),
		    @ApiResponse(responseCode = "400", description = ApiDocConstants.UPDATE_USER_FAIL),
		    @ApiResponse(responseCode = "404", description = ApiDocConstants.ACCOUNT_NOT_FOUND),
		})
	public BasicResponse update(@Valid @RequestBody UpdateUserRequest req) {
		return service.update(req);
	}

	@PostMapping(value = "cancel")
	@Operation(
		    summary = ApiDocConstants.USER_CANCEL_SUMMARY,
		    description = ApiDocConstants.USER_CANCEL_DESC,
		    method = "POST",
		    parameters = {
		        @Parameter(
		            name = "account",
		            description = "會員帳號（必填）"
		        )
		    }
		)
		@ApiResponses({
		    @ApiResponse(responseCode = "200", description = ApiDocConstants.DELETE_SUCCESS),
		    @ApiResponse(responseCode = "400", description = ApiDocConstants.MISSING_REQUIRED_FIELD),
		    @ApiResponse(responseCode = "404", description = ApiDocConstants.ACCOUNT_NOT_FOUND),
		})
	public BasicResponse cancel(@RequestParam("account") String account) throws Exception {
		return service.cancel(account);
	}

	@PostMapping(value = "updatePasswordUser")
	@Operation(
		    summary = ApiDocConstants.USER_UPDATE_PASSWORD_SUMMARY,
		    description = ApiDocConstants.USER_UPDATE_PASSWORD_DESC,
		    method = "POST",
		    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
		        description = "會員修改密碼請求資料規則：" + ApiDocConstants.USER_UPDATE_PASSWORD_REQUEST_BODY_RULE
		    )
		)
		@ApiResponses({
		    @ApiResponse(responseCode = "200", description = ApiDocConstants.UPDATE_SUCCESS),
		    @ApiResponse(responseCode = "400", description = ApiDocConstants.USER_UPDATE_PASSWORD_RESPONSE_400),
		    @ApiResponse(responseCode = "404", description = ApiDocConstants.ACCOUNT_NOT_FOUND),
		})
	public BasicResponse updatePasswordUser(@Valid @RequestBody UpdatePasswordUserRequest req) {
		return service.updatePasswordUser(req);
	}

	@PostMapping(value = "getUser")
	@Operation(
		    summary = ApiDocConstants.USER_GET_SUMMARY,
		    description = ApiDocConstants.USER_GET_DESC,
		    method = "POST",
		    parameters = {
		        @Parameter(
		            name = "account",
		            description = "會員帳號（必填）"
		        )
		    }
		)
		@ApiResponses({
			@ApiResponse(responseCode = "200", 
					description = ApiDocConstants.SEARCH_SUCCESS, 
					content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))}),
			@ApiResponse(responseCode = "400", description = ApiDocConstants.MISSING_REQUIRED_FIELD),
		    @ApiResponse(responseCode = "404", description = ApiDocConstants.ACCOUNT_NOT_FOUND),
		})
	public BasicResponse getUser(@RequestParam("account") String account) {
		return service.getUser(account);
	}

	@PostMapping(value = "getFamilyByAccount")
	@Operation(
		    summary = ApiDocConstants.FAMILY_GET_BY_ACCOUNT_SUMMARY,
		    description = ApiDocConstants.FAMILY_GET_BY_ACCOUNT_DESC,
		    method = "POST",
		    parameters = {
		        @Parameter(
		            name = "account",
		            description = "會員帳號（必填）"
		        )
		    }
		)
		@ApiResponses({
			@ApiResponse(responseCode = "200", 
					description = ApiDocConstants.SEARCH_SUCCESS, 
					content = {@Content(mediaType = "application/json", schema = @Schema(implementation = FamilyAvatarListResponse.class))}),
			@ApiResponse(responseCode = "400", description = ApiDocConstants.MISSING_REQUIRED_FIELD),
		})
	public BasicResponse getFamilyByAccount(@RequestParam("account") String account)
			throws JsonProcessingException {
		return service.getFamilyByAccount(account);
	}

	@PostMapping(value = "login")
	@Operation(
		    summary = ApiDocConstants.USER_LOGIN_SUMMARY,
		    description = ApiDocConstants.USER_LOGIN_DESC,
		    method = "POST",
		    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
		        description = "會員登入請求資料規則：" + ApiDocConstants.USER_LOGIN_REQUEST_BODY_RULE
		    )
		)
		@ApiResponses({
		    @ApiResponse(responseCode = "200", description = ApiDocConstants.LOGIN_SUCCESS),
		    @ApiResponse(responseCode = "400", description = ApiDocConstants.PASSWORD_NOT_MATCH),
		    @ApiResponse(responseCode = "404", description = ApiDocConstants.ACCOUNT_NOT_FOUND),
		})
	public BasicResponse login(@Valid @RequestBody loginRequest req, HttpSession session) {
		var res = service.login(req);
		if(res.getCode() == 200)
		{
			session.setAttribute("account", req.account());
			session.setAttribute("sessionId", session.getId());
			session.setMaxInactiveInterval(604800);
		}
		return res;

	}
	
	@PostMapping(value = "getNameByAccount")
	@Operation(
		    summary = ApiDocConstants.USER_GET_NAME_BY_ACCOUNT_SUMMARY,
		    description = ApiDocConstants.USER_GET_NAME_BY_ACCOUNT_DESC,
		    method = "POST",
		    parameters = {
		        @Parameter(
		            name = "account",
		            description = "會員帳號（必填）"
		        )
		    }
		)
		@ApiResponses({
			@ApiResponse(responseCode = "200", 
					description = ApiDocConstants.SEARCH_SUCCESS, 
					content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MemberNameResponse.class))}),
			@ApiResponse(responseCode = "400", description = ApiDocConstants.MISSING_REQUIRED_FIELD),
		    @ApiResponse(responseCode = "404", description = ApiDocConstants.ACCOUNT_NOT_FOUND),
		})
	public BasicResponse getNameByAccount(@RequestParam("account") String account) {
		return service.getNameByAccount(account);
	}
	
	@PostMapping(value = "logout")
	@Operation(
		    summary = ApiDocConstants.USER_LOGOUT_SUMMARY,
		    description = ApiDocConstants.USER_LOGOUT_DESC,
		    method = "POST",
		    parameters = {
		        @Parameter(
		            name = "account",
		            description = "會員帳號（必填）"
		        )
		    }
		)
		@ApiResponses({
		    @ApiResponse(responseCode = "200", description = ApiDocConstants.LOGOUT_SUCCESS),
		    @ApiResponse(responseCode = "500", description = ApiDocConstants.INVALID_SESSION),
		})
	/**
	 * 使用者登出（如需真正後端驗證，請配合 session 或 token blacklist 等方式）
	 * @param account 使用者帳號
	 * @return 登出結果
	 */
	public BasicResponse logout(HttpSession session) {
		try {
			session.invalidate();
		}
	    catch (IllegalStateException e) {
	    	return new BasicResponse(ResponseMessages.INVALID_SESSION);
		}
		return new BasicResponse(ResponseMessages.SUCCESS);
	}

	    /**
	     * 更新會員訂閱狀態
	     * @param account 會員帳號
	     * @param subscription 是否訂閱
	     * @param expirationDate 訂閱到期日（yyyy-MM-ddTHH:mm:ss）
	     * @return BasicResponse
	     */
	    @PostMapping(value = "updateSubscription")
	    @Operation(
	    	    summary = ApiDocConstants.USER_UPDATE_SUBSCRIPTION_SUMMARY,
	    	    description = ApiDocConstants.USER_UPDATE_SUBSCRIPTION_DESC,
	    	    method = "POST",
	    	    parameters = {
	    	        @Parameter(name = "account", description = "會員帳號（必填）"),
	    	        @Parameter(name = "subscription", description = "是否訂閱（必填，true/false）")
	    	    }
	    	)
	    	@ApiResponses({
	    	    @ApiResponse(responseCode = "200", description = ApiDocConstants.UPDATE_SUCCESS),
	    	    @ApiResponse(responseCode = "400", description = ApiDocConstants.USER_UPDATE_SUBSCRIPTION_RESPONSE_400),
	    	    @ApiResponse(responseCode = "404", description = ApiDocConstants.ACCOUNT_NOT_FOUND),
	    	})
	    public BasicResponse updateSubscription(
	            @RequestParam("account") String account,
	            @RequestParam("subscription") Boolean subscription
	    ) {
	        return service.updateSubscription(account, subscription);
	    }

	    /**
	     * 查詢會員訂閱狀態
	     * @param account 會員帳號
	     * @return SubscriptionResponse
	     */
	    @PostMapping(value = "getSubscription")
	    @Operation(
	    	    summary = ApiDocConstants.USER_GET_SUBSCRIPTION_SUMMARY,
	    	    description = ApiDocConstants.USER_GET_SUBSCRIPTION_DESC,
	    	    method = "GET",
	    	    parameters = {
	    	        @Parameter(name = "account", description = "會員帳號（必填）")
	    	    }
	    	)
	    	@ApiResponses({
	    	    @ApiResponse(responseCode = "200", description = ApiDocConstants.SEARCH_SUCCESS),
	    	    @ApiResponse(responseCode = "400", description = ApiDocConstants.MISSING_REQUIRED_FIELD),
	    	    @ApiResponse(responseCode = "404", description = ApiDocConstants.ACCOUNT_NOT_FOUND),
	    	})
	    public SubscriptionResponse getSubscription(@RequestParam("account") String account) {
	        return service.getSubscription(account);
	    }
}
