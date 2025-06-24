package com.example.FinCore.vo.request;

/**
 * FamilyInvitationRequest
 * <p>
 * 前端建立或修改家族邀請時的請求物件（DTO）。
 */
public class FamilyInvitationRequest {
    /** 被邀請者帳號 */
    private String account;

    /** 家族ID */
    private int familyId;

    /** 狀態 */
    private boolean status;

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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}