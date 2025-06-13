package com.example.FinCore.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * 轉帳紀錄實體，用來記錄從某個帳戶到某個帳戶的轉帳操作
 */
@Table(name = "transfers")
@Entity
public class Transfers 
{
	
	@Id
	@Column(name = "id")
	private int id;
	
	@Column(name = "from_balance")
	private int fromBalance;
	
	@Column(name = "to_balance")
	private int toBalance;
	
	@Column(name = "amount")
	private int amount;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "create_date")
	private LocalDate createDate;
	
	@Column(name = "year")
	private int year;
	
	@Column(name = "month")
	private int month;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFromBalance() {
		return fromBalance;
	}

	public void setFromBalance(int fromBalance) {
		this.fromBalance = fromBalance;
	}

	public int getToBalance() {
		return toBalance;
	}

	public void setToBalance(int toBalance) {
		this.toBalance = toBalance;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDate createDate) {
		this.createDate = createDate;
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
	
	
	
}
