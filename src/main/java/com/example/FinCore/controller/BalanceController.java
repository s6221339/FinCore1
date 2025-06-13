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
import com.example.FinCore.vo.request.AccountWithDateFilterRequest;
import com.example.FinCore.vo.request.GetBudgetByBalanceIdRequest;
import com.example.FinCore.vo.request.UpdateBalanceRequest;
import com.example.FinCore.vo.response.BalanceListResponse;
import com.example.FinCore.vo.response.BasicResponse;
import com.example.FinCore.vo.response.BudgetListResponse;
import com.example.FinCore.vo.response.BudgetResponse;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(allowedHeaders = "*")
@RequestMapping(value = "finbook/balance/")
public class BalanceController 
{
	
	@Autowired
	private BalanceService service;

	@PostMapping(value = "create")
	public BasicResponse create(@Valid @RequestBody CreateBalanceRequest req) throws Exception
	{
		return service.create(req);
	}
	
	@PostMapping(value = "update")
	public BasicResponse update(@Valid @RequestBody UpdateBalanceRequest req) throws Exception
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
	
	@PostMapping(value = "getBudget")
	public BudgetResponse getBudget(@Valid @RequestBody GetBudgetByBalanceIdRequest req) 
	{
		return service.getBudget(req);
	}
	
	@PostMapping(value = "getBudgetByAccount")
	public BudgetListResponse getBudgetByAccount(@Valid @RequestBody AccountWithDateFilterRequest req) 
	{
		return service.getBudgetByAccount(req);
	}
	
	@PostMapping(value = "getAllByAccount")
	public BalanceListResponse getAllBalance(String account) 
	{
		return service.getAllBalance(account);
	}
	
}
