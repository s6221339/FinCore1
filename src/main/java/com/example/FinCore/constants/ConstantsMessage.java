package com.example.FinCore.constants;

public class ConstantsMessage {

	public final static String PARAM_OWNER_ERROR = "參數 owner 錯誤!!";

	public final static String PARAM_INVITOR_ERROR = "參數 invitor 錯誤!!";

	public final static String PARAM_CREATE_DATE_ERROR = "參數 create date 錯誤!!";

	public final static String PARAM_FAMILY_ID_VALUE_ERROR = "參數 family id 不能小於1!!";

	public final static String PARAM_ACCOUNT_ERROR = "參數 account 錯誤!!";

	public final static String PARAM_NAME_ERROR = "參數 name 錯誤!!";

	public final static String PARAM_PASSWORD_ERROR = "參數 password 錯誤!!";

	public final static String PARAM_TYPE_BLANK_ERROR = "參數「type」空白！";

	public final static String PARAM_ITEM_BLANK_ERROR = "參數「item」空白！";

	public final static String PARAM_ACCOUNT_BLANK_ERROR = "參數「account」空白！";

	public static final String FAMILY_ID_VALUE_ERROR = "「familyId」不得小於 1";

	public final static String BALANCE_ID_VALUE_ERROR = "「balanceId」不得小於 1";

	public final static String PAYMENT_ID_VALUE_ERROR = "「paymentId」不得小於 1";

	public final static String AMOUNT_NEGATIVE_ERROR = "「amount」不得為負數";

	public final static String RECURRING_PERIOD_NEGATIVE_ERROR = "循環週期不得為負數";

	public final static String INVALID_RECORD_DATE = "無效的「recordDate」";

	public final static String EMPTY_NAME_ERROR = "名稱不可為空白！";

	public final static String SAVINGS_VALUE_ERROR = "「savings」不得為負數！";

	public final static String INVALID_DATE_ERROR = "無效的日期索引";

	public final static String EMAIL_IS_NECESSARY = "必填欄位：請輸入電子郵件";

	public static final String EMAIL_PATTERN = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

	public final static String EMAIL_ADDRESS_IS_INVALID = "電子郵件地址無效：此信箱不存在或無法驗證";

	public final static String NEW_PASSWORD_IS_NECESSARY = "必填欄位：請輸入新密碼";

	public final static String PARAM_PASSWORD_LENGTH_ERROR = "密碼長度不符：密碼長度需符合規範（例如 8~16 字元）";

	public final static String PASSWORD_PATTERN = "";

	public final static String PARAM_PASSWORD_COMPLEXITY_ERROR = "密碼複雜度不足：密碼太簡單，請提高安全性（建議加上特殊字元）";

	private ConstantsMessage() {
	}

}
