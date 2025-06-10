package com.example.FinCore.vo;

import java.util.List;

public record FamilyVO(
		
		int id,
		
		String name,
		
		String owner,
		
		List<String> invitorList) {

}
