package com.example.FinCore.vo.response;

public class FamilyInvitationResponse extends BasicResponse {

    /** 被邀請者帳號 */
    private String account;

    /** 家族ID */
    private int familyId;

    /** 狀態 */
    private boolean status;

    // Getter/Setter
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