package com.example.FinCore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.FinCore.annotation.TODO;
import com.example.FinCore.constants.ApiDocConstants;
import com.example.FinCore.constants.TodoPriority;
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
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(allowedHeaders = "*")
@RequestMapping(value = "finbook/balance/")
@Tag(name = "帳戶 API", description = "具有操作帳戶的魔法")
@TODO(value = "撰寫 API 文件", priority = TodoPriority.NOT_REQUIRED)
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
	public BudgetResponse getBudget(@Valid @RequestBody GetBudgetByBalanceIdRequest req) 
	{
		return service.getBudget(req);
	}
	
	@PostMapping(value = "getBudgetByAccount")
	public BudgetListResponse getBudgetByAccount(@Valid @RequestBody AccountWithDateFilterRequest req) 
	{
		return service.getBudgetByAccount(req);
	}
	
	@PostMapping(value = "getAllByAccount")
	public BalanceListResponse getAllBalance(@RequestParam("account") String account) 
	{
		return service.getPersonalBalance(account);
	}
	
	@PostMapping(value = "getAllByFamily")
	public BalanceListResponse getFamilyBalance(@RequestParam("account") String account) 
	{
		return service.getFamilyBalance(account);
	}
	
}
