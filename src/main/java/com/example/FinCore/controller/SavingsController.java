package com.example.FinCore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.FinCore.constants.ApiDocConstants;
import com.example.FinCore.service.itfc.SavingsService;
import com.example.FinCore.vo.response.SavingsListResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin(allowedHeaders = "*")
@RequestMapping(value = "finbook/savings/")
@Tag(name = "儲蓄 API", description = "與帳號相關的儲蓄設定功能")
public class SavingsController 
{
	
	@Autowired
	private SavingsService service;
	
	@PostMapping(value = "getAll")
	@Operation(
			summary = ApiDocConstants.SAVINGS_GET_ALL_SUMMARY, 
			description = ApiDocConstants.SAVINGS_GET_ALL_DESC
					+ ApiDocConstants.TEST_PASS, 
			method = "POST",
			parameters = {@Parameter(name = "account", description = "帳號")}
			)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = ApiDocConstants.SEARCH_SUCCESS + "：取得所有項目"),
		@ApiResponse(responseCode = "404", description = ApiDocConstants.ACCOUNT_NOT_FOUND),
	})
	public SavingsListResponse getAll(@RequestParam("account") String account) 
	{
		return service.getAll(account);
	}
	
}
