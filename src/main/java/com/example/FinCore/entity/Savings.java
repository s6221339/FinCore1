package com.example.FinCore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

/**
 * 使用者自定義的儲蓄金額，附帶於「帳戶」底下，並與年月平行
 */
@Table(name = "savings")
@Entity
@IdClass(value = SavingsPK.class)
public class Savings 
{
	
	@Id
	@Column(name = "balance_id")
	private int balanceId;
	
	@Id
	@Column(name = "year")
	private int year;
	
	@Id
	@Column(name = "month")
	private int month;
	
	@Column(name = "amount")
	private int amount;

	public int getBalanceId() {
		return balanceId;
	}

	public void setBalanceId(int balanceId) {
		this.balanceId = balanceId;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
	
}
