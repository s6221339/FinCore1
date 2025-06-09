package com.example.FinCore.vo.request;

import java.util.Arrays;
import java.util.List;

import com.example.FinCore.constants.ConstantsMessage;

import jakarta.validation.constraints.NotBlank;

public class CreateFamilyRequest {
	
	private String name;

	@NotBlank(message = ConstantsMessage.PARAM_OWNER_ERROR)
	private String owner;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("CreateFamilyRequest：{");
		sb.append("name: '" + name + "', ");
		sb.append("owner: '" + owner + "', ");
		sb.append("invitor: " + (invitor == null ? "[]" : Arrays.toString(invitor.toArray())) + "}");
		String str = "";
		
		if(invitor == null)
			str = "[]";
		else
			str = Arrays.toString(invitor.toArray());
		
		return sb.toString();
	}

	

	

	
}
