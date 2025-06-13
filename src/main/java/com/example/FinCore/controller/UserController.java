package com.example.FinCore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.FinCore.service.itfc.UserService;
import com.example.FinCore.vo.request.CreateUserRequest;
import com.example.FinCore.vo.request.UpdatePasswordUserRequest;
import com.example.FinCore.vo.request.UpdateUserRequest;
import com.example.FinCore.vo.request.loginRequest;
import com.example.FinCore.vo.response.BasicResponse;
import com.example.FinCore.vo.response.FamilyListResponse;
import com.example.FinCore.vo.response.UserResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping(value = "finbook/user/")
public class UserController {

	@Autowired
	private UserService service;
	
	// 測試成功
	@PostMapping(value = "register")
    public BasicResponse register(@Valid @RequestBody CreateUserRequest req) {
        return service.register(req);
    }
	// 測試成功
	@PostMapping(value = "update")
    public BasicResponse update(@Valid @RequestBody UpdateUserRequest req) {
        return service.update(req);
    }
	
	@PostMapping(value = "cancel")
	    public BasicResponse cancel(@RequestParam("account") String account) {
	        return service.cancel(account);
	    }
	
	// 測試成功
	@PostMapping(value = "updatePasswordUser")
	public BasicResponse updatePasswordUser(@Valid @RequestBody UpdatePasswordUserRequest req) {
		return service.updatePasswordUser(req);
	}
	// 測試成功
	@PostMapping(value = "getUser")
	public UserResponse getUser(@RequestParam("account") String account) {
		return service.getUser(account);
	}
	// 測試成功
	@PostMapping(value = "getFamilyByAccount")
	public FamilyListResponse getFamilyByAccount(@RequestParam("account") String account) throws JsonProcessingException {
		return service.getFamilyByAccount(account);
	}
	// 測試成功
	@PostMapping(value = "login")
	public BasicResponse login(@Valid @RequestBody loginRequest req) {
		return service.login(req);
	}
	
}
