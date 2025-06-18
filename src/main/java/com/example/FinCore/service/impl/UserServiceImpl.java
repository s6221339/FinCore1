package com.example.FinCore.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.FinCore.constants.ResponseMessages;
import com.example.FinCore.dao.BalanceDao;
import com.example.FinCore.dao.FamilyDao;
import com.example.FinCore.dao.PaymentDao;
import com.example.FinCore.dao.PaymentTypeDao;
import com.example.FinCore.dao.SavingsDao;
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
import com.example.FinCore.vo.response.FamilyListResponse;
import com.example.FinCore.vo.response.UserResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private FamilyDao familyDao;

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private BalanceDao balanceDao;
	
	@Autowired
	private PaymentDao paymentDao;
	
	@Autowired
	private SavingsDao savingsDao;
	
	@Autowired
	private PaymentTypeDao paymentTypeDao;

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
	public BasicResponse cancel(String account) throws Exception {
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
		List<Integer> balanceIdList = balanceDao.getBalanceIdListByAccount(account);
		List<Integer> paymentIdList = paymentDao.getPaymentIdListByBalanceIdList(balanceIdList);
		try
		{
			paymentDao.deleteAllById(paymentIdList);
			savingsDao.deleteByBalanceIdList(balanceIdList);
			
//			TODO：AI查詢資料也要刪除
			
			balanceDao.deleteAllById(balanceIdList);
			paymentTypeDao.deleteByAccount(account);
			userDao.cancel(account);
		}
		catch(Exception e)
		{
			throw e;
		}
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

	@Override
	public FamilyListResponse getFamilyByAccount(String account) throws JsonProcessingException {
		// 1. 檢查帳號有沒有填，沒填就直接回傳錯誤
	    if (account == null || account.isEmpty()) {
	        return new FamilyListResponse(ResponseMessages.MISSING_REQUIRED_FIELD);
	    }
	    // 2. 準備一個結果清單，把有關的家族資料都丟進去
	    List<FamilyVO> voList = new ArrayList<FamilyVO>();
	    // 3. 從資料庫撈出所有家族，之後要一個一個檢查
	    List<Family> familyList = familyDao.selectAllFamily(); // 取出所有家族
	    // 4. 逐一檢查每個 family，看這個帳號是不是 owner 或 invitor
	    for (Family family : familyList) {
	        // 4-1. family 的 invitor 欄位是 JSON 字串，要先轉成 List 才能判斷
	        List<String> invitorList = mapper.readValue(
	                family.getInvitor(),
	                new TypeReference<List<String>>() {});
	        // 4-2. 判斷自己是不是這個家族的 owner
	        boolean isOwner = account.equals(family.getOwner());
	        // 4-3. 判斷自己是不是 invitor（家族成員）
	        boolean isInvitor = invitorList != null && invitorList.contains(account);
	        // 4-4. 只要有其中一個符合（是 owner 或是成員），就加進回傳清單
	        if (isOwner || isInvitor) {
	            FamilyVO vo = new FamilyVO(
	                family.getId(),         // 家族編號
	                family.getName(),       // 家族名稱
	                family.getOwner(),      // 家族負責人
	                invitorList             // 家族成員帳號清單
	            );
	            voList.add(vo);
	        }
	    }
	    // 5. 統一回傳查詢成功與結果清單
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
