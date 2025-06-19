package com.example.FinCore.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_verify_code")
public class UserVerifyCode {

	 @Id
	 @Column(name = "account")
	 private String account;
	
	 @Column(name = "code")
	 private String code;
    
	 @Column(name = "expire_at")
	 private LocalDateTime expireAt;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public LocalDateTime getExpireAt() {
		return expireAt;
	}

	public void setExpireAt(LocalDateTime expireAt) {
		this.expireAt = expireAt;
	}
	 
	 
}