package com.example.FinCore.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.example.FinCore.vo.request.RegisterUserRequest;
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
import java.util.LinkedHashMap;

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
	
	
	@Autowired
	private EmailServiceImpl emailServiceImpl;

	private final ObjectMapper mapper = new ObjectMapper();

	@Override
	@Transactional
	public BasicResponse register(RegisterUserRequest req) {
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
	    // 1. 查詢帳號
	    User user = userDao.selectById(req.account());
	    if (user == null) {
	        return new BasicResponse(ResponseMessages.ACCOUNT_NOT_FOUND);
	    }

	    // 2. 比對舊密碼
	    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	    if (!encoder.matches(req.oldPassword(), user.getPassword())) {
	        return new BasicResponse(ResponseMessages.PASSWORD_NOT_MATCH);
	    }

	    // 3. 檢查新密碼是否與舊密碼相同（避免重複）
	    if (encoder.matches(req.newPassword(), user.getPassword())) {
	        return new BasicResponse(ResponseMessages.PASSWORD_DUPLICATE);
	    }

	    // 4. 新密碼加密再存回資料庫
	    String encodeNewPwd = encoder.encode(req.newPassword());
	    int update = userDao.updatePassword(user.getAccount(), encodeNewPwd);

	    if (update > 0) {
	        // 5. 修改成功後發送通知信給使用者
	        emailServiceImpl.sendVerificationCode(
	            user.getAccount(),
	            "【User系統】密碼已更改通知",
	            "您的帳號密碼已於 " + LocalDateTime.now() + " 成功變更，若非您本人操作，請盡快聯絡客服。"
	        );
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

	    // 自動檢查訂閱到期：如果已過期就設為未訂閱
	    boolean isSub = user.isSubscription();
	    LocalDateTime exp = user.getExpirationDate();
	    if (isSub && exp != null && exp.isBefore(LocalDateTime.now())) {
	        userDao.updateSubscription(user.getAccount(), false, null);
	        isSub = false;
	    }

	    String role = user.isSuperAdmin() ? "admin" : "user";
	    String subscription = isSub ? "subscribed" : "unsubscribed";

	    // 處理頭像 Base64
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
	        role,
	        subscription
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
	    User user = userDao.selectById(account);
	    if (user == null) {
	        return new BasicResponse(ResponseMessages.ACCOUNT_NOT_FOUND);
	    }

	    LocalDateTime now = LocalDateTime.now();
	    LocalDateTime expirationDate = null;

	    // 3. 如果是訂閱（subscription = true）
	    if (Boolean.TRUE.equals(subscription)) {
	        if (user.getExpirationDate() != null && user.getExpirationDate().isAfter(now)) {
	            // 尚未到期 → 續約從 expirationDate 再加一個月
	            expirationDate = user.getExpirationDate().plusMonths(1);
	        } else {
	            // 沒有訂閱或已過期 → 從現在起算一個月
	            expirationDate = now.plusMonths(1);
	        }
	        // 訂閱欄位設為 1
	        subscription = true;
	    } else {
	        // 取消訂閱：直接設為未訂閱，expirationDate 設 null
	        subscription = false;
	        expirationDate = null;
	    }

	    // 4. 更新資料庫
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
	    if (account == null || account.isEmpty()) {
	        return new SubscriptionResponse(ResponseMessages.MISSING_REQUIRED_FIELD, null);
	    }
	    User user = userDao.selectById(account);
	    if (user == null) {
	        return new SubscriptionResponse(ResponseMessages.ACCOUNT_NOT_FOUND, null);
	    }
	    // 判斷訂閱是否過期（這段可加可不加，防止過期還顯示true）
	    boolean isSub = user.isSubscription();
	    LocalDateTime exp = user.getExpirationDate();
	    if (isSub && exp != null && exp.isBefore(LocalDateTime.now())) {
	        // 過期了就直接在回傳前顯示 false（資料庫不自動寫入，僅回傳結果正確）
	        isSub = false;
	    }
	    // 建立回傳物件
	    SubscriptionVO vo = new SubscriptionVO(isSub, user.getExpirationDate());
	    return new SubscriptionResponse(ResponseMessages.SUCCESS, vo);
	}
    
    /**
     * 產生綠界金流 ECPay 的訂單參數，用於前端提交到藍新付款。
     * 金額固定為 60 元，商品名稱固定為 "VIP Subscription"。
     * @param account 會員帳號（可用於未來記錄訂單使用）
     * @return 回傳 ECPay 所需的表單欄位參數 (包含 CheckMacValue)
     */
    @Override
    public Map<String, String> getECPayForm(String account) {
        Map<String, String> params = new LinkedHashMap<>();
        
        // 藍新金流測試環境參數
        String MerchantID = "2000132";
        String HashKey = "5294y06JbISpM5x9";
        String HashIV = "v77hoKGq4kWxNNIS";
        
        // 訂單固定資料
        String MerchantTradeNo = account + "_" + System.currentTimeMillis();
        String MerchantTradeDate = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        String PaymentType = "aio";
        String TradeDesc = "SubscriptionPayment";
        String ItemName = "VIP Subscription";
        String ReturnURL = "http://localhost:8080/finbook/user/handleECPayNotify"; // TODO: 換成你的
        String ChoosePayment = "ALL";
        int amount = 60; // 固定 60 元

        // 填入參數
        params.put("MerchantID", MerchantID);
        params.put("MerchantTradeNo", MerchantTradeNo);
        params.put("MerchantTradeDate", MerchantTradeDate);
        params.put("PaymentType", PaymentType);
        params.put("TotalAmount", String.valueOf(amount));
        params.put("TradeDesc", TradeDesc);
        params.put("ItemName", ItemName);
        params.put("ReturnURL", ReturnURL);
        params.put("ChoosePayment", ChoosePayment);

        // 組成待加密字串
        String raw = "HashKey=" + HashKey + "&" +
                     "ChoosePayment=" + ChoosePayment + "&" +
                     "ItemName=" + ItemName + "&" +
                     "MerchantID=" + MerchantID + "&" +
                     "MerchantTradeDate=" + MerchantTradeDate + "&" +
                     "MerchantTradeNo=" + MerchantTradeNo + "&" +
                     "PaymentType=" + PaymentType + "&" +
                     "ReturnURL=" + ReturnURL + "&" +
                     "TotalAmount=" + amount + "&" +
                     "TradeDesc=" + TradeDesc + "&" +
                     "HashIV=" + HashIV;

        // URL encode 並小寫
        try {
            raw = java.net.URLEncoder.encode(raw, "UTF-8")
                     .replace("%21", "!")
                     .replace("%28", "(")
                     .replace("%29", ")")
                     .replace("%2A", "*")
                     .replace("%2D", "-")
                     .replace("%2E", ".")
                     .replace("%5F", "_")
                     .toLowerCase();
        } catch (Exception e) {
            throw new RuntimeException("URLEncode error", e);
        }

        // 做 MD5 並轉大寫
        String checkMacValue;
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(raw.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02X", b));
            }
            checkMacValue = sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("MD5 error", e);
        }

        params.put("CheckMacValue", checkMacValue);
        return params;
    }
    
    /**
     * 處理綠界 notify，收到付款成功即啟用訂閱
     * @param merchantTradeNo 訂單編號（格式: account_時間戳）
     * @param rtnCode 藍新付款結果 (1=成功)
     * @return 回傳給藍新的字串，需固定 "1|OK"
     */
    @Override
    @Transactional
    public String handleECPayNotify(String merchantTradeNo, String rtnCode) {
        // 從訂單編號拆出帳號
        String account = merchantTradeNo.split("_")[0];

        // rtnCode=1 表示付款成功
        if ("1".equals(rtnCode)) {
            updateSubscription(account, true);
        }
        
        System.out.println("[ECPay Notify] merchantTradeNo: " + merchantTradeNo + ", rtnCode: " + rtnCode);
        
        // 根據藍新文件規定，需回傳 "1|OK"
        return "1|OK";
        
    }
	
}
