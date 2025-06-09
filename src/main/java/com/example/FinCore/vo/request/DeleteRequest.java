package com.example.FinCore.vo.request;

import java.util.List;

import com.example.FinCore.constants.ConstantsMessage;

import jakarta.validation.constraints.NotEmpty;

public class DeleteRequest {

	@NotEmpty(message = ConstantsMessage.FAMILY_ID_VALUE_ERROR)
	private List<Integer> idList;

	public List<Integer> getIdList() {
		return idList;
	}

	public void setIdList(List<Integer> idList) {
		this.idList = idList;
	}
	
	
}
