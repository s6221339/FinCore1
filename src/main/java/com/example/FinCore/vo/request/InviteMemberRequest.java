package com.example.FinCore.vo.request;

import java.util.List;

import com.example.FinCore.constants.ConstantsMessage;

import jakarta.validation.constraints.NotEmpty;

/**
 * 家族成員邀請請求物件
 * familyId：家族ID
 * owner：操作者（發起邀請的人，一定是 owner）
 * invitor：被邀請的成員帳號清單
 */
public record InviteMemberRequest(
		
		int familyId, 
		
		String owner, 
		
		@NotEmpty(message = ConstantsMessage.PARAM_INVITOR_ERROR)
		List<String> invitor) {

}
