package com.example.FinCore.vo;

public record FamilyInvitationStatusVO(
	    int familyId,
	    String familyName,
	    String statusText // 這裡直接回傳邀請狀態描述
	) {}
    



