package com.example.FinCore.vo.response;

import com.example.FinCore.constants.ResponseMessages;
import com.example.FinCore.vo.FamilyVO;

/**
 * 查詢家族資訊 Response
 */
public class FamilyIdResponse extends BasicResponse {

    private FamilyVO family;

    // 成功時建構子
    public FamilyIdResponse(int code, String message, FamilyVO family) {
        super(code, message);
        this.family = family;
    }

    // 僅錯誤回傳時用
    public FamilyIdResponse(ResponseMessages messages) {
        super(messages);
    }

    public FamilyVO getFamily() {
        return family;
    }

    public void setFamily(FamilyVO family) {
        this.family = family;
    }
}