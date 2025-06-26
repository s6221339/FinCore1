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
import com.example.FinCore.vo.request.CreateFamilyRequest;
import com.example.FinCore.vo.request.DismissFamilyRequest;
import com.example.FinCore.vo.request.InviteMemberRequest;
import com.example.FinCore.vo.request.InviteRequest;
import com.example.FinCore.vo.request.KickMemberRequest;
import com.example.FinCore.vo.request.OwnerResignAndAssignRequest;
import com.example.FinCore.vo.request.QuitFamilyRequest;
import com.example.FinCore.vo.request.RenameFamilyRequest;
import com.example.FinCore.vo.response.BasicResponse;
import com.example.FinCore.vo.response.FamilyInvitationListResponse;
import com.example.FinCore.vo.response.FamilyListResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

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
		    summary = ApiDocConstants.FAMILY_CREATE_SUMMARY,
		    description = ApiDocConstants.FAMILY_CREATE_DESC,
		    method = "POST",
		    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
		        description = "建立家族群組請求資料規則：" + ApiDocConstants.FAMILY_CREATE_REQUEST_BODY_RULE
		    )
		)
		@ApiResponses({
		    @ApiResponse(responseCode = "200", description = ApiDocConstants.CREATE_SUCCESS),
		    @ApiResponse(responseCode = "400", description = ApiDocConstants.FAMILY_CREATE_RESPONSE_400),
		    @ApiResponse(responseCode = "404", description = ApiDocConstants.FAMILY_CREATE_RESPONSE_404),
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
	
	@PostMapping(value = "getById")
	@Operation(
		    summary = ApiDocConstants.FAMILY_GET_BY_ID_SUMMARY,
		    description = ApiDocConstants.FAMILY_GET_BY_ID_DESC,
		    method = "POST",
		    parameters = {
		        @Parameter(
		            name = "familyId",
		            description = "家族群組 ID（必填，需大於 0）"
		        )
		    }
		)
		@ApiResponses({
		    @ApiResponse(responseCode = "200", description = ApiDocConstants.SEARCH_SUCCESS),
		    @ApiResponse(responseCode = "400", description = ApiDocConstants.MISSING_REQUIRED_FIELD),
		    @ApiResponse(responseCode = "404", description = ApiDocConstants.FAMILY_NOT_FOUND),
		})
	public BasicResponse getById(@RequestParam("familyId") int familyId) {
		return service.getById(familyId);
	}
	
	@PostMapping(value = "listAllFamily")
	@Operation(
	        summary = ApiDocConstants.FAMILY_LIST_ALL_FAMILY_SUMMARY,
	        description = ApiDocConstants.FAMILY_LIST_ALL_FAMILY_DESC
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
		    summary = ApiDocConstants.FAMILY_INVITE_MEMBER_SUMMARY,
		    description = ApiDocConstants.FAMILY_INVITE_MEMBER_DESC,
		    method = "POST",
		    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
		        description = "邀請家族新成員請求資料規則：" + ApiDocConstants.FAMILY_INVITE_MEMBER_REQUEST_BODY_RULE
		    )
		)
		@ApiResponses({
		    @ApiResponse(responseCode = "200", description = ApiDocConstants.CREATE_SUCCESS),
		    @ApiResponse(responseCode = "400", description = ApiDocConstants.FAMILY_INVITE_MEMBER_RESPONSE_400),
		    @ApiResponse(responseCode = "403", description = ApiDocConstants.NO_PERMISSION),
		    @ApiResponse(responseCode = "404", description = ApiDocConstants.FAMILY_INVITE_MEMBER_RESPONSE_404),
		})
	public BasicResponse inviteMember(@Valid @RequestBody InviteMemberRequest req) //
	throws JsonProcessingException{
		return service.inviteMember(req);
	}
	
	@PostMapping(value = "dismiss")
	@Operation(
		    summary = ApiDocConstants.FAMILY_DISMISS_SUMMARY,
		    description = ApiDocConstants.FAMILY_DISMISS_DESC,
		    method = "POST",
		    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
		        description = "解散家族群組請求資料規則：" + ApiDocConstants.FAMILY_DISMISS_REQUEST_BODY_RULE
		    )
		)
		@ApiResponses({
		    @ApiResponse(responseCode = "200", description = ApiDocConstants.DELETE_SUCCESS),
		    @ApiResponse(responseCode = "403", description = ApiDocConstants.NO_PERMISSION),
		    @ApiResponse(responseCode = "404", description = ApiDocConstants.FAMILY_NOT_FOUND),
		})
	public BasicResponse dismissFamily(@Valid @RequestBody DismissFamilyRequest req) {
		return service.dismissFamily(req);
	}
	
	@PostMapping(value = "kick")
	@Operation(
		    summary = ApiDocConstants.FAMILY_KICK_MEMBER_SUMMARY,
		    description = ApiDocConstants.FAMILY_KICK_MEMBER_DESC,
		    method = "POST",
		    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
		        description = "踢除家族成員請求資料規則：" + ApiDocConstants.FAMILY_KICK_MEMBER_REQUEST_BODY_RULE
		    )
		)
		@ApiResponses({
		    @ApiResponse(responseCode = "200", description = ApiDocConstants.UPDATE_SUCCESS),
		    @ApiResponse(responseCode = "400", description = ApiDocConstants.FAMILY_KICK_MEMBER_RESPONSE_400),
		    @ApiResponse(responseCode = "403", description = ApiDocConstants.NO_PERMISSION),
		    @ApiResponse(responseCode = "404", description = ApiDocConstants.MEMBER_NOT_FOUND),
		})
	public BasicResponse kickMember(@Valid @RequestBody KickMemberRequest req) {
		return service.kickMember(req);
	}
	
//  owner退出 合併到 member quit	
//	@PostMapping(value = "ownerQuit")
//	@Operation(
//		    summary = ApiDocConstants.FAMILY_OWNER_QUIT_SUMMARY,
//		    description = ApiDocConstants.FAMILY_OWNER_QUIT_DESC,
//		    method = "POST",
//		    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
//		        description = "群組擁有者退位並指派新擁有者請求資料規則：" + ApiDocConstants.FAMILY_OWNER_QUIT_REQUEST_BODY_RULE
//		    )
//		)
//		@ApiResponses({
//		    @ApiResponse(responseCode = "200", description = ApiDocConstants.UPDATE_SUCCESS),
//		    @ApiResponse(responseCode = "400", description = ApiDocConstants.FAMILY_OWNER_QUIT_RESPONSE_400),
//		    @ApiResponse(responseCode = "403", description = ApiDocConstants.NO_PERMISSION),
//		    @ApiResponse(responseCode = "404", description = ApiDocConstants.FAMILY_OWNER_QUIT_RESPONSE_404),
//		})
//	public BasicResponse ownerQuit(@Valid @RequestBody OwnerResignAndAssignRequest req) {
//		return service.ownerQuit(req);
//	}
	
	@PostMapping(value = "transferOwner")
	@Operation(
		    summary = ApiDocConstants.FAMILY_TRANSFER_OWNER_SUMMARY,
		    description = ApiDocConstants.FAMILY_TRANSFER_OWNER_DESC,
		    method = "POST",
		    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
		        description = "群組擁有者權限轉讓請求資料規則：" + ApiDocConstants.FAMILY_TRANSFER_OWNER_REQUEST_BODY_RULE
		    )
		)
		@ApiResponses({
		    @ApiResponse(responseCode = "200", description = ApiDocConstants.UPDATE_SUCCESS),
		    @ApiResponse(responseCode = "400", description = ApiDocConstants.FAMILY_TRANSFER_OWNER_RESPONSE_400),
		    @ApiResponse(responseCode = "403", description = ApiDocConstants.NO_PERMISSION),
		    @ApiResponse(responseCode = "404", description = ApiDocConstants.FAMILY_TRANSFER_OWNER_RESPONSE_404),
		})
	public BasicResponse transferOwner(@Valid @RequestBody OwnerResignAndAssignRequest req) {
		return service.transferOwner(req);
	}
	
	@PostMapping(value = "quit")
	@Operation(
		    summary = ApiDocConstants.FAMILY_QUIT_MEMBER_SUMMARY,
		    description = ApiDocConstants.FAMILY_QUIT_MEMBER_DESC,
		    method = "POST",
		    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
		        description = "家族成員退出群組請求資料規則：" + ApiDocConstants.FAMILY_QUIT_MEMBER_REQUEST_BODY_RULE
		    )
		)
		@ApiResponses({
		    @ApiResponse(responseCode = "200", description = ApiDocConstants.UPDATE_SUCCESS),
		    @ApiResponse(responseCode = "400", description = ApiDocConstants.FAMILY_QUIT_MEMBER_RESPONSE_400),
		    @ApiResponse(responseCode = "404", description = ApiDocConstants.FAMILY_NOT_FOUND),
		})
	public BasicResponse quitFamily(@Valid @RequestBody QuitFamilyRequest req) {
		return service.quitFamily(req);
	}
	
	@PostMapping(value = "rename")
	@Operation(
		    summary = ApiDocConstants.FAMILY_RENAME_SUMMARY,
		    description = ApiDocConstants.FAMILY_RENAME_DESC,
		    method = "POST",
		    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
		        description = "家族群組改名請求資料規則：" + ApiDocConstants.FAMILY_RENAME_REQUEST_BODY_RULE
		    )
		)
		@ApiResponses({
		    @ApiResponse(responseCode = "200", description = ApiDocConstants.UPDATE_SUCCESS),
		    @ApiResponse(responseCode = "403", description = ApiDocConstants.NO_PERMISSION),
		    @ApiResponse(responseCode = "404", description = ApiDocConstants.FAMILY_NOT_FOUND),
		})
	public BasicResponse renameFamily(@Valid @RequestBody RenameFamilyRequest req) {
		return service.renameFamily(req);
	}
	
	 /**
     * 接受邀請加入家庭群組
	 * @throws JsonProcessingException 
     */
    @PostMapping(value = "acceptInvite")
    @Operation(
    	    summary = ApiDocConstants.FAMILY_INVITATION_ACCEPT_SUMMARY,
    	    description = ApiDocConstants.FAMILY_INVITATION_ACCEPT_DESC,
    	    method = "POST",
    	    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
    	        description = "接受邀請請求，規則：" + ApiDocConstants.FAMILY_INVITATION_ACCEPT_REQUEST_BODY_RULE
    	    )
    	)
    	@ApiResponses({
    	    @ApiResponse(responseCode = "200", description = ApiDocConstants.UPDATE_SUCCESS),
    	    @ApiResponse(responseCode = "400", description = ApiDocConstants.FAMILY_INVITATION_ACCEPT_RESPONSE_400),
    	    @ApiResponse(responseCode = "404", description = ApiDocConstants.FAMILY_INVITATION_ACCEPT_RESPONSE_404),
    	})
    public BasicResponse acceptInvite(@Valid@RequestBody InviteRequest req) throws JsonProcessingException {
        return service.acceptInvite(req);
    }
    
    /**
     * 拒絕邀請加入家庭群組
     * @throws JsonProcessingException 
     */
    @PostMapping(value = "rejectInvite")
    @Operation(
    	    summary = ApiDocConstants.FAMILY_INVITATION_REJECT_SUMMARY,
    	    description = ApiDocConstants.FAMILY_INVITATION_REJECT_DESC,
    	    method = "POST",
    	    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
    	        description = "拒絕邀請請求，規則：" + ApiDocConstants.FAMILY_INVITATION_REJECT_REQUEST_BODY_RULE
    	    )
    	)
    	@ApiResponses({
    	    @ApiResponse(responseCode = "200", description = ApiDocConstants.UPDATE_SUCCESS),
    	    @ApiResponse(responseCode = "400", description = ApiDocConstants.MISSING_REQUIRED_FIELD),
    	    @ApiResponse(responseCode = "404", description = ApiDocConstants.INVITATION_NOT_FOUND),
    	})
    public BasicResponse rejectInvite(@Valid@RequestBody InviteRequest req) throws JsonProcessingException {
        return service.rejectInvite(req);
    }
    
    /**
     * 查詢指定家庭群組的邀請中名單（只回傳未接受的邀請資訊）
     * @param familyId 家庭ID
     * @return FamilyInvitationListResponse
     */
    @PostMapping("getInvitingList")
    @Operation(
    	    summary = ApiDocConstants.FAMILY_INVITATION_LIST_SUMMARY,
    	    description = ApiDocConstants.FAMILY_INVITATION_LIST_DESC,
    	    method = "POST",
    	    parameters = {
    	        @Parameter(
    	            name = "familyId",
    	            description = "家族群組 ID（必填）"
    	        )
    	    }
    	)
    	@ApiResponses({
    	    @ApiResponse(responseCode = "200", description = ApiDocConstants.SEARCH_SUCCESS),
    	})
    public FamilyInvitationListResponse getInvitingList(@RequestParam("familyId") int familyId) {
        return service.getInvitingList(familyId);
    }
}
