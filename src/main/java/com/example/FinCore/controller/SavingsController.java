package com.example.FinCore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.FinCore.constants.ApiDocConstants;
import com.example.FinCore.service.itfc.SavingsService;
import com.example.FinCore.vo.response.BasicResponse;
import com.example.FinCore.vo.response.SavingsListResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
		@ApiResponse(responseCode = "200", 
				description = ApiDocConstants.SEARCH_SUCCESS, 
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = SavingsListResponse.class))}),
		@ApiResponse(responseCode = "404", description = ApiDocConstants.ACCOUNT_NOT_FOUND),
	})
	public BasicResponse getAll(@RequestParam("account") String account) 
	{
		return service.getAll(account);
	}
	
}
