package com.example.FinCore.service.impl;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.FinCore.constants.ResponseMessages;
import com.example.FinCore.dao.FamilyDao;
import com.example.FinCore.dao.UserDao;
import com.example.FinCore.service.itfc.UserService;
import com.example.FinCore.vo.request.CreateUserRequest;
import com.example.FinCore.vo.request.UpdateUserRequest;
import com.example.FinCore.vo.response.BasicResponse;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private FamilyDao familyDao;

	@Autowired
	private UserDao userDao; 

	@Override
    @Transactional
	public BasicResponse create(CreateUserRequest req) {
		// 1. 檢查必要欄位
        if (req.getAccount() == null || req.getAccount().isEmpty() ||
            req.getName() == null || req.getName().isEmpty() ||
            req.getPassword() == null || req.getPassword().isEmpty()) {
            return new BasicResponse(ResponseMessages.MISSING_REQUIRED_FIELD.getCode(),
                                     ResponseMessages.MISSING_REQUIRED_FIELD.getMessage());
        }

        // 2. 帳號不可重複
        int exists = userDao.selectCountByAccount(req.getAccount());
        if (exists > 0) {
//            return new BasicResponse(ResponseMessages.ACCOUNT_EXIST.getCode(),
//                                     ResponseMessages.ACCOUNT_EXIST.getMessage());
        }

        // 3. 密碼加密
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        String encodedPwd = encoder.encode(req.getPassword());

        // 4. 建立新帳號
        userDao.create(
            req.getAccount(),
            req.getName(),
            encodedPwd,
            req.getPhone(),
            req.getAvatar(),
            req.getSuperAdmin(),
            LocalDate.now()
        );

        return new BasicResponse(ResponseMessages.SUCCESS.getCode(),
                                 ResponseMessages.SUCCESS.getMessage());
	    
	}

	@Override
	public BasicResponse update(UpdateUserRequest req) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BasicResponse delete(String account) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
