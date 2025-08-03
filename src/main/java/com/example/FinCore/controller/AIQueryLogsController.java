package com.example.FinCore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.FinCore.annotation.TestAPI;
import com.example.FinCore.constants.ApiDocConstants;
import com.example.FinCore.constants.ConstantsMessage;
import com.example.FinCore.service.itfc.AIQueryLogsService;
import com.example.FinCore.vo.request.AICallRequest;
import com.example.FinCore.vo.request.AICreateRequest;
import com.example.FinCore.vo.response.AICallbackResponse;
import com.example.FinCore.vo.response.BasicResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(allowedHeaders = "*")
@RequestMapping(value = "finbook/aiQueryLogs/")
@Tag(name = "AI分析 API", description = "終極黑魔法！可呼叫 AI 的 API")
public class AIQueryLogsController 
{
	
	@Autowired
	private AIQueryLogsService service;
	
	@TestAPI
	@PostMapping(value = "create")
    @Operation(
            summary = ApiDocConstants.AI_QUERY_LOGS_CREATE_SUMMARY,
            description = ApiDocConstants.AI_QUERY_LOGS_CREATE_DESC,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = ApiDocConstants.AI_QUERY_LOGS_CREATE_REQUEST_BODY_RULE,
                required = true
            ),
            responses = {
                @ApiResponse(responseCode = "200", description = ApiDocConstants.CREATE_SUCCESS)
            }
        )
	public BasicResponse create(@Valid @RequestBody AICreateRequest req) throws Exception 
	{
		return service.create(req);
	}
	
	@TestAPI
	@PostMapping(value = "call")
	@Operation(
			summary = ApiDocConstants.AI_QUERY_LOGS_CALL_SUMMARY,
			description = ApiDocConstants.AI_QUERY_LOGS_CALL_DESC
		)
	@ApiResponses({
		@ApiResponse(responseCode = "200", 
				description = ApiDocConstants.ANALYSIS_SUCCESS, 
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AICallbackResponse.class))}),
		@ApiResponse(responseCode = "400", description = ApiDocConstants.AI_QUERY_LOGS_CALL_RESPONSE_400),
		@ApiResponse(responseCode = "404", description = ApiDocConstants.AI_QUERY_LOGS_CALL_RESPONSE_404)
	})
	public BasicResponse call(@Valid @RequestBody AICallRequest req) throws Exception
	{
		return service.call(req);
	}
	
	@PostMapping(value = "disable/analysis")
	@Operation(
		    summary = ApiDocConstants.AI_QUERY_LOGS_ANALYSIS_SUMMARY,
		    description = ApiDocConstants.AI_QUERY_LOGS_ANALYSIS_DESC,
		    responses = {
		        @ApiResponse(responseCode = "500", description = ApiDocConstants.INTERNAL_ONLY)
		    }
		)
	public final void analysis() throws Exception
	{
		throw new UnsupportedOperationException(ConstantsMessage.API_NOT_ALLOWED);
	}
	
}
