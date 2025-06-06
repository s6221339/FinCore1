package com.example.FinCore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.example.FinCore.service.itfc.UserService;

@CrossOrigin
@RestController
public class UserController {

	@Autowired
	private UserService service;
	
	
}
