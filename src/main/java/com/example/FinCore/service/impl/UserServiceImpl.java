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
import com.example.FinCore.vo.SimpleUserVO;
import com.example.FinCore.vo.UserVO;
import com.example.FinCore.vo.request.RregisterUserRequest;
import com.example.FinCore.vo.request.UpdatePasswordUserRequest;
import com.example.FinCore.vo.request.UpdateUserRequest;
import com.example.FinCore.vo.request.loginRequest;
import com.example.FinCore.vo.response.BasicResponse;
import com.example.FinCore.vo.response.FamilyListResponse;
import com.example.FinCore.vo.response.MemberNameResponse;
import com.example.FinCore.vo.response.UserResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
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
	public BasicResponse register(RregisterUserRequest req) {
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
	    if (user == null) {
	        return new UserResponse(ResponseMessages.ACCOUNT_NOT_FOUND);
	    }

	    // superAdmin 轉成 role 字串
	    String role = user.isSuperAdmin() ? "admin" : "user";

	    UserVO vo = new UserVO(
	        user.getAccount(),
	        user.getName(),
	        user.getPhone(),
	        user.getBirthday(),
	        user.getAvatar(),
	        role
	    );
	    return new UserResponse(ResponseMessages.SUCCESS, vo);
	}

	@Override
	public FamilyListResponse getFamilyByAccount(String account) throws JsonProcessingException {
	    // 1. 檢查帳號有沒有填，沒填就直接回傳錯誤
	    if (account == null || account.isEmpty()) {
	        return new FamilyListResponse(ResponseMessages.MISSING_REQUIRED_FIELD);
	    }

	    // 2. 準備一個結果清單，把有關的家族資料都丟進去
	    List<FamilyVO> voList = new ArrayList<>();
	    // 3. 從資料庫撈出所有家族，之後要一個一個檢查
	    List<Family> familyList = familyDao.selectAllFamily();

	    for (Family family : familyList) {
	        // 4-1. family 的 invitor 欄位可能為 null，需要先判斷
	        List<String> invitorList;
	        String invitorContent = family.getInvitor();
	        if (invitorContent == null || invitorContent.trim().isEmpty()) {
	            invitorList = new ArrayList<>();
	        } else {
	            invitorList = mapper.readValue(invitorContent, new com.fasterxml.jackson.core.type.TypeReference<List<String>>() {});
	        }
	        // 4-2. 查 owner 的名字（如找不到顯示 null）
	        User ownerUser = userDao.selectById(family.getOwner());
	        SimpleUserVO ownerVO = new SimpleUserVO(family.getOwner(),
	                ownerUser != null ? ownerUser.getName() : null);

	        // 4-3. 組成成員物件清單
	        List<SimpleUserVO> memberList = new ArrayList<>();
	        if (invitorList != null) {
	            for (String memberAccount : invitorList) {
	                User memberUser = userDao.selectById(memberAccount);
	                memberList.add(new SimpleUserVO(
	                        memberAccount,
	                        memberUser != null ? memberUser.getName() : null
	                ));
	            }
	        }

	        // 4-4. 判斷自己是不是 owner 或 invitor（只要有一項就加入回傳清單）
	        boolean isOwner = account.equals(family.getOwner());
	        boolean isInvitor = invitorList != null && invitorList.contains(account);

	        if (isOwner || isInvitor) {
	            FamilyVO vo = new FamilyVO(
	                    family.getId(),
	                    family.getName(),
	                    ownerVO,      // Owner 是物件
	                    memberList    // 成員清單
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
	
	@Override
	public MemberNameResponse getNameByAccount(String account) {
	    if (account == null || account.isEmpty()) {
	        // 可用 ResponseMessages.MISSING_REQUIRED_FIELD
	        return new MemberNameResponse(
	            ResponseMessages.MISSING_REQUIRED_FIELD.getCode(),
	            "account 不可為空",
	            null
	        );
	    }
	    String name = userDao.findNameByAccount(account);
	    if (name == null) {
	        return new MemberNameResponse(
	            ResponseMessages.ACCOUNT_NOT_FOUND.getCode(),
	            "查無此帳號",
	            null
	        );
	    }
	    
	    MemberNameResponse.MemberData memberData = new MemberNameResponse.MemberData(name, account); 
	    return new MemberNameResponse(ResponseMessages.SUCCESS.getCode(), "查詢成功", memberData);
	}
	
	/**
	 * 使用者登出（如需真正後端驗證，請配合 session 或 token blacklist 等方式）
	 * @param account 使用者帳號
	 * @return 登出結果
	 */
	@Override
	public BasicResponse logout(String account) {
	    // 1. 檢查帳號參數是否為空
	    if (account == null || account.isEmpty()) {
	        return new BasicResponse(ResponseMessages.MISSING_REQUIRED_FIELD);
	    }
	    // 2. 檢查帳號是否存在
	    int exists = userDao.selectCountByAccount(account);
	    if (exists == 0) {
	        return new BasicResponse(ResponseMessages.ACCOUNT_NOT_FOUND);
	    }
	    // 3. 執行登出邏輯
	    // RESTful 無狀態系統通常不做 server-side logout，前端只需刪除 token/session
	    // 若有 session 管理，這裡可加上 session 失效、token black list 等操作
	    return new BasicResponse(ResponseMessages.SUCCESS);
	}
	
}
