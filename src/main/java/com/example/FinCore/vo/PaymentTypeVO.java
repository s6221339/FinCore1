package com.example.FinCore.vo;

/**
 * PaymentType 的值對象，捨棄了帳號項目。
 */
public record PaymentTypeVO(
		String type,
		String item
		) {

}
