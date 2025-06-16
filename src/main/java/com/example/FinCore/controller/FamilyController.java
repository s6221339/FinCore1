package com.example.FinCore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

import jakarta.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping(value = "finbook/family/")
public class FamilyController {
	
	@Autowired
	private FamilyService service;
	
//	@RequestBody：用在傳參是一個 class 的時候，例如 Request
//	@RequestParam：用在傳參為「基本資料型態」的時候，例如 int、String
//	@Valid：如果 RequestBody 中有資料驗證，則需要使用該註釋來啟動驗證功能
	@PostMapping(value = "create")
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
	public BasicResponse getFamilyById(@RequestParam("familyId") int familyId) {
		return service.getFamilyById(familyId);
	}
	
	@PostMapping(value = "listAllFamily")
	public FamilyListResponse listAllFamily() throws JsonProcessingException {
		return service.listAllFamily();
	}

	@PostMapping(value = "invite")
	public BasicResponse inviteMember(@Valid @RequestBody InviteMemberRequest req) //
	throws JsonProcessingException{
		return service.inviteMember(req);
	}
	
	@PostMapping(value = "dismiss")
	public BasicResponse dismissFamily(@Valid @RequestBody DismissFamilyRequest req) {
		return service.dismissFamily(req);
	}
	
	@PostMapping(value = "kick")
	public BasicResponse kickMember(@Valid @RequestBody KickMemberRequest req) {
		return service.kickMember(req);
	}
	
	@PostMapping(value = "ownerResignAndAssign")
	public BasicResponse ownerResignAndAssign(@Valid @RequestBody OwnerResignAndAssignRequest req) {
		return service.ownerResignAndAssign(req);
	}
	
	@PostMapping(value = "quit")
	public BasicResponse quitFamily(@Valid @RequestBody QuitFamilyRequest req) {
		return service.quitFamily(req);
	}
	
	@PostMapping(value = "rename")
	public BasicResponse renameFamily(@Valid @RequestBody RenameFamilyRequest req) {
		return service.renameFamily(req);
	}
	
	@PostMapping(value = "acceptInvite")
	public BasicResponse acceptInvite(@Valid @RequestBody AcceptInviteRequest req) throws JsonProcessingException {
	    return service.acceptInvite(req);
	}

	@PostMapping(value = "rejectInvite")
	public BasicResponse rejectInvite(@Valid @RequestBody AcceptInviteRequest req) throws JsonProcessingException {
	    return service.rejectInvite(req);
	}

}
