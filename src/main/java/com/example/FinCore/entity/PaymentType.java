package com.example.FinCore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

/**
 * 款項種類，用來分類各個款項的項目，且使用者可自定義。
 */
@Table(name = "payment_type")
@Entity
@IdClass(value = PaymentTypePK.class)
public class PaymentType 
{
	
	@Id
	@Column(name = "type")
	private String type;
	
	@Id
	@Column(name = "item")
	private String item;
	
	@Id
	@Column(name = "account")
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
