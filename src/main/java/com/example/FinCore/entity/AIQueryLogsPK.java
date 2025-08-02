package com.example.FinCore.entity;

import java.io.Serializable;
import java.util.Objects;

public class AIQueryLogsPK implements Serializable 
{

	private static final long serialVersionUID = -6275477850526909065L;
	
	private String account;
	
	private int year;
	
	private int month;

	public AIQueryLogsPK() {
		super();
	}

	public AIQueryLogsPK(String account, int year, int month) {
		super();
		this.account = account;
		this.year = year;
		this.month = month;
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

	@Override
	public int hashCode() {
		return Objects.hash(account, month, year);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AIQueryLogsPK other = (AIQueryLogsPK) obj;
		return Objects.equals(account, other.account) && month == other.month && year == other.year;
	}

}
