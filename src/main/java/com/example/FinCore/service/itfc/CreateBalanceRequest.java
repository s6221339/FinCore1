package com.example.FinCore.service.itfc;

import com.example.FinCore.constants.ConstantsMessage;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/**
 * 創建帳戶專用的請求資料，注意群組編號和帳號是不特別檢查的。
 */
public record CreateBalanceRequest(
		@Min(value = 0, message = ConstantsMessage.FAMILY_ID_VALUE_ERROR)
		int familyId,
		
		String account,
		
		@NotBlank(message = ConstantsMessage.EMPTY_NAME_ERROR)
		String name
		) 
{

}
