package com.example.FinCore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.FinCore.constants.ApiDocConstants;
import com.example.FinCore.service.itfc.BalanceService;
import com.example.FinCore.vo.request.AccountWithDateFilterRequest;
import com.example.FinCore.vo.request.CreateBalanceRequest;
import com.example.FinCore.vo.request.GetBudgetByBalanceIdRequest;
import com.example.FinCore.vo.request.UpdateBalanceRequest;
import com.example.FinCore.vo.response.BalanceListResponse;
import com.example.FinCore.vo.response.BasicResponse;
import com.example.FinCore.vo.response.BudgetListResponse;
import com.example.FinCore.vo.response.BudgetResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(allowedHeaders = "*")
@RequestMapping(value = "finbook/balance/")
@Tag(name = "帳戶 API", description = "具有操作帳戶的魔法")
public class BalanceController 
{
	
	@Autowired
	private BalanceService service;

	@PostMapping(value = "create")
	@Operation(
			summary = ApiDocConstants.BALANCE_CREATE_SUMMARY,
			description = ApiDocConstants.BALANCE_CREATE_DESC + ApiDocConstants.TEST_PASS,
			method = "POST",
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
				description = "新增帳戶的請求資料，規則：" + ApiDocConstants.BALANCE_CREATE_REQUEST_BODY_RULE
			)
		)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = ApiDocConstants.CREATE_SUCCESS),
		@ApiResponse(responseCode = "400", description = ApiDocConstants.BALANCE_CREATE_RESPONSE_400),
		@ApiResponse(responseCode = "404", description = ApiDocConstants.BALANCE_CREATE_RESPONSE_404)
	})
	public BasicResponse create(@Valid @RequestBody CreateBalanceRequest req) throws Exception
	{
		return service.create(req);
	}
	
	@PostMapping(value = "update")
	@Operation(
			summary = ApiDocConstants.BALANCE_UPDATE_SUMMARY,
			description = ApiDocConstants.BALANCE_UPDATE_DESC + ApiDocConstants.TEST_PASS,
			method = "POST",
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
				description = "更新帳戶資料的請求格式，規則：" + ApiDocConstants.BALANCE_UPDATE_REQUEST_BODY_RULE
			)
		)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = ApiDocConstants.UPDATE_SUCCESS),
		@ApiResponse(responseCode = "404", description = ApiDocConstants.BALANCE_NOT_FOUND)
	})
	public BasicResponse update(@Valid @RequestBody UpdateBalanceRequest req) throws Exception
	{
		return service.update(req);
	}
	
	@PostMapping(value = "delete")
	@Operation(
			summary = ApiDocConstants.BALANCE_DELETE_SUMMARY,
			description = ApiDocConstants.BALANCE_DELETE_DESC + ApiDocConstants.TEST_PASS,
			method = "POST",
			parameters = {
					@Parameter(name = "balanceId", description = "欲刪除的帳戶 ID，必填")
			}
		)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = ApiDocConstants.DELETE_SUCCESS),
		@ApiResponse(responseCode = "404", description = ApiDocConstants.BALANCE_NOT_FOUND)
	})
	public BasicResponse delete(@RequestParam("balanceId") int balanceId) throws Exception
	{
		return service.delete(balanceId);
	}
	
//	@PostMapping(value = "deleteByAccount")
//	public BasicResponse deleteByAccount(@RequestParam("account") String account) throws Exception
//	{
//		return service.deleteByAccount(account);
//	}
	
	@PostMapping(value = "getBudget")
	@Operation(
			summary = ApiDocConstants.BALANCE_GET_BUDGET_SUMMARY,
			description = ApiDocConstants.BALANCE_GET_BUDGET_DESC,
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
				description = ApiDocConstants.BALANCE_GET_BUDGET_REQUEST_BODY_RULE,
				required = true
			)
		)
	@ApiResponses({
		@ApiResponse(responseCode = "200", 
				description = ApiDocConstants.SEARCH_SUCCESS, 
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = BudgetResponse.class))}),
		@ApiResponse(responseCode = "400", description = ApiDocConstants.BALANCE_GET_BUDGET_RESPONSE_400)
	})
	public BasicResponse getBudget(@Valid @RequestBody GetBudgetByBalanceIdRequest req) 
	{
		return service.getBudget(req);
	}
	
	@PostMapping(value = "getBudgetByAccount")
	@Operation(
		    summary = ApiDocConstants.BALANCE_GET_BUDGET_BY_ACCOUNT_SUMMARY,
		    description = ApiDocConstants.BALANCE_GET_BUDGET_BY_ACCOUNT_DESC,
		    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
		        description = ApiDocConstants.BALANCE_GET_BUDGET_BY_ACCOUNT_REQUEST_BODY_RULE
		    )
		)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", 
				description = ApiDocConstants.SEARCH_SUCCESS, 
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = BudgetListResponse.class))}),
		@ApiResponse(responseCode = "404", description = ApiDocConstants.ACCOUNT_NOT_FOUND),
	    @ApiResponse(responseCode = "404", description = ApiDocConstants.BALANCE_NOT_FOUND)
	})
	public BasicResponse getBudgetByAccount(@Valid @RequestBody AccountWithDateFilterRequest req) 
	{
		return service.getBudgetByAccount(req);
	}
	
	@PostMapping(value = "getAllByAccount")
	@Operation(
		    summary = ApiDocConstants.BALANCE_GET_ALL_BY_ACCOUNT_SUMMARY,
		    description = ApiDocConstants.BALANCE_GET_ALL_BY_ACCOUNT_DESC,
		    parameters = {
		        @Parameter(name = "account", description = "欲查詢的帳號，不能為空")
		    }
		)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", 
				description = ApiDocConstants.SEARCH_SUCCESS, 
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = BalanceListResponse.class))}),
		@ApiResponse(responseCode = "404", description = ApiDocConstants.ACCOUNT_NOT_FOUND)
	})
	public BasicResponse getAllBalance(@RequestParam("account") String account) 
	{
		return service.getPersonalBalance(account);
	}
	
	@PostMapping(value = "getAllByFamily")
	@Operation(
		    summary = ApiDocConstants.BALANCE_GET_ALL_BY_FAMILY_SUMMARY,
		    description = ApiDocConstants.BALANCE_GET_ALL_BY_FAMILY_DESC,
		    parameters = {
		        @Parameter(name = "account", description = "欲查詢的帳號，不能為空")
		    }
		)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", 
					description = ApiDocConstants.SEARCH_SUCCESS, 
					content = {@Content(mediaType = "application/json", schema = @Schema(implementation = BalanceListResponse.class))}),
			@ApiResponse(responseCode = "404", description = ApiDocConstants.ACCOUNT_NOT_FOUND)
	})
	public BasicResponse getFamilyBalance(@RequestParam("account") String account) 
	{
		return service.getFamilyBalance(account);
	}
	
}
