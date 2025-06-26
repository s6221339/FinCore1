package com.example.FinCore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.FinCore.constants.ApiDocConstants;
import com.example.FinCore.service.itfc.PaymentService;
import com.example.FinCore.vo.request.AccountWithDateFilterRequest;
import com.example.FinCore.vo.request.CreatePaymentRequest;
import com.example.FinCore.vo.request.RecoveryPaymentRequest;
import com.example.FinCore.vo.request.StatisticsRequest;
import com.example.FinCore.vo.request.UpdatePaymentRequest;
import com.example.FinCore.vo.response.BasicResponse;
import com.example.FinCore.vo.response.SearchPaymentResponse;
import com.example.FinCore.vo.response.StatisticsResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(allowedHeaders = "*")
@RequestMapping(value = "finbook/payment/")
@Tag(name = "帳款 API", description = "具有各種操作帳款的方法")
public class PaymentController 
{
	
	@Autowired
	private PaymentService service;
	
	@PostMapping(value = "create")
	@Operation(
			summary = ApiDocConstants.PAYMENT_CREATE_SUMMARY, 
			description = ApiDocConstants.PAYMENT_CREATE_DESC
					+ ApiDocConstants.TEST_PASS, 
			method = "POST",
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "建立資料請求，規則："
					+ ApiDocConstants.PAYMENT_CREATE_REQUEST_BODY_RULE)
			)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = ApiDocConstants.CREATE_SUCCESS),
		@ApiResponse(responseCode = "404", description = ApiDocConstants.BALANCE_NOT_FOUND),
	})
	public BasicResponse create(@Valid @RequestBody CreatePaymentRequest req) 
			throws Exception
	{
		return service.create(req);
	}
	
	
	@PostMapping(value = "delete")
	@Operation(
			summary = ApiDocConstants.PAYMENT_DELETE_SUMMARY, 
			description = ApiDocConstants.PAYMENT_DELETE_DESC
					+ ApiDocConstants.TEST_PASS, 
			method = "POST",
			parameters = {@Parameter(name = "paymentId", description = "款項編號")}
			)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = ApiDocConstants.DELETE_SUCCESS),
		@ApiResponse(responseCode = "404", description = ApiDocConstants.PAYMENT_NOT_FOUND),
		@ApiResponse(responseCode = "400", description = ApiDocConstants.PAYMENT_HAS_BEEN_DELETED),
	})
	public BasicResponse delete(@RequestParam("paymentId") int paymentId)
	{
		return service.delete(paymentId);
	}
	
	@PostMapping(value = "update")
	@Operation(
			summary = ApiDocConstants.PAYMENT_UPDATE_SUMMARY, 
			description = ApiDocConstants.PAYMENT_UPDATE_DESC
					+ ApiDocConstants.TEST_PASS, 
			method = "POST",
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "建立資料請求，規則："
					+ ApiDocConstants.PAYMENT_UPDATE_REQUEST_BODY_RULE)
			)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = ApiDocConstants.UPDATE_SUCCESS),
		@ApiResponse(responseCode = "404", description = ApiDocConstants.PAYMENT_NOT_FOUND),
		@ApiResponse(responseCode = "400", description = ApiDocConstants.PAYMENT_UPDATE_RESPONSE_400),
	})
	public BasicResponse update(@Valid @RequestBody UpdatePaymentRequest req) 
			throws Exception 
	{
		return service.update(req);
	}
	
	@PostMapping(value = "getInfoByAccount")
	@Operation(
			summary = ApiDocConstants.PAYMENT_GET_INFO_BY_ACCOUNT_SUMMARY, 
			description = ApiDocConstants.PAYMENT_GET_INFO_BY_ACCOUNT_DESC
					+ ApiDocConstants.TEST_PASS, 
			method = "POST",
			parameters = {@Parameter(name = "paymentId", description = "款項編號")}
			)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = ApiDocConstants.DELETE_SUCCESS),
		@ApiResponse(responseCode = "404", description = ApiDocConstants.PAYMENT_NOT_FOUND),
	})
	public SearchPaymentResponse getPaymentInfoByAccount(@RequestParam("account") String account) 
	{
		return service.getPaymentInfoByAccount(account);
	}
	
	@PostMapping(value = "getInfoByAccountWithDateFilter")
	@Operation(
			summary = ApiDocConstants.PAYMENT_GET_INFO_BY_ACCOUNT_WITH_DATE_FILTER_SUMMARY,
			description = ApiDocConstants.PAYMENT_GET_INFO_BY_ACCOUNT_WITH_DATE_FILTER_DESC + ApiDocConstants.TEST_PASS,
			method = "POST",
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
				description = "查詢付款資訊的請求資料，規則：" + ApiDocConstants.PAYMENT_ACCOUNT_WITH_DATE_FILTER_REQUEST_BODY_RULE
			)
		)
		@ApiResponses({
			@ApiResponse(responseCode = "200", description = ApiDocConstants.SEARCH_SUCCESS),
			@ApiResponse(responseCode = "404", description = ApiDocConstants.ACCOUNT_NOT_FOUND)
		})
	public SearchPaymentResponse getPaymentInfoWithDateFilter(@Valid @RequestBody AccountWithDateFilterRequest req) 
	{
		return service.getPaymentInfoByAccountWithDateFilter(req);
	}
	
	@PostMapping(value = "getInfoOfFamily")
	@Operation(
			summary = ApiDocConstants.PAYMENT_GET_FAMILY_INFO_SUMMARY,
			description = ApiDocConstants.PAYMENT_GET_FAMILY_INFO_DESC,
			method = "POST",
			parameters = {
				@Parameter(
					name = "account",
					description = ApiDocConstants.PARAM_ACCOUNT_DESC
				)
			}
		)
	public SearchPaymentResponse getPaymentInfoOfFamily(String account) 
	{
		return service.getPaymentInfoOfFamily(account);
	}
	
	@PostMapping(value = "getInfoOfFamilyWithDateFilter")
	@Operation(
			summary = ApiDocConstants.PAYMENT_GET_FAMILY_INFO_WITH_DATE_FILTER_SUMMARY,
			description = ApiDocConstants.PAYMENT_GET_FAMILY_INFO_WITH_DATE_FILTER_DESC,
			method = "POST",
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
					description = ApiDocConstants.PAYMENT_GET_FAMILY_INFO_WITH_DATE_FILTER_REQUEST_RULE
					)
		)
	public SearchPaymentResponse getPaymentInfoOfFamilyWithDateFilter(AccountWithDateFilterRequest req)
	{
		return service.getPaymentInfoOfFamilyWithDateFilter(req);
	}
	
	@PostMapping(value = "recovery")
	@Operation(
			summary = ApiDocConstants.PAYMENT_RECOVERY_SUMMARY,
			description = ApiDocConstants.PAYMENT_RECOVERY_DESC + ApiDocConstants.TEST_PASS,
			method = "POST",
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
				description = "復原款項的請求資料，規則：" + ApiDocConstants.PAYMENT_RECOVERY_REQUEST_BODY_RULE
			)
		)
		@ApiResponses({
			@ApiResponse(responseCode = "200", description = ApiDocConstants.UPDATE_SUCCESS),
			@ApiResponse(responseCode = "404", description = ApiDocConstants.PAYMENT_RECOVERY_RESPONSE_404)
		})
	public BasicResponse recovery(@Valid @RequestBody RecoveryPaymentRequest req) 
	{
		return service.recovery(req);
	}
	
	@PostMapping(value = "trashCan")
	@Operation(
			summary = ApiDocConstants.PAYMENT_TRASH_CAN_SUMMARY,
			description = ApiDocConstants.PAYMENT_TRASH_CAN_DESC + ApiDocConstants.TEST_PASS,
			method = "POST",
			parameters = {
				@Parameter(name = "account", description = "使用者帳號，必填")
			}
		)
		@ApiResponses({
			@ApiResponse(responseCode = "200", description = ApiDocConstants.SEARCH_SUCCESS),
			@ApiResponse(responseCode = "404", description = ApiDocConstants.ACCOUNT_NOT_FOUND)
		})
	public SearchPaymentResponse getDeletedPayment(@RequestParam("account") String account) 
	{
		return service.getDeletedPayment(account);
	}
	
	@PostMapping(value = "statistics")
	@Operation(
			summary = ApiDocConstants.PAYMENT_STATISTICS_SUMMARY,
			description = ApiDocConstants.PAYMENT_STATISTICS_DESC + ApiDocConstants.TEST_PASS,
			method = "POST",
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
				description = "查詢統計資料的請求格式，規則：" + ApiDocConstants.PAYMENT_STATISTICS_REQUEST_BODY_RULE
			)
		)
		@ApiResponses({
			@ApiResponse(responseCode = "200", description = ApiDocConstants.SEARCH_SUCCESS),
			@ApiResponse(responseCode = "404", description = ApiDocConstants.ACCOUNT_NOT_FOUND)
		})
	public StatisticsResponse statistics(@Valid @RequestBody StatisticsRequest req) 
	{
		return service.statistics(req);
	}
	
	@PostMapping(value = "disable/scheduledCreate")
	@Operation(
			summary = ApiDocConstants.PAYMENT_SCHEDULED_CREATE_SUMMARY,
			description = ApiDocConstants.PAYMENT_SCHEDULED_CREATE_DESC + ApiDocConstants.INTERNAL_ONLY,
			method = "POST",
			hidden = true
		)
	public final void scheduledCreate() throws Exception
	{
		throw new UnsupportedOperationException("此 API 無法被呼叫！");
	}
	
	@PostMapping(value = "disable/scheduledDelete")
	@Operation(
			summary = ApiDocConstants.PAYMENT_SCHEDULED_DELETE_SUMMARY,
			description = ApiDocConstants.PAYMENT_SCHEDULED_DELETE_DESC + ApiDocConstants.INTERNAL_ONLY,
			method = "POST",
			hidden = true
		)
	public final void scheduledDelete() throws Exception
	{
		throw new UnsupportedOperationException("此 API 無法被呼叫！");
	}
	
}
