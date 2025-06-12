package com.example.FinCore.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.catalina.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.FinCore.constants.ResponseMessages;
import com.example.FinCore.dao.FamilyDao;
import com.example.FinCore.dao.UserDao;
import com.example.FinCore.entity.Family;
import com.example.FinCore.entity.User;
import com.example.FinCore.service.itfc.UserService;
import com.example.FinCore.vo.FamilyVO;
import com.example.FinCore.vo.UserVO;
import com.example.FinCore.vo.request.CreateUserRequest;
import com.example.FinCore.vo.request.UpdatePasswordUserRequest;
import com.example.FinCore.vo.request.UpdateUserRequest;
import com.example.FinCore.vo.request.loginRequest;
import com.example.FinCore.vo.response.BasicResponse;
import com.example.FinCore.vo.response.FamilyIdResponse;
import com.example.FinCore.vo.response.FamilyListResponse;
import com.example.FinCore.vo.response.UserResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private FamilyDao familyDao;

	@Autowired
	private UserDao userDao;
	
	private final ObjectMapper mapper = new ObjectMapper();

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
		// 1.查詢帳號
		User user = userDao.selectById(req.account());
		if (user == null) {
			return new BasicResponse(ResponseMessages.ACCOUNT_NOT_FOUND);
		}

		// 2.比對舊密碼
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		if (!encoder.matches(req.oldPassword(), user.getPassword())) {
			return new BasicResponse(ResponseMessages.PASSWORD_NOT_MATCH);

		}
		// 3.新密碼加密再存回資料庫
		String encodeNewPwd = encoder.encode(req.newPassword());

		// 更新密碼的 Dao 方法
		int update = userDao.updatePassword(user.getAccount(), encodeNewPwd);

		if (update > 0) {
			return new BasicResponse(ResponseMessages.SUCCESS);
		} else {
			return new BasicResponse(ResponseMessages.UPDATE_USER_FAIL);
		}
	}

	@Override
	public UserResponse getUser(String account) {
		if (account == null || account.isEmpty()) {
			return new UserResponse(ResponseMessages.MISSING_REQUIRED_FIELD);
		}
		User user = userDao.selectById(account);
		if(user == null) {
			return new UserResponse(ResponseMessages.ACCOUNT_NOT_FOUND);
		}
		UserVO vo = new UserVO(user.getAccount(), user.getName(),
				user.getPhone(), user.getBirthday(), user.getAvatar());
		
		return new UserResponse(ResponseMessages.SUCCESS, vo);
	}

	// 檢查 family 的 owner 是否和 account 一致（用 String 的 equals 方法）
	// 檢查 family 的 invitorList 是否存在 account（用 List 的 contains 方法）
	// 符合其中一項加到voList
	// 也可以用 or 判斷式
	@Override
	public FamilyListResponse getFamilyByAccount(String account) throws JsonProcessingException {
		if(account == null || account.isEmpty()) {
			return new FamilyListResponse(ResponseMessages.MISSING_REQUIRED_FIELD);
		}
		List<FamilyVO> voList = new ArrayList<FamilyVO>();
		List<Family> familyList = familyDao.selectAllFamily();
		for(Family family : familyList)
		{
			List<String> invitorList = mapper.readValue(
					family.getInvitor(), 
					new TypeReference<List<String>>() {});
			
			FamilyVO vo = new FamilyVO(family.getId(), family.getName()
					, family.getOwner(), invitorList);
			voList.add(vo);
		}
		return new FamilyListResponse(ResponseMessages.SUCCESS, voList);
		
	}
	
	@Override
	public BasicResponse login(loginRequest req) {
	    // 1. 查詢會員
	    User user = userDao.selectById(req.account());
	    if (user == null) {
	        return new BasicResponse(ResponseMessages.ACCOUNT_NOT_FOUND);
	    }

	    // 2. 密碼比對
	    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	    if (!encoder.matches(req.password(), user.getPassword())) {
	        return new BasicResponse(ResponseMessages.PASSWORD_NOT_MATCH);
	    }

	    // 3. 登入成功（你可以選擇只回傳成功，不帶 user，或帶 user 資料給前端）
	    return new BasicResponse(ResponseMessages.SUCCESS);
	}
	
}
