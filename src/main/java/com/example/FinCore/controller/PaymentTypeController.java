package com.example.FinCore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.FinCore.service.itfc.PaymentTypeService;
import com.example.FinCore.vo.request.CreatePaymentTypeRequest;
import com.example.FinCore.vo.response.BasicResponse;
import com.example.FinCore.vo.response.GetPaymentTypeListResponse;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(allowedHeaders = "*")
@RequestMapping(value = "finbook/paymentType/")
public class PaymentTypeController 
{
	
	@Autowired
	private PaymentTypeService service;
	
	@PostMapping(value = "create")
	public BasicResponse createType(@Valid @RequestBody CreatePaymentTypeRequest req)
	{
		return service.createType(req);
	}
	
	@PostMapping(value = "getByAccount")
	public GetPaymentTypeListResponse getTypeByAccount(@RequestParam("account") String account)
	{
		return service.getTypeByAccount(account);
	}
	
}
