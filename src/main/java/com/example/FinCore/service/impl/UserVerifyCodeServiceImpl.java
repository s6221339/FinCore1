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
	 * 忘記密碼：重設密碼（必須已驗證過）
	 */
	@Override
	public BasicResponse updatePwdByEmail(UpdatePwdByEmailRequest req) {
		User user = userDao.selectById(req.getAccount());
		// 檢查帳號是否存在
		if (user == null) {
			return new BasicResponse(ResponseMessages.NOT_FOUND);
		}
		// 若是驗證狀態還是 false 代表尚未驗證
		if (!user.isVerified()) {
			return new BasicResponse(ResponseMessages.VERIFICATION_FAILED);
		}
		// 修改密碼
		userDao.updatePassword(req.getAccount(), encoder.encode(req.getNewPassword()));
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

}
