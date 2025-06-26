package com.example.FinCore.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "ai_query_logs")
@Entity
public class AIQueryLogs 
{
	
	@Id
	@Column(name = "id")
	private int id;
	
	@Column(name = "balance_id")
	private int balanceId;
	
	@Column(name = "query_text")
	private String queryText;
	
	@Column(name = "response_text")
	private String responseText;
	
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

	public int getBalanceId() {
		return balanceId;
	}

	public void setBalanceId(int balanceId) {
		this.balanceId = balanceId;
	}

	public String getQueryText() {
		return queryText;
	}

	public void setQueryText(String queryText) {
		this.queryText = queryText;
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
