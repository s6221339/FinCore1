package com.example.FinCore.entity;

import java.io.Serializable;

public class PaymentTypePK implements Serializable
{

	private static final long serialVersionUID = -2243780931139845890L;
	
	private String type;
	
	private String item;
	
	private String account;

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

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
	
}
