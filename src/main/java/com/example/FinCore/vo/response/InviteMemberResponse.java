package com.example.FinCore.vo.response;

import java.util.List;

import com.example.FinCore.vo.SimpleUserVO;

public class InviteMemberResponse extends BasicResponse {
	
    private List<SimpleUserVO> invitedMembers;

    public InviteMemberResponse(int code, String message, List<SimpleUserVO> invitedMembers) {
        super(code, message);
        this.invitedMembers = invitedMembers;
    }

    public List<SimpleUserVO> getInvitedMembers() {
        return invitedMembers;
    }
}
