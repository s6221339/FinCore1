package com.example.FinCore.vo;

import java.time.LocalDate;

public record UserVO(
		
		String account, 
		
		String name, 
		
		String phone,
		
		LocalDate birthday,
		
		String avatar,
		
		String role,
		
		String subscription
		) {
	

}
