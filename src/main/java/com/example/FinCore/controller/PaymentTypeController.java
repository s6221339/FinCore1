package com.example.FinCore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.FinCore.constants.ApiDocConstants;
import com.example.FinCore.service.itfc.PaymentTypeService;
import com.example.FinCore.vo.request.CreatePaymentTypeRequest;
import com.example.FinCore.vo.response.BasicResponse;
import com.example.FinCore.vo.response.GetPaymentTypeListResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(allowedHeaders = "*")
@RequestMapping(value = "finbook/paymentType/")
@Tag(name = "記帳類型與細項 API", description = "可以自定義類型與細項的API。")
public class PaymentTypeController 
{
	
	@Autowired
	private PaymentTypeService service;
	
	@PostMapping(value = "create")
	@Operation(
			summary = "建立新項目", 
			description = "建立一筆新的分類與細項，不允許使用者新增完全重複的類型與項目。<br>"
					+ ApiDocConstants.TEST_PASS, 
			method = "POST",
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "建立資料請求，規則："
					+ ApiDocConstants.PAYMENT_TYPE_CREATE_REQUEST_BODY_RULE)
			)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = ApiDocConstants.CREATE_SUCCESS),
		@ApiResponse(responseCode = "400", description = ApiDocConstants.ITEM_EXISTED),
		@ApiResponse(responseCode = "404", description = ApiDocConstants.ACCOUNT_NOT_EXIST)
	})
	public BasicResponse createType(@Valid @RequestBody CreatePaymentTypeRequest req)
	{
		return service.createType(req);
	}
	
	@PostMapping(value = "getByAccount")
	@Operation(
			summary = "取得帳號創建的所有類型與項目資料", 
			description = "取得帳號創建的所有類型與項目資料，其中包含了預設的資料。該操作必定成功<del>（大不了就沒自己ㄉ東東）</del><br>"
					+ ApiDocConstants.TEST_PASS, 
			method = "POST",
			parameters = {@Parameter(name = "account", description = "帳號")}
			)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = ApiDocConstants.SEARCH_SUCCESS + "：取得所有項目"),
	})
	public GetPaymentTypeListResponse getTypeByAccount(@RequestParam("account") String account)
	{
		return service.getTypeByAccount(account);
	}
	
}
