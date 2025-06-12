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
import com.example.FinCore.vo.response.BasicResponse;

import jakarta.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping(value = "finbook/user/")
public class UserController {

	@Autowired
	private UserService service;
	
	@PostMapping(value = "register")
    public BasicResponse register(@Valid @RequestBody CreateUserRequest req) {
        return service.register(req);
    }
	
	@PostMapping(value = "update")
    public BasicResponse update(@Valid @RequestBody UpdateUserRequest req) {
        return service.update(req);
    }
	
	@PostMapping(value = "cancel")
	    public BasicResponse cancel(@RequestParam String account) {
	        return service.cancel(account);
	    }
	
	@PostMapping(value = "updatePasswordUser")
	public BasicResponse updatePasswordUser(@Valid @RequestBody UpdatePasswordUserRequest req) {
		return service.updatePasswordUser(req);
	}
	
	@PostMapping(value = "getUser")
	public BasicResponse getUser(@RequestParam String account) {
		return service.getUser(account);
	}
	
	
	
	
}
