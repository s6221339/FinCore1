package com.example.FinCore.vo;

import java.util.List;

public record FamilyAvatarVO (
		
	    int id,
	    
	    String name,
	    
	    SimpleUserAvatarVO owner,
	    
	    List<SimpleUserAvatarVO> memberList
	) {}
