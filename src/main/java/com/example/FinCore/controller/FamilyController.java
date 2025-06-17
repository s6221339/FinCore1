package com.example.FinCore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.FinCore.constants.ApiDocConstants;
import com.example.FinCore.service.itfc.FamilyService;
import com.example.FinCore.vo.request.AcceptInviteRequest;
import com.example.FinCore.vo.request.CreateFamilyRequest;
import com.example.FinCore.vo.request.DismissFamilyRequest;
import com.example.FinCore.vo.request.InviteMemberRequest;
import com.example.FinCore.vo.request.KickMemberRequest;
import com.example.FinCore.vo.request.OwnerResignAndAssignRequest;
import com.example.FinCore.vo.request.QuitFamilyRequest;
import com.example.FinCore.vo.request.RenameFamilyRequest;
import com.example.FinCore.vo.request.UpdateFamilyRequest;
import com.example.FinCore.vo.response.BasicResponse;
import com.example.FinCore.vo.response.FamilyListResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Parameter;

@CrossOrigin
@RestController
@RequestMapping(value = "finbook/family/")
@Tag(name = "家庭群組 API", description = "提供家庭群組會使用到的方法")
public class FamilyController {
	
	@Autowired
	private FamilyService service;
	
//	@RequestBody：用在傳參是一個 class 的時候，例如 Request
//	@RequestParam：用在傳參為「基本資料型態」的時候，例如 int、String
//	@Valid：如果 RequestBody 中有資料驗證，則需要使用該註釋來啟動驗證功能
	@PostMapping(value = "create")
	@Operation(
		        summary = "建立家族",
		        description = "由指定 owner 建立新的家族，並可一次邀請多位成員<br>"
		        		+ ApiDocConstants.TEST_PASS, 
		        method = "POST",
		        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody //
		        (description = "建立請求資料，規則："
						+ ApiDocConstants.FAMILY_CREATE_REQUEST_BODY_RULE)
				)
		    @ApiResponses({
		        @ApiResponse(responseCode = "200", description = ApiDocConstants.CREATE_SUCCESS),
		        @ApiResponse(responseCode = "400", description = ApiDocConstants.FAMILY_REQUEST_BODY_RULE)
		    })
	public BasicResponse create(@Valid @RequestBody CreateFamilyRequest req) throws Exception {
		return service.create(req);
	}
	
	// update目前只有更新名稱的功能，變更名稱用 finbook/family/rename
//	@PostMapping(value = "update")
//	public BasicResponse update(@Valid @RequestBody UpdateFamilyRequest req) {
//		return service.update(req);
//	}
	
	// 解散家庭群族用 finbook/family/dismiss
//	@PostMapping(value = "delete")
//	public BasicResponse delete(@Valid @RequestBody DeleteFamilyRequest req) {
//		return service.delete(req);
//	}
	
	@PostMapping(value = "getFamilyById")
	@Operation(
	        summary = "查詢家族資訊",
	        description = "根據 familyId 查詢對應的家族資訊<br>"
	        		+ ApiDocConstants.TEST_PASS,
	        method = "POST",
	        parameters = {
	            @Parameter(name = "familyId", description = "要查詢的家族 ID")
	        }
	    )
	    @ApiResponses({
	        @ApiResponse(responseCode = "200", description = ApiDocConstants.SEARCH_SUCCESS),
	        @ApiResponse(responseCode = "404", description = ApiDocConstants.FAMILY_NOT_EXIST),
	    })
	public BasicResponse getFamilyById(@RequestParam("familyId") int familyId) {
		return service.getFamilyById(familyId);
	}
	
	@PostMapping(value = "listAllFamily")
	@Operation(
	        summary = "查詢所有家族",
	        description = "回傳系統中所有家族的清單（可用於管理後台）<br>"
	        		+ ApiDocConstants.TEST_PASS,
	        method = "POST"
	    )
	    @ApiResponses({
	        @ApiResponse(responseCode = "200", description = ApiDocConstants.SEARCH_SUCCESS)
	    })
	public FamilyListResponse listAllFamily() throws JsonProcessingException {
		return service.listAllFamily();
	}

	@PostMapping(value = "invite")
	@Operation(
		    summary = "邀請新成員加入家族",
		    description = "由家族 owner 邀請新成員加入家族，會將帳號加入家族的邀請名單 <br>"
		    		+ ApiDocConstants.TEST_PASS,
		    method = "POST",
		    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody //
		    (description = ApiDocConstants.FAMILY_REQUEST_BODY_RULE)
		)
		@ApiResponses({
		    @ApiResponse(responseCode = "200", description = ApiDocConstants.UPDATE_SUCCESS),
		    @ApiResponse(responseCode = "404", description = ApiDocConstants.FAMILY_NOT_EXIST)
		})
	public BasicResponse inviteMember(@Valid @RequestBody InviteMemberRequest req) //
	throws JsonProcessingException{
		return service.inviteMember(req);
	}
	
	@PostMapping(value = "dismiss")
	@Operation(
		    summary = "解散家族",
		    description = "由家族 owner 解散該家族，所有成員將退出<br>"
		    		+ ApiDocConstants.TEST_PASS,
		    method = "POST",
		    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody //
		    (description = ApiDocConstants.FAMILY_REQUEST_BODY_RULE)
		)
		@ApiResponses({
		    @ApiResponse(responseCode = "200", description = ApiDocConstants.DELETE_SUCCESS),
		    @ApiResponse(responseCode = "403", description = ApiDocConstants.NO_PERMISSION)
		})
	public BasicResponse dismissFamily(@Valid @RequestBody DismissFamilyRequest req) {
		return service.dismissFamily(req);
	}
	
	@PostMapping(value = "kick")
	@Operation(
		    summary = "踢出家族成員",
		    description = "由 owner 或管理者將指定帳號從家族中移除<br>"
		    		+ ApiDocConstants.TEST_PASS,
		    		method = "POST",
		    		requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody //
		    		(description = ApiDocConstants.FAMILY_REQUEST_BODY_RULE)
		)
		@ApiResponses({
		    @ApiResponse(responseCode = "200", description = ApiDocConstants.UPDATE_SUCCESS),
		    @ApiResponse(responseCode = "404", description = ApiDocConstants.FAMILY_NOT_EXIST)
		})
	public BasicResponse kickMember(@Valid @RequestBody KickMemberRequest req) {
		return service.kickMember(req);
	}
	
	@PostMapping(value = "ownerQuit")
	@Operation(
			summary = "轉讓家族管理者",
			description = "原本的 owner 退出，將管理權轉交給指定新帳號，如沒有指定將由群組第一人為新 owner <br>"
					+ ApiDocConstants.TEST_PASS,
					method = "POST",
					requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody //
					(description = ApiDocConstants.FAMILY_REQUEST_BODY_RULE)
			)
	@ApiResponses({
	    @ApiResponse(responseCode = "200", description = ApiDocConstants.UPDATE_SUCCESS),
	    @ApiResponse(responseCode = "403", description = ApiDocConstants.NO_PERMISSION)
	})
	public BasicResponse ownerQuit(@Valid @RequestBody OwnerResignAndAssignRequest req) {
		return service.ownerQuit(req);
	}
	
	@PostMapping(value = "quit")
	@Operation(
			summary = "成員退出家族",
			description = "非 owner 的一般成員可自行退出家族<br>"
					+ ApiDocConstants.TEST_PASS,
					method = "POST",
					requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody //
					(description = ApiDocConstants.FAMILY_REQUEST_BODY_RULE)
			)
	@ApiResponses({
	    @ApiResponse(responseCode = "200", description = ApiDocConstants.UPDATE_SUCCESS),
	    @ApiResponse(responseCode = "404", description = ApiDocConstants.FAMILY_NOT_EXIST)
	})
	public BasicResponse quitFamily(@Valid @RequestBody QuitFamilyRequest req) {
		return service.quitFamily(req);
	}
	
	@PostMapping(value = "rename")
	@Operation(
		    summary = "更改家族名稱",
		    description = "家族 owner 可更新家族的名稱<br>"
		    		+ ApiDocConstants.TEST_PASS,
		    method = "POST",
		    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody //
		    (description = ApiDocConstants.FAMILY_REQUEST_BODY_RULE)
		)
		@ApiResponses({
		    @ApiResponse(responseCode = "200", description = ApiDocConstants.UPDATE_SUCCESS),
		    @ApiResponse(responseCode = "403", description = ApiDocConstants.NO_PERMISSION)
		})
	public BasicResponse renameFamily(@Valid @RequestBody RenameFamilyRequest req) {
		return service.renameFamily(req);
	}
	
	//尚未測試，不一定會用到
//	@PostMapping(value = "acceptInvite")
//	@Operation(
//		    summary = "接受加入家族邀請",
//		    description = "被邀請人接受邀請後，將從邀請名單中移除並正式加入家族。",
//		    method = "POST",
//		    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody //
//		    (description = ApiDocConstants.FAMILY_REQUEST_BODY_RULE)
//		)
//		@ApiResponses({
//		    @ApiResponse(responseCode = "200", description = ApiDocConstants.UPDATE_SUCCESS),
//		    @ApiResponse(responseCode = "404", description = ApiDocConstants.FAMILY_NOT_EXIST)
//		})
//	public BasicResponse acceptInvite(@Valid @RequestBody AcceptInviteRequest req) throws JsonProcessingException {
//	    return service.acceptInvite(req);
//	}

	//尚未測試，不一定會用到
//	@PostMapping(value = "rejectInvite")
//	@Operation(
//		    summary = "拒絕加入家族邀請",
//		    description = "被邀請人拒絕邀請，將從邀請名單中移除。",
//		    method = "POST",
//		    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody //
//		    (description = ApiDocConstants.FAMILY_REQUEST_BODY_RULE)
//		)
//		@ApiResponses({
//		    @ApiResponse(responseCode = "200", description = ApiDocConstants.UPDATE_SUCCESS),
//		    @ApiResponse(responseCode = "404", description = ApiDocConstants.FAMILY_NOT_EXIST)
//		})
//	public BasicResponse rejectInvite(@Valid @RequestBody AcceptInviteRequest req) throws JsonProcessingException {
//	    return service.rejectInvite(req);
//	}

}
