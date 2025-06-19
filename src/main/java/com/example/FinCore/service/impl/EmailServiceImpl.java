package com.example.FinCore.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.FinCore.dao.UserDao;
import com.example.FinCore.dao.UserVerifyCodeDao;

@Service
public class EmailServiceImpl {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private UserDao userDao;

	@Autowired
	private UserVerifyCodeDao userVerifyCodeDao;

	public void sendVerificationCode(String toEmail, String subject, String content) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(toEmail);
		message.setSubject(subject);
		message.setText(content);

		mailSender.send(message);
	}

	/**
	 * 每天凌晨 12:05 刪除所有驗證碼資料
	 */
	@Scheduled(cron = "0 5 0 * * *")
	public void deleteVerification() {
		userVerifyCodeDao.deleteAll();
	}

	/**
	 * 每天凌晨 12:05 執行，將所有已驗證帳號的 verified 欄位設為 false（0）
	 */
	@Scheduled(cron = "0 5 0 * * *", zone = "Asia/Taipei")
	public void resetAllUserVerified() {
		userDao.resetAllVerified();
	}

}