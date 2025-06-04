package com.example.FinCore.vo.request;

import java.util.List;

import com.example.FinCore.constants.ConstantsMessage;

import jakarta.validation.constraints.NotBlank;

public class CreateFamilyRequest {
	
	@NotBlank(message = ConstantsMessage.PARAM_OWNER_ERROR)
	private String owner;
	
	// @NotEmpty(message = ConstantsMessage.PARAM_INVITOR_ERROR)<----
	// @NotBlank 只能用在 String 欄位上。如果你要驗證 List 不是 null、不是空，應該用 @NotEmpty
	@NotBlank(message = ConstantsMessage.PARAM_INVITOR_ERROR)
	private List<String> invitor;

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public List<String> getInvitor() {
		return invitor;
	}

	public void setInvitor(List<String> invitor) {
		this.invitor = invitor;
	}



	

	
}
