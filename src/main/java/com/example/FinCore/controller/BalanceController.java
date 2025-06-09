package com.example.FinCore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.FinCore.service.itfc.BalanceService;
import com.example.FinCore.service.itfc.CreateBalanceRequest;
import com.example.FinCore.vo.request.UpdateBalanceRequest;
import com.example.FinCore.vo.response.BasicResponse;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(allowedHeaders = "*")
@RequestMapping(value = "finbook/balance/")
public class BalanceController 
{
	
	@Autowired
	private BalanceService service;

	@PostMapping(value = "create")
	public BasicResponse create(@Valid @RequestBody CreateBalanceRequest req)
	{
		return service.create(req);
	}
	
	@PostMapping(value = "update")
	public BasicResponse update(@Valid @RequestBody UpdateBalanceRequest req)
	{
		return service.update(req);
	}
	
	@PostMapping(value = "delete")
	public BasicResponse delete(@RequestParam("balanceId") int balanceId) throws Exception
	{
		return service.delete(balanceId);
	}
	
	@PostMapping(value = "deleteByAccount")
	public BasicResponse deleteByAccount(@RequestParam("account") String account) throws Exception
	{
		return service.deleteByAccount(account);
	}
	
}
