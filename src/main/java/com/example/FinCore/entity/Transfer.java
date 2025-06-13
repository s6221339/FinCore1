package com.example.FinCore.entity;

import jakarta.persistence.Table;

@Table(name = "transfer")
public class Transfer 
{
	
	private int id;
	
	private String fromBalance;
	
	private String toBalance;
	
	private int amount;
	
	private String description;
	
	
	
}
