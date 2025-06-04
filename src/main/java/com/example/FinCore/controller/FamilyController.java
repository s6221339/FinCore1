package com.example.FinCore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.FinCore.service.itfc.FamilyService;
import com.example.FinCore.vo.request.CreateFamilyRequest;
import com.example.FinCore.vo.request.UpdateRequest;
import com.example.FinCore.vo.response.BasicResponse;



@CrossOrigin
@RestController
public class FamilyController {
	
	@Autowired
	private FamilyService service;
	
	@PostMapping(value = "quiz/create")
	public BasicResponse create(CreateFamilyRequest req) {
		return service.create(req);
	}
	
	@PostMapping(value = "quiz/update")
	public BasicResponse update(UpdateRequest req) {
		return service.update(req);
	}

}
