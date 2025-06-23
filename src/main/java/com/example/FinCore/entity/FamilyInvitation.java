package com.example.FinCore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * 家族邀請
 * 
 * 家庭群組owner發出邀請後，確認邀請的狀態
 */
@Entity
@Table(name = "family_invitation")
public class FamilyInvitation {
	
	@Id
    @Column(name = "account")
    private String account;
	
	@Id
    @Column(name = "family_id")
    private int familyId;
	
	@Column(name = "status")
	private boolean status;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public int getFamilyId() {
		return familyId;
	}

	public void setFamilyId(int familyId) {
		this.familyId = familyId;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	
}
