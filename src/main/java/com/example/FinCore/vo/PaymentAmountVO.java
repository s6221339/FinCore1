package com.example.FinCore.vo;

/**
 * 每個款項統計資料以「類型」為依據，例如「飲食統計」「娛樂統計」等等。
 */
public record PaymentAmountVO(
		String type,
		int totalAmount
		) {

}
