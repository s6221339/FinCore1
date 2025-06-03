package com.example.FinCore.entity;

import java.time.LocalDate;

import jakarta.persistence.*;

/**
 * 款項，每個款項都依附在一個帳號之下
 */
@Table(name = "payment")
@Entity
public class Payment 
{
	
	@Id
	@Column(name = "payment_id")
	private int paymentId;
	
	@Column(name = "balance_id")
	private int balanceId;
	
	@Column(name = "description")
	private String description;
	
	/** 款項類型 */
	@Column(name = "type")
	private String type;
	
	/** 分類細項 */
	@Column(name = "item")
	private String item;
	
	@Column(name = "amount")
	private int amount;
	
	/** 循環週期，如果不為零代表該款項為循環款項 */
	@Column(name = "recurring_period")
	private int recurringPeriod;
	
	@Column(name = "create_date")
	private LocalDate createDate;
	
	@Column(name = "record_date")
	private LocalDate recordDate;
	
	@Column(name = "delete_date")
	private LocalDate deleteDate;
	
	@Column(name = "year")
	private int year;
	
	@Column(name = "month")
	private int month;
	
	@Column(name = "day")
	private int day;

	public int getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(int paymentId) {
		this.paymentId = paymentId;
	}

	public int getBalanceId() {
		return balanceId;
	}

	public void setBalanceId(int balanceId) {
		this.balanceId = balanceId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getRecurringPeriod() {
		return recurringPeriod;
	}

	public void setRecurringPeriod(int recurringPeriod) {
		this.recurringPeriod = recurringPeriod;
	}

	public LocalDate getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDate createDate) {
		this.createDate = createDate;
	}

	public LocalDate getRecordDate() {
		return recordDate;
	}

	public void setRecordDate(LocalDate recordDate) {
		this.recordDate = recordDate;
	}

	public LocalDate getDeleteDate() {
		return deleteDate;
	}

	public void setDeleteDate(LocalDate deleteDate) {
		this.deleteDate = deleteDate;
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

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}
	
}
