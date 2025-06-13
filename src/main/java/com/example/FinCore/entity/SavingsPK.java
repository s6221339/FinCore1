package com.example.FinCore.entity;

import java.io.Serializable;
import java.util.Objects;

public class SavingsPK implements Serializable
{

	private static final long serialVersionUID = -8902926450607621711L;

	private int balanceId;

	private int year;

	private int month;
	
	public SavingsPK() {}

	public SavingsPK(int balanceId, int year, int month) {
		super();
		this.balanceId = balanceId;
		this.year = year;
		this.month = month;
	}

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

	@Override
	public int hashCode() {
		return Objects.hash(balanceId, month, year);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SavingsPK other = (SavingsPK) obj;
		return balanceId == other.balanceId && month == other.month && year == other.year;
	}
	
}
