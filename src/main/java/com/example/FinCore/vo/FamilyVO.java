package com.example.FinCore.vo;

import java.util.List;

public record FamilyVO(
		
	    int id,
	    
	    String name,
	    
	    SimpleUserVO owner,
	    
	    List<SimpleUserVO> memberList
	) {}
