package com.example.FinCore.vo.request;

import com.example.FinCore.constants.ConstantsMessage;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/**
 * 家庭邀請操作 Request VO
 * 可用於接受、拒絕邀請等行為
 */
public class InviteRequest {
    /** 受邀人帳號 */
	@NotBlank(message = ConstantsMessage.PARAM_ACCOUNT_ERROR)
    private String account;
    /** 家族ID */
	@Min(value = 1, message = ConstantsMessage.PARAM_FAMILY_ID_VALUE_ERROR )
    private int familyId;

    // 若你未來要支援 accept/reject 可以加 actionType 欄位
    // private String actionType; // "accept" or "reject"

    public InviteRequest() {}

    public InviteRequest(String account, int familyId) {
        this.account = account;
        this.familyId = familyId;
    }

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

    // 如果有 actionType 再加
    // public String getActionType() { return actionType; }
    // public void setActionType(String actionType) { this.actionType = actionType; }
}
