package com.example.FinCore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.FinCore.constants.ApiDocConstants;
import com.example.FinCore.service.itfc.TransfersService;
import com.example.FinCore.vo.request.CreateTransfersRequest;
import com.example.FinCore.vo.response.BasicResponse;
import com.example.FinCore.vo.response.TransfersListResponse;

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
@RequestMapping(value = "finbook/transfers/")
@Tag(name = "轉帳紀錄 API", description = "提供操作轉帳紀錄的方法。")
public class TransfersController 
{
	
	@Autowired
	private TransfersService service;
	
	@PostMapping(value = "create")
	@Operation(
			summary = ApiDocConstants.TRANSFERS_CREATE_SUMMARY, 
			description = ApiDocConstants.TRANSFERS_CREATE_DESC
					+ ApiDocConstants.TEST_PASS, 
			method = "POST",
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "建立資料請求，規則："
					+ ApiDocConstants.TRANSFERS_CREATE_REQUEST_BODY_RULE)
			)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = ApiDocConstants.CREATE_SUCCESS),
		@ApiResponse(responseCode = "400", description = ApiDocConstants.TRANSFERS_CREATE_RESPONSE_400),
		@ApiResponse(responseCode = "404", description = ApiDocConstants.TRANSFERS_CREATE_RESPONSE_404)
	})
	public BasicResponse create(@Valid @RequestBody CreateTransfersRequest req)
			throws Exception
	{
		return service.create(req);
	}
	
	@PostMapping(value = "delete")
	@Operation(
			summary = ApiDocConstants.TRANSFERS_DELETE_SUMMARY, 
			description = ApiDocConstants.TRANSFERS_DELETE_DESC
					+ ApiDocConstants.TEST_PASS, 
			method = "POST",
			parameters = {
					@Parameter(name = "account", description = "帳號"),
					@Parameter(name = "id", description = "要刪除的紀錄編號")
					}
			)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = ApiDocConstants.DELETE_SUCCESS),
		@ApiResponse(responseCode = "404", description = ApiDocConstants.TRANSFERS_DELETE_RESPONSE_404),
		@ApiResponse(responseCode = "403", description = ApiDocConstants.NOT_SUPER_ADMIN),
	})
	public BasicResponse delete(
			@RequestParam("account") String account, 
			@RequestParam("id") int id) 
	{
		return service.delete(account, id);
	}
	
	@PostMapping(value = "deleteByBalanceId")
	@Operation(
			summary = ApiDocConstants.TRANSFERS_DELETE_BY_BALANCE_ID_SUMMARY, 
			description = ApiDocConstants.TRANSFERS_DELETE_BY_BALANCE_ID_SUMMARY
					+ ApiDocConstants.TEST_PASS, 
			method = "POST",
			parameters = {
					@Parameter(name = "from", description = "轉出的帳戶編號"),
					@Parameter(name = "to", description = "匯入的帳戶編號")
					}
			)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = ApiDocConstants.DELETE_SUCCESS),
		@ApiResponse(responseCode = "400", description = ApiDocConstants.TRANSFERS_DELETE_BY_BALANCE_ID_RESPONSE_400)
	})
	public BasicResponse deleteByBalanceId(
			@RequestParam("from") int from, 
			@RequestParam("to") int to) 
	{
		return service.deleteByBalanceId(from, to);
	}
	
	@PostMapping(value = "getAll")
	@Operation(
			summary = ApiDocConstants.TRANSFERS_GET_ALL_BY_BALANCE_ID_SUMMARY, 
			description = ApiDocConstants.TRANSFERS_GET_ALL_BY_BALANCE_ID_DESC
					+ ApiDocConstants.TEST_PASS, 
			method = "POST",
			parameters = {@Parameter(name = "balanceId", description = "帳戶編號")}
			)
	@ApiResponses({
		@ApiResponse(responseCode = "200", 
				description = ApiDocConstants.SEARCH_SUCCESS, 
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = TransfersListResponse.class))}),
		@ApiResponse(responseCode = "404", description = ApiDocConstants.BALANCE_NOT_FOUND)
	})
	public BasicResponse getAllByBalanceId(@RequestParam("balanceId") int balanceId) 
	{
		return service.getAllByBalanceId(balanceId);
	}
	
	@PostMapping(value = "confirm")
	@Operation(
			summary = ApiDocConstants.TRANSFERS_CONFIRM_SUMMARY,
			description = ApiDocConstants.TRANSFERS_CONFIRM_DESC,
			parameters = {
				@Parameter(name = "tId", description = "轉帳紀錄 ID"),
				@Parameter(name = "bId", description = "目標帳戶 ID")
			}
		)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = ApiDocConstants.UPDATE_SUCCESS),
		@ApiResponse(responseCode = "400", description = ApiDocConstants.TRANSFERS_CONFIRM_RESPONSE_400),
		@ApiResponse(responseCode = "403", description = ApiDocConstants.FORBIDDEN),
		@ApiResponse(responseCode = "404", description = ApiDocConstants.TRANSFERS_CONFIRM_RESPONSE_404)
	})
	public BasicResponse confirm(@RequestParam("tId") int transfersId, @RequestParam("bId") int balanceId)
	{
		return service.confirm(transfersId, balanceId);
	}
	
	@PostMapping(value = "getNotConfirm")
	@Operation(
			summary = ApiDocConstants.TRANSFERS_GET_NOT_CONFIRM_SUMMARY,
			description = ApiDocConstants.TRANSFERS_GET_NOT_CONFIRM_DESC
		)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = ApiDocConstants.SEARCH_SUCCESS,
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransfersListResponse.class))
		),
		@ApiResponse(responseCode = "400", description = ApiDocConstants.PLEASE_LOGIN_FIRST)
	})
	public BasicResponse getNotConfirmTransfers()
	{
		return service.getNotConfirmTransfers();
	}
	
	@PostMapping(value = "retract")
	@Operation(
			summary = ApiDocConstants.TRANSFERS_RETRACT_SUMMARY,
			description = ApiDocConstants.TRANSFERS_RETRACT_DESC,
			parameters = {
				@Parameter(name = "tId", description = "指定轉帳紀錄 ID")
			},
			responses = {
				@ApiResponse(
					responseCode = "200",
					description = ApiDocConstants.DELETE_SUCCESS,
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = BasicResponse.class))
				),
				@ApiResponse(responseCode = "400", description = ApiDocConstants.TRANSFERS_RETRACT_RESPONSE_400),
				@ApiResponse(responseCode = "403", description = ApiDocConstants.FORBIDDEN),
				@ApiResponse(responseCode = "404", description = ApiDocConstants.TRANSFERS_NOT_FOUND)
			}
		)
	public BasicResponse retract(@RequestParam("tId") int transfersId)
	{
		return service.retract(transfersId);
	}
	
}
