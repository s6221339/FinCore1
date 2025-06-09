package com.example.FinCore.service.impl;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.FinCore.constants.ResponseMessages;
import com.example.FinCore.dao.FamilyDao;
import com.example.FinCore.dao.UserDao;
import com.example.FinCore.service.itfc.PaymentService;
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
	public BasicResponse register(CreateUserRequest req) {
        // 1. 帳號不可重複
        int exists = userDao.selectCountByAccount(req.getAccount());
        if (exists > 0) {
            return new BasicResponse(ResponseMessages.ACCOUNT_EXIST);
        }

        // 2. 密碼加密
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        String encodedPwd = encoder.encode(req.getPassword());

        // 3. 建立新帳號
        userDao.register(
            req.getAccount(),
            req.getName(),
            encodedPwd,
            req.getPhone(),
            LocalDate.now()
        );

        return new BasicResponse(ResponseMessages.SUCCESS);
	    
	}

	@Override
	public BasicResponse update(UpdateUserRequest req) {
        // 1. 確認帳號存在
        int exists = userDao.selectCountByAccount(req.getAccount());
        if (exists == 0) {
            return new BasicResponse(ResponseMessages.ACCOUNT_NOT_FOUND);
        }

        // 2. 更新
        int updated = userDao.update(
            req.getAccount(),
            req.getName(),
            req.getPhone(),
            req.getBirthday(),
            req.getAvatar()
        );
        if (updated > 0) {
            return new BasicResponse(ResponseMessages.SUCCESS);
        } else {
            return new BasicResponse(ResponseMessages.UPDATE_USER_FAIL);
        }
	}

	@Override
	public BasicResponse cancel(String account) {
		 // 1. 檢查必要欄位
        if (!StringUtils.hasText(account)) {
            return new BasicResponse(ResponseMessages.MISSING_REQUIRED_FIELD);
        }

        // 2. 確認帳號存在
        int exists = userDao.selectCountByAccount(account);
        if (exists == 0) {
            return new BasicResponse(ResponseMessages.ACCOUNT_NOT_FOUND);
        }

        // 3. 執行刪除
        userDao.cancel(account);
        return new BasicResponse(ResponseMessages.SUCCESS);
    }
	
}
