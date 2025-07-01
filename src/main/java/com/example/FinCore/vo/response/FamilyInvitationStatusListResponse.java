package com.example.FinCore.vo.response;

import java.util.List;

import com.example.FinCore.vo.FamilyInvitationStatusVO;

/**
 * 家族邀請狀態查詢 Response
 */
public class FamilyInvitationStatusListResponse extends BasicResponse {
    private List<FamilyInvitationStatusVO> list;

    public FamilyInvitationStatusListResponse(int code, String message, List<FamilyInvitationStatusVO> list) {
        super(code, message);
        this.list = list;
    }

    public List<FamilyInvitationStatusVO> getList() {
        return list;
    }

    public void setList(List<FamilyInvitationStatusVO> list) {
        this.list = list;
    }
}
	