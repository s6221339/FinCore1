package com.example.FinCore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.FinCore.service.itfc.FamilyService;
import com.example.FinCore.vo.request.CreateFamilyRequest;
import com.example.FinCore.vo.request.DeleteFamilyRequest;
import com.example.FinCore.vo.request.UpdateFamilyRequest;
import com.example.FinCore.vo.response.BasicResponse;

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
	
	@PostMapping(value = "update")
	public BasicResponse update(@Valid @RequestBody UpdateFamilyRequest req) {
		return service.update(req);
	}
	
	@PostMapping(value = "delete")
	public BasicResponse delete(@Valid @RequestBody DeleteFamilyRequest req) {
		return service.delete(req);
	}

}
