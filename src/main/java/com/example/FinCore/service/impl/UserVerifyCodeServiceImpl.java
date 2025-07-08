package com.example.FinCore.service.impl;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.FinCore.constants.ResponseMessages;
import com.example.FinCore.dao.UserDao;
import com.example.FinCore.dao.UserVerifyCodeDao;
import com.example.FinCore.entity.User;
import com.example.FinCore.entity.UserVerifyCode;
import com.example.FinCore.service.itfc.UserVerifyCodeService;
import com.example.FinCore.vo.request.UpdatePwdByEmailRequest;
import com.example.FinCore.vo.response.BasicResponse;

@Service
public class UserVerifyCodeServiceImpl implements UserVerifyCodeService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private UserVerifyCodeDao userVerifyCodeDao;

	@Autowired
	private EmailServiceImpl emailServiceImpl;

	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	/**
	 * 更改密碼
	 * 發送驗證信
	 */
	@Override
	public BasicResponse sendVerifyCode(String account) {
		User user = userDao.selectById(account);
		if (user == null) {
			return new BasicResponse(ResponseMessages.NOT_FOUND);
		}
		String code = randomCodeGenerator();
		// 將驗證碼寫入 user_verify_code 表
		userVerifyCodeDao.insertOrUpdateVerified(code, LocalDateTime.now().plusMinutes(10), account);

		emailServiceImpl.sendVerificationCode(
			account,
			"【User系統】更改密碼驗證碼通知",
			"您的驗證碼是： " + code + " ，請在10分鐘內完成驗證"
		);
		return new BasicResponse(ResponseMessages.SUCCESS); 
	}

	/**
	 * 更改密碼
	 * 認證驗證碼
	 */
	@Override
	public BasicResponse checkVerifyCode(String code, String account) {
		UserVerifyCode userVerifyCode = userVerifyCodeDao.selectById(account);
		// 沒有驗證碼紀錄代表調用錯 API
		if (userVerifyCode == null) {
			return new BasicResponse(ResponseMessages.FAILED);
		}
		// 驗證碼錯誤
		if (!userVerifyCode.getCode().equals(code)) {
			return new BasicResponse(ResponseMessages.VERIFICATION_FAILED);
		}
		// 驗證碼過期
		if (LocalDateTime.now().isAfter(userVerifyCode.getExpireAt())) {
			return new BasicResponse(ResponseMessages.VERIFICATION_CODE_VALID);
		}
		// 驗證成功，設 user 表 verified = true
		userDao.updateVerified(account);
		// 驗證成功後刪除驗證碼
		userVerifyCodeDao.deleteByAccount(account);

		return new BasicResponse(ResponseMessages.SUCCESS);
	}
	
	/**
	 * 使用者透過 Email 驗證後修改密碼。
	 *
	 * @param req 包含帳號與新密碼的請求物件
	 * @return BasicResponse 返回執行結果，可能為帳號不存在、未驗證、密碼重複或成功
	 */
	@Override
	public BasicResponse updatePwdByEmail(UpdatePwdByEmailRequest req) {
	    User user = userDao.selectById(req.getAccount());
	    // 1. 檢查帳號是否存在
	    if (user == null) {
	        return new BasicResponse(ResponseMessages.NOT_FOUND);
	    }
	    // 2. 檢查是否已經驗證過
	    if (!user.isVerified()) {
	        return new BasicResponse(ResponseMessages.VERIFICATION_FAILED);
	    }
	    // 3. 比對新舊密碼是否相同（防止使用者重複設置相同密碼）
	    if (encoder.matches(req.getNewPassword(), user.getPassword())) {
	        return new BasicResponse(ResponseMessages.PASSWORD_DUPLICATE);
	    }
	    // 4. 修改密碼
	    userDao.updatePassword(req.getAccount(), encoder.encode(req.getNewPassword()));
	    // 5. 修改完密碼後，把 verified 設回 false(0)
	    userDao.updateVerifiedFalse(req.getAccount());

	    // 6. 發送通知信告知使用者密碼已被修改
	    emailServiceImpl.sendVerificationCode(
	        req.getAccount(),
	        "【User系統】密碼已更改通知",
	        "您的帳號密碼已於 " + LocalDateTime.now() + " 成功變更，若非您本人操作，請盡快聯絡客服。"
	    );

	    return new BasicResponse(ResponseMessages.SUCCESS);
	}

	private String randomCodeGenerator() {
		String charPool = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();

		String code = "";
		for (int i = 0; i < 8; i++) {
			int index = random.nextInt(charPool.length());
			code += charPool.charAt(index);
		}
		return code;
	}
	
	/**
	 * 註冊會員時，發送驗證信
	 * 不檢查帳號是否存在（因為還沒註冊）
	 */
	@Override
	public BasicResponse sendRegisterVerifyCode(String account) {
	    String code = randomCodeGenerator();
	    // 將驗證碼寫入 user_verify_code 表
	    userVerifyCodeDao.insertOrUpdateVerified(code, LocalDateTime.now().plusMinutes(10), account);

	    emailServiceImpl.sendVerificationCode(
	        account,
	        "【User系統】註冊驗證碼通知",
	        "您的註冊驗證碼是： " + code + " ，請在10分鐘內完成驗證"
	    );
	    return new BasicResponse(ResponseMessages.SUCCESS);
	}
	
	/**
	 * 註冊會員時，認證驗證碼
	 * 不檢查 verified，只確認驗證碼正確與有效
	 */
	@Override
	public BasicResponse checkRegisterVerifyCode(String code, String account) {
	    UserVerifyCode userVerifyCode = userVerifyCodeDao.selectById(account);
	    // 沒有驗證碼紀錄代表呼叫錯 API
	    if (userVerifyCode == null) {
	        return new BasicResponse(ResponseMessages.FAILED);
	    }
	    // 驗證碼錯誤
	    if (!userVerifyCode.getCode().equals(code)) {
	        return new BasicResponse(ResponseMessages.VERIFICATION_FAILED);
	    }
	    // 驗證碼過期
	    if (LocalDateTime.now().isAfter(userVerifyCode.getExpireAt())) {
	        return new BasicResponse(ResponseMessages.VERIFICATION_CODE_VALID);
	    }
	    // 驗證成功後刪除驗證碼
	    userVerifyCodeDao.deleteByAccount(account);
	    return new BasicResponse(ResponseMessages.SUCCESS);
	}

}
