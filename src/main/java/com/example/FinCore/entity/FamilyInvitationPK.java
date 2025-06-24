package com.example.FinCore.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * FamilyInvitationPK
 * <p>
 * 家族邀請的複合主鍵類別，對應 FamilyInvitation Entity 的 account + familyId 組合。
 * 必須實作 Serializable，並正確覆寫 equals 與 hashCode 方法。
 */
public class FamilyInvitationPK implements Serializable {

	private static final long serialVersionUID = 7410615437836654025L;

	/** 被邀請者帳號 */
    private String account;

    /** 家族ID */
    private int familyId;

    /**
     * 無參數建構子
     * 必須要有，JPA 會自動使用
     */
    public FamilyInvitationPK() {
    }

    /**
     * 帶參數建構子
     * @param account 被邀請者帳號
     * @param familyId 家族ID
     */
    public FamilyInvitationPK(String account, int familyId) {
        this.account = account;
        this.familyId = familyId;
    }

    // Getter 和 Setter
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

    /**
     * equals() 必須正確判斷兩個 PK 是否相同
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FamilyInvitationPK)) return false;
        FamilyInvitationPK that = (FamilyInvitationPK) o;
        return familyId == that.familyId &&
                Objects.equals(account, that.account);
    }

    /**
     * hashCode() 必須符合 equals 規則
     */
    @Override
    public int hashCode() {
        return Objects.hash(account, familyId);
    }
}
