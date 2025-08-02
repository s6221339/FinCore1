package com.example.FinCore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.FinCore.constants.ApiDocConstants;
import com.example.FinCore.constants.ConstantsMessage;
import com.example.FinCore.service.itfc.PaymentService;
import com.example.FinCore.vo.request.AccountWithDateFilterRequest;
import com.example.FinCore.vo.request.CreatePaymentRequest;
import com.example.FinCore.vo.request.RecoveryPaymentRequest;
import com.example.FinCore.vo.request.StatisticsRequest;
import com.example.FinCore.vo.request.UpdatePaymentRequest;
import com.example.FinCore.vo.response.BasicResponse;
import com.example.FinCore.vo.response.SearchPaymentResponse;
import com.example.FinCore.vo.response.StatisticsIncomeAndOutlayResponse;
import com.example.FinCore.vo.response.StatisticsIncomeAndOutlayWithBalanceInfoResponse;
import com.example.FinCore.vo.response.StatisticsLookupPaymentTypeWithAllBalanceResponse;
import com.example.FinCore.vo.response.StatisticsPaymentDetailsSummarizeResponse;
import com.example.FinCore.vo.response.StatisticsPaymentDetailsWithBalanceResponse;
import com.example.FinCore.vo.response.StatisticsPersonalBalanceWithPaymentTypeResponse;

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
		@ApiResponse(responseCode = "200", 
				description = ApiDocConstants.SEARCH_SUCCESS, 
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = StatisticsLookupPaymentTypeWithAllBalanceResponse.class))}),
		@ApiResponse(responseCode = "404", description = ApiDocConstants.PAYMENT_NOT_FOUND),
		@ApiResponse(responseCode = "403", description = ApiDocConstants.NO_PAYMENT_MODIFING_PERMISSION),
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
		@ApiResponse(responseCode = "200", 
				description = ApiDocConstants.SEARCH_SUCCESS, 
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = SearchPaymentResponse.class))}),
		@ApiResponse(responseCode = "404", description = ApiDocConstants.PAYMENT_NOT_FOUND),
	})
	public BasicResponse getPaymentInfoByAccount(@RequestParam("account") String account) 
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
			@ApiResponse(responseCode = "200", 
					description = ApiDocConstants.SEARCH_SUCCESS, 
					content = {@Content(mediaType = "application/json", schema = @Schema(implementation = SearchPaymentResponse.class))}),
			@ApiResponse(responseCode = "404", description = ApiDocConstants.ACCOUNT_NOT_FOUND)
		})
	public BasicResponse getPaymentInfoWithDateFilter(@Valid @RequestBody AccountWithDateFilterRequest req) 
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
	@ApiResponses({
		@ApiResponse(responseCode = "200", 
				description = ApiDocConstants.SEARCH_SUCCESS, 
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = SearchPaymentResponse.class))}),
		@ApiResponse(responseCode = "404", description = ApiDocConstants.ACCOUNT_NOT_FOUND)
	})
	public BasicResponse getPaymentInfoOfFamily(@RequestParam("account") String account) 
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
	@ApiResponses({
		@ApiResponse(responseCode = "200", 
				description = ApiDocConstants.SEARCH_SUCCESS, 
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = SearchPaymentResponse.class))}),
		@ApiResponse(responseCode = "404", description = ApiDocConstants.ACCOUNT_NOT_FOUND)
	})
	public BasicResponse getPaymentInfoOfFamilyWithDateFilter(@Valid @RequestBody AccountWithDateFilterRequest req)
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
			@ApiResponse(responseCode = "200", 
					description = ApiDocConstants.SEARCH_SUCCESS, 
					content = {@Content(mediaType = "application/json", schema = @Schema(implementation = SearchPaymentResponse.class))}),
			@ApiResponse(responseCode = "404", description = ApiDocConstants.ACCOUNT_NOT_FOUND)
		})
	public BasicResponse getDeletedPayment(@RequestParam("account") String account) 
	{
		return service.getDeletedPayment(account);
	}
	
	@PostMapping(value = "statistics/paymentTypeWithAllBalance")
	@Operation(
			summary = ApiDocConstants.PAYMENT_STATISTICS_LOOKUP_ALL_BALANCE_SUMMARY,
			description = ApiDocConstants.PAYMENT_STATISTICS_LOOKUP_ALL_BALANCE_DESC + ApiDocConstants.TEST_PASS,
			method = "POST",
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
				description = "查詢統計資料的請求格式，規則：" + ApiDocConstants.PAYMENT_STATISTICS_REQUEST_BODY_RULE
			)
		)
		@ApiResponses({
			@ApiResponse(responseCode = "200", 
					description = ApiDocConstants.SEARCH_SUCCESS, 
					content = {@Content(mediaType = "application/json", schema = @Schema(implementation = StatisticsLookupPaymentTypeWithAllBalanceResponse.class))}),
			@ApiResponse(responseCode = "404", description = ApiDocConstants.ACCOUNT_NOT_FOUND)
		})
	public BasicResponse statisticsLookupPaymentTypeWithAllBalance(@Valid @RequestBody StatisticsRequest req) 
	{
		return service.statisticsLookupPaymentTypeWithAllBalance(req);
	}
	
	@PostMapping(value = "statistics/summaryIncomeAndOutlay")
	@Operation(
		    summary = ApiDocConstants.PAYMENT_STATISTICS_SUMMARY_INCOME_AND_OUTLAY_SUMMARY,
		    description = ApiDocConstants.PAYMENT_STATISTICS_SUMMARY_INCOME_AND_OUTLAY_DESC,
		    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
		        description = ApiDocConstants.PAYMENT_STATISTICS_REQUEST_BODY_RULE,
		        required = true
		    )
		)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", 
					description = ApiDocConstants.SEARCH_SUCCESS, 
					content = {@Content(mediaType = "application/json", schema = @Schema(implementation = StatisticsIncomeAndOutlayResponse.class))}),
			@ApiResponse(responseCode = "404", description = ApiDocConstants.ACCOUNT_NOT_FOUND)
	})
	public BasicResponse statisticsIncomeAndOutlaySummarize(@Valid @RequestBody StatisticsRequest req) 
	{
		return service.statisticsIncomeAndOutlaySummarize(req);
	}
	
	@PostMapping(value = "statistics/personalBalanceWithPaymentType")
	@Operation(
		    summary = ApiDocConstants.PAYMENT_STATISTICS_PERSONAL_BALANCE_WITH_PAYMENT_TYPE_SUMMARY,
		    description = ApiDocConstants.PAYMENT_STATISTICS_PERSONAL_BALANCE_WITH_PAYMENT_TYPE_DESC,
		    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
		        description = ApiDocConstants.PAYMENT_STATISTICS_REQUEST_BODY_RULE,
		        required = true
		    )
		)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", 
					description = ApiDocConstants.SEARCH_SUCCESS, 
					content = {@Content(mediaType = "application/json", schema = @Schema(implementation = StatisticsPersonalBalanceWithPaymentTypeResponse.class))}),
			@ApiResponse(responseCode = "404", description = ApiDocConstants.ACCOUNT_NOT_FOUND)
	})
	public BasicResponse statisticsLookupPaymentTypePersonalBalance(@Valid @RequestBody StatisticsRequest req)
	{
		return service.statisticsLookupPaymentTypeSummarize(req);
	}
	
	@PostMapping(value = "statistics/incomeAndOutlayWithAllBalance")
	@Operation(
		    summary = ApiDocConstants.PAYMENT_STATISTICS_INCOME_AND_OUTLAY_WITH_ALL_BALANCE_SUMMARY,
		    description = ApiDocConstants.PAYMENT_STATISTICS_INCOME_AND_OUTLAY_WITH_ALL_BALANCE_DESC,
		    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
		        description = ApiDocConstants.PAYMENT_STATISTICS_REQUEST_BODY_RULE,
		        required = true
		    )
		)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", 
					description = ApiDocConstants.SEARCH_SUCCESS, 
					content = {@Content(mediaType = "application/json", schema = @Schema(implementation = StatisticsIncomeAndOutlayWithBalanceInfoResponse.class))}),
			@ApiResponse(responseCode = "404", description = ApiDocConstants.ACCOUNT_NOT_FOUND)
	})
	public BasicResponse statisticsIncomeAndOutlayWithAllBalance(@Valid @RequestBody StatisticsRequest req) 
	{
		return service.statisticsIncomeAndOutlayWithAllBalance(req);
	}
	
	@PostMapping(value = "statistics/incomeDetailsWithAllBalance")
	@Operation(
		    summary = ApiDocConstants.PAYMENT_STATISTICS_INCOME_DETAILS_WITH_ALL_BALANCE_SUMMARY,
		    description = ApiDocConstants.PAYMENT_STATISTICS_INCOME_DETAILS_WITH_ALL_BALANCE_DESC,
		    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
		        description = ApiDocConstants.PAYMENT_STATISTICS_REQUEST_BODY_RULE,
		        required = true
		    )
		)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", 
					description = ApiDocConstants.SEARCH_SUCCESS, 
					content = {@Content(mediaType = "application/json", schema = @Schema(implementation = StatisticsPaymentDetailsWithBalanceResponse.class))}),
			@ApiResponse(responseCode = "404", description = ApiDocConstants.ACCOUNT_NOT_FOUND)
	})
	public BasicResponse statisticsIncomeDetailsWithAllBalance(@Valid @RequestBody StatisticsRequest req) 
	{
		return service.statisticsIncomeDetailsWithAllBalance(req);
	}
	
	@PostMapping(value = "statistics/incomeDetailsSummarize")
	@Operation(
		    summary = ApiDocConstants.PAYMENT_STATISTICS_INCOME_DETAILS_SUMMARIZE_SUMMARY,
		    description = ApiDocConstants.PAYMENT_STATISTICS_INCOME_DETAILS_SUMMARIZE_DESC,
		    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
		        description = ApiDocConstants.PAYMENT_STATISTICS_REQUEST_BODY_RULE,
		        required = true
		    )
		)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", 
					description = ApiDocConstants.SEARCH_SUCCESS, 
					content = {@Content(mediaType = "application/json", schema = @Schema(implementation = StatisticsPaymentDetailsSummarizeResponse.class))}),
			@ApiResponse(responseCode = "404", description = ApiDocConstants.ACCOUNT_NOT_FOUND)
	})
	public BasicResponse statisticsIncomeDetailsSummarize(@Valid @RequestBody StatisticsRequest req) 
	{
		return service.statisticsIncomeDetailsSummarize(req);
	}
	
	@PostMapping(value = "disable/scheduledCreate")
	@Operation(
			summary = ApiDocConstants.PAYMENT_SCHEDULED_CREATE_SUMMARY,
			description = ApiDocConstants.PAYMENT_SCHEDULED_CREATE_DESC,
			method = "POST",
			hidden = true,
			responses = {
					@ApiResponse(responseCode = "500", description = ApiDocConstants.INTERNAL_ONLY)
			}
		)
	public final void scheduledCreate() throws Exception
	{
		throw new UnsupportedOperationException(ConstantsMessage.API_NOT_ALLOWED);
	}
	
	@PostMapping(value = "disable/scheduledDelete")
	@Operation(
			summary = ApiDocConstants.PAYMENT_SCHEDULED_DELETE_SUMMARY,
			description = ApiDocConstants.PAYMENT_SCHEDULED_DELETE_DESC,
			method = "POST",
			hidden = true,
			responses = {
					@ApiResponse(responseCode = "500", description = ApiDocConstants.INTERNAL_ONLY)
			}
		)
	public final void scheduledDelete() throws Exception
	{
		throw new UnsupportedOperationException(ConstantsMessage.API_NOT_ALLOWED);
	}
	
}
