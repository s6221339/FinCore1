package com.example.FinCore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.FinCore.service.itfc.FamilyService;
import com.example.FinCore.vo.request.CreateFamilyRequest;
import com.example.FinCore.vo.request.DeleteFamilyRequest;
import com.example.FinCore.vo.request.UpdateFamilyRequest;
import com.example.FinCore.vo.response.BasicResponse;



@CrossOrigin
@RestController
public class FamilyController {
	
	@Autowired
	private FamilyService service;
	
	@PostMapping(value = "finbook/createFamily")
	public BasicResponse create(CreateFamilyRequest req) {
		return service.create(req);
	}
	
	@PostMapping(value = "finbook/updateFamily")
	public BasicResponse update(UpdateFamilyRequest req) {
		return service.update(req);
	}
	
	@PostMapping(value = "finbook/deleteFamily")
	public BasicResponse delete(DeleteFamilyRequest req) {
		return service.delete(req);
	}

}
