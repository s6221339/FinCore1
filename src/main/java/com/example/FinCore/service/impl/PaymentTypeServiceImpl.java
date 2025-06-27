package com.example.FinCore.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.FinCore.constants.ResponseMessages;
import com.example.FinCore.dao.PaymentTypeDao;
import com.example.FinCore.dao.UserDao;
import com.example.FinCore.service.itfc.PaymentTypeService;
import com.example.FinCore.vo.PaymentTypeVO;
import com.example.FinCore.vo.request.CreatePaymentTypeRequest;
import com.example.FinCore.vo.response.BasicResponse;
import com.example.FinCore.vo.response.GetPaymentTypeListResponse;

@Service
public class PaymentTypeServiceImpl implements PaymentTypeService 
{
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private PaymentTypeDao paymentTypeDao;

	@Override
	public BasicResponse createType(CreatePaymentTypeRequest req) 
	{
//		System.out.println(req.toString());
		if(!req.account().equals("default") && !userDao.existsById(req.account()))
			return new BasicResponse(ResponseMessages.ACCOUNT_NOT_FOUND);
		
		int count = paymentTypeDao.selectCount(req.type(), req.item(), req.account());
		if(count > 0)
			return new BasicResponse(ResponseMessages.ITEM_EXISTED);
		
		paymentTypeDao.create(req.type(), req.item(), req.account());
		return new BasicResponse(ResponseMessages.SUCCESS);
	}

	@Override
	public GetPaymentTypeListResponse getTypeByAccount(String account) 
	{
		var ptList = paymentTypeDao.getTypeByAccount(account);
		List<PaymentTypeVO> result = new ArrayList<>();
		ptList.forEach(item -> {
			PaymentTypeVO vo = new PaymentTypeVO(item.getType(), item.getItem());
			result.add(vo);
		});
		return new GetPaymentTypeListResponse(ResponseMessages.SUCCESS, result);
	}

}
