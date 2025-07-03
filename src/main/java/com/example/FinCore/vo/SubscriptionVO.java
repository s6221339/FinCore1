package com.example.FinCore.vo;

import java.time.LocalDateTime;

public record SubscriptionVO(
		
		Boolean subscription,
		
		LocalDateTime expirationDate) {}