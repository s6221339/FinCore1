package com.example.FinCore.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * 帳戶實體，帳戶依附在帳號或群組（家庭）底下，具有名稱（name）和創建日期（create_date）
 * 屬性，另外有所屬帳號（account）以及群組編號（famliy_id），並以自己的編號（balance_id）
 * 為主鍵。
 */
@Table(schema = "balance")
@Entity
public class Balance 
{
	
	@Id
	@Column(name = "balance_id")
	private int balanceId;
	
	@Column(name = "famliy_id")
	private int famliyId;
	
	@Column(name = "account")
	private String account;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "create_date")
	private LocalDate createDate;

	public int getBalanceId() {
		return balanceId;
	}

	public void setBalanceId(int balanceId) {
		this.balanceId = balanceId;
	}

	public int getFamliyId() {
		return famliyId;
	}

	public void setFamliyId(int famliyId) {
		this.famliyId = famliyId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDate createDate) {
		this.createDate = createDate;
	}
	
}
