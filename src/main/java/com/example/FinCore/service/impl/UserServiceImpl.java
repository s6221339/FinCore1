package com.example.FinCore.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
import com.example.FinCore.vo.FamilyAvatarVO;
import com.example.FinCore.vo.SimpleUserAvatarVO;
import com.example.FinCore.vo.SubscriptionVO;
import com.example.FinCore.vo.UserVO;
import com.example.FinCore.vo.request.RregisterUserRequest;
import com.example.FinCore.vo.request.UpdatePasswordUserRequest;
import com.example.FinCore.vo.request.UpdateUserRequest;
import com.example.FinCore.vo.request.loginRequest;
import com.example.FinCore.vo.response.BasicResponse;
import com.example.FinCore.vo.response.FamilyAvatarListResponse;
import com.example.FinCore.vo.response.MemberNameResponse;
import com.example.FinCore.vo.response.SubscriptionResponse;
import com.example.FinCore.vo.response.UserResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Base64;

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
	    // 1. 確認帳號是否存在
	    int exists = userDao.selectCountByAccount(req.getAccount());
	    if (exists == 0) {
	        return new BasicResponse(ResponseMessages.ACCOUNT_NOT_FOUND);
	    }

	    // 2. 解析 avatar 的 base64 字串，轉成 byte[]
	    byte[] avatarBytes = null;
	    String base64 = req.getAvatar();
	    if (base64 != null && !base64.isEmpty()) {
	        // 去掉 "data:image/png;base64," 前綴
	        if (base64.contains(",")) {
	            base64 = base64.substring(base64.indexOf(",") + 1);
	        }
	        try {
	            avatarBytes = java.util.Base64.getDecoder().decode(base64);
	        } catch (IllegalArgumentException e) {
	            return new BasicResponse(ResponseMessages.UPDATE_USER_FAIL);
	        }
	    }

	    // 3. 執行資料更新
	    int updated = userDao.update(
	        req.getAccount(),
	        req.getName(),
	        req.getPhone(),
	        req.getBirthday(),
	        avatarBytes  // 直接存byte[]
	    );
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

	    String role = user.isSuperAdmin() ? "admin" : "user";

	    // 動態判斷圖片格式
	    String avatarBase64 = null;
	    byte[] avatarBytes = user.getAvatar();
	    if (avatarBytes != null && avatarBytes.length > 0) {
	        String mimeType = detectImageMimeType(avatarBytes);
	        avatarBase64 = "data:" + mimeType + ";base64," + Base64.getEncoder().encodeToString(avatarBytes);
	    }

	    UserVO vo = new UserVO(
	        user.getAccount(),
	        user.getName(),
	        user.getPhone(),
	        user.getBirthday(),
	        avatarBase64,
	        role
	    );
	    return new UserResponse(ResponseMessages.SUCCESS, vo);
	}

	// 支援 PNG/JPEG/GIF
	private String detectImageMimeType(byte[] imageBytes) {
	    if (imageBytes == null || imageBytes.length < 8) {
	        return "image/png";
	    }
	    if (imageBytes[0] == (byte)0x89 && imageBytes[1] == 0x50 &&
	        imageBytes[2] == 0x4E && imageBytes[3] == 0x47) {
	        return "image/png";
	    }
	    if (imageBytes[0] == (byte)0xFF && imageBytes[1] == (byte)0xD8) {
	        return "image/jpeg";
	    }
	    if (imageBytes[0] == 0x47 && imageBytes[1] == 0x49 && imageBytes[2] == 0x46) {
	        return "image/gif";
	    }
	    return "image/png"; // 預設
	}

	@Override
	public FamilyAvatarListResponse getFamilyByAccount(String account) throws JsonProcessingException {
	    if (account == null || account.isEmpty()) {
	        return new FamilyAvatarListResponse(ResponseMessages.MISSING_REQUIRED_FIELD);
	    }
	    List<FamilyAvatarVO> voList = new ArrayList<>();
	    List<Family> familyList = familyDao.selectAllFamily();

	    for (Family family : familyList) {
	        // 處理 owner avatar
	        User ownerUser = userDao.selectById(family.getOwner());
	        String ownerAvatar = null;
	        if (ownerUser != null && ownerUser.getAvatar() != null && ownerUser.getAvatar().length > 0) {
	            String mimeType = detectImageMimeType(ownerUser.getAvatar());
	            ownerAvatar = "data:" + mimeType + ";base64," + Base64.getEncoder().encodeToString(ownerUser.getAvatar());
	        }
	        SimpleUserAvatarVO ownerVO = new SimpleUserAvatarVO(
	            family.getOwner(),
	            ownerUser != null ? ownerUser.getName() : null,
	            ownerAvatar
	        );

	        // 處理 memberList avatar
	        List<String> invitorList;
	        String invitorContent = family.getInvitor();
	        if (invitorContent == null || invitorContent.trim().isEmpty()) {
	            invitorList = new ArrayList<>();
	        } else {
	            invitorList = mapper.readValue(invitorContent, new com.fasterxml.jackson.core.type.TypeReference<List<String>>() {});
	        }

	        List<SimpleUserAvatarVO> memberList = new ArrayList<>();
	        if (invitorList != null) {
	            for (String memberAccount : invitorList) {
	                User memberUser = userDao.selectById(memberAccount);
	                String memberAvatar = null;
	                if (memberUser != null && memberUser.getAvatar() != null && memberUser.getAvatar().length > 0) {
	                    String mimeType = detectImageMimeType(memberUser.getAvatar());
	                    memberAvatar = "data:" + mimeType + ";base64," + Base64.getEncoder().encodeToString(memberUser.getAvatar());
	                }
	                memberList.add(new SimpleUserAvatarVO(
	                    memberAccount,
	                    memberUser != null ? memberUser.getName() : null,
	                    memberAvatar
	                ));
	            }
	        }

	        // 判斷自己是不是 owner 或 invitor（只要有一項就加入回傳清單）
	        boolean isOwner = account.equals(family.getOwner());
	        boolean isInvitor = invitorList != null && invitorList.contains(account);

	        if (isOwner || isInvitor) {
	        	FamilyAvatarVO vo = new FamilyAvatarVO(
	                family.getId(),
	                family.getName(),
	                ownerVO,      // Owner 是物件
	                memberList    // 成員清單
	            );
	            voList.add(vo);
	        }
	    }
	    return new FamilyAvatarListResponse(ResponseMessages.SUCCESS, voList);
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
	        return new MemberNameResponse(
	            ResponseMessages.MISSING_REQUIRED_FIELD.getCode(),
	            "account 不可為空",
	            null
	        );
	    }
	    User user = userDao.selectById(account);  // 這裡要查完整 User 物件
	    if (user == null) {
	        return new MemberNameResponse(
	            ResponseMessages.ACCOUNT_NOT_FOUND.getCode(),
	            "查無此帳號",
	            null
	        );
	    }

	    // 處理 avatar (byte[] -> base64，判斷MIME)
	    String avatarBase64 = null;
	    byte[] avatarBytes = user.getAvatar();
	    if (avatarBytes != null && avatarBytes.length > 0) {
	        String mimeType = detectImageMimeType(avatarBytes);
	        avatarBase64 = "data:" + mimeType + ";base64," + Base64.getEncoder().encodeToString(avatarBytes);
	    }

	    MemberNameResponse.MemberData memberData =
	        new MemberNameResponse.MemberData(user.getName(), user.getAccount(), avatarBase64);

	    return new MemberNameResponse(ResponseMessages.SUCCESS.getCode(), "查詢成功", memberData);
	}
	
	/**
	 * 更新會員訂閱狀態
	 * @param account 會員帳號
	 * @param subscription 是否訂閱
	 */
	@Override
	@Transactional
	public BasicResponse updateSubscription(String account, Boolean subscription) {
	    // 1. 檢查必要欄位
	    if (account == null || account.isEmpty()) {
	        return new BasicResponse(ResponseMessages.MISSING_REQUIRED_FIELD);
	    }
	    // 2. 檢查帳號是否存在
	    int exists = userDao.selectCountByAccount(account);
	    if (exists == 0) {
	        return new BasicResponse(ResponseMessages.ACCOUNT_NOT_FOUND);
	    }
	    // 3. 訂閱到期日設為現在起一個月
	    LocalDateTime expirationDate = LocalDateTime.now().plusMonths(1);

	    // 4. 更新資料庫（DAO方法要對應LocalDateTime）
	    int updated = userDao.updateSubscription(account, subscription, expirationDate);
	    if (updated > 0) {
	        return new BasicResponse(ResponseMessages.SUCCESS);
	    } else {
	        return new BasicResponse(ResponseMessages.UPDATE_USER_FAIL);
	    }
	}

    /**
     * 查詢會員訂閱狀態
     * @param account 會員帳號
     * @return SubscriptionResponse，data 為 SubscriptionVO
     */
    @Override
    public SubscriptionResponse getSubscription(String account) {
        // 1. 檢查帳號參數是否有填寫
        if (account == null || account.isEmpty()) {
            return new SubscriptionResponse(ResponseMessages.MISSING_REQUIRED_FIELD, null);
        }
        // 2. 依帳號查詢會員資料
        User user = userDao.selectById(account);
        if (user == null) {
            // 找不到該帳號，回傳錯誤
            return new SubscriptionResponse(ResponseMessages.ACCOUNT_NOT_FOUND, null);
        }
        // 3. 組裝訂閱資訊回傳物件
        SubscriptionVO vo = new SubscriptionVO(
            user.isSubscription(),           // 是否訂閱
            user.getExpirationDate()         // 到期時間
        );
        // 4. 回傳查詢成功、帶訂閱資訊
        return new SubscriptionResponse(ResponseMessages.SUCCESS, vo);
    }
	
}
