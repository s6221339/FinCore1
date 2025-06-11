package com.example.FinCore.service.impl;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.FinCore.constants.ResponseMessages;
import com.example.FinCore.dao.FamilyDao;
import com.example.FinCore.dao.UserDao;
import com.example.FinCore.entity.User;
import com.example.FinCore.service.itfc.UserService;
import com.example.FinCore.vo.request.CreateUserRequest;
import com.example.FinCore.vo.request.UpdatePasswordUserRequest;
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
		userDao.register(req.getAccount(), req.getName(), encodedPwd, req.getPhone(), LocalDate.now());

		return new BasicResponse(ResponseMessages.SUCCESS);

	}

	@Override
	@Transactional
	public BasicResponse update(UpdateUserRequest req) {
		// 1. 確認帳號存在
		int exists = userDao.selectCountByAccount(req.getAccount());
		if (exists == 0) {
			return new BasicResponse(ResponseMessages.ACCOUNT_NOT_FOUND);
		}

		// 2. 更新
		int updated = userDao.update(req.getAccount(), req.getName(), req.getPhone(), req.getBirthday(),
				req.getAvatar());
		if (updated > 0) {
			return new BasicResponse(ResponseMessages.SUCCESS);
		} else {
			return new BasicResponse(ResponseMessages.UPDATE_USER_FAIL);
		}
	}

	@Override
	@Transactional
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

	@Override
	@Transactional
	public BasicResponse updatePasswordUser(UpdatePasswordUserRequest req) {
		// 1.檢查欄位
		if (req.account() == null || req.account().isEmpty() || //
				req.oldPassword() == null || req.oldPassword().isEmpty() || //
				req.newPassword() == null || req.newPassword().isEmpty()) {
			return new BasicResponse(ResponseMessages.MISSING_REQUIRED_FIELD);

		}

		// 2.查詢帳號
		User user = userDao.selectById(req.account());
		if (user == null) {
			return new BasicResponse(ResponseMessages.ACCOUNT_NOT_FOUND);
		}

		// 3.比對舊密碼
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		if (!encoder.matches(req.oldPassword(), user.getPassword())) {
			return new BasicResponse(ResponseMessages.PASSWORD_NOT_MATCH);

		}
		// 4.新密碼加密再存回資料庫
		String encodeNewPwd = encoder.encode(req.newPassword());

		// 更新密碼的 Dao 方法
		int update = userDao.updatePassword(user.getAccount(), encodeNewPwd);

		if (update > 0) {
			return new BasicResponse(ResponseMessages.SUCCESS);
		} else {
			return new BasicResponse(ResponseMessages.UPDATE_USER_FAIL);
		}
	}

}
