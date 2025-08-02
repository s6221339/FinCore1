package com.example.FinCore.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Table(name = "ai_query_logs")
@Entity
@IdClass(value = AIQueryLogsPK.class)
public class AIQueryLogs 
{
	
	@Id
	@Column(name = "account")
	private String account;
	
	@Id
	@Column(name = "year")
	private int year;
	
	@Id
	@Column(name = "month")
	private int month;
	
	@Column(name = "response_text")
	private String responseText;
	
	@Column(name = "create_date")
	private LocalDate createDate;
	
	public AIQueryLogs() {
		super();
	}

	public AIQueryLogs(String account, int year, int month, String responseText, LocalDate createDate) {
		super();
		this.account = account;
		this.year = year;
		this.month = month;
		this.responseText = responseText;
		this.createDate = createDate;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
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

	public String getResponseText() {
		return responseText;
	}

	public void setResponseText(String responseText) {
		this.responseText = responseText;
	}

	public LocalDate getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDate createDate) {
		this.createDate = createDate;
	}

}
