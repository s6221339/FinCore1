package com.example.FinCore.service.impl;

import org.springframework.stereotype.Service;

import com.example.FinCore.service.itfc.PaymentTypeService;
import com.example.FinCore.vo.request.CreatePaymentTypeRequest;
import com.example.FinCore.vo.response.BasicResponse;

@Service
public class PaymentTypeServiceImpl implements PaymentTypeService 
{

	@Override
	public BasicResponse createType(CreatePaymentTypeRequest req) 
	{
		return null;
	}

}
