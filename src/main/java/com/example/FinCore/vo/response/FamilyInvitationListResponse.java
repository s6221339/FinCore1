package com.example.FinCore.vo.response;

import java.util.List;

public class FamilyInvitationListResponse extends BasicResponse {
	
	private int familyId;
	
	private List<InviteeInfo> inviteeList;

	public static class InviteeInfo {
		private String account;
		
		private String name;

		public String getAccount() {
			return account;
		}

		public void setAccount(String account) {
			this.account = account;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
		
	}

	public int getFamilyId() {
		return familyId;
	}

	public void setFamilyId(int familyId) {
		this.familyId = familyId;
	}

	public List<InviteeInfo> getInviteeList() {
		return inviteeList;
	}

	public void setInviteeList(List<InviteeInfo> inviteeList) {
		this.inviteeList = inviteeList;
	}
	
	
	
}
