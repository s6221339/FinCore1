package com.example.FinCore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.FinCore.service.itfc.PaymentService;
import com.example.FinCore.vo.request.CreatePaymentRequest;
import com.example.FinCore.vo.request.UpdatePaymentRequest;
import com.example.FinCore.vo.response.BasicResponse;
import com.example.FinCore.vo.response.SearchPaymentResponse;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(allowedHeaders = "*")
@RequestMapping(value = "finbook/payment/")
public class PaymentController 
{
	
	@Autowired
	private PaymentService service;
	
	@PostMapping(value = "create")
	public BasicResponse create(@Valid @RequestBody CreatePaymentRequest req)
	{
		return service.create(req);
	}
	
	@PostMapping(value = "delete")
	public BasicResponse delete(@RequestParam("paymentId") int paymentId)
	{
		return service.delete(paymentId);
	}
	
	@PostMapping(value = "update")
	public BasicResponse update(@Valid @RequestBody UpdatePaymentRequest req) 
	{
		return service.update(req);
	}
	
	@PostMapping(value = "getByAccount")
	public SearchPaymentResponse getPaymentInfoByAccount(@RequestParam("account") String account) 
	{
		return service.getPaymentInfoByAccount(account);
	}
	
}
