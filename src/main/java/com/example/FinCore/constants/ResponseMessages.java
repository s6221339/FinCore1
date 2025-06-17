package com.example.FinCore.constants;

public enum ResponseMessages 
{
	
	SUCCESS(200, "成功！"),
	ACCOUNT_NOT_FOUND(404, "查無此帳號！"),
	BALANCE_NOT_FOUND(404, "查無此帳戶！"),
	PAYMENT_NOT_FOUND(404, "查無此帳款！"),
	FAMLIY_NOT_FOUND(404, "查無此群組（家庭）！"),
	TRANSFERS_NOT_FOUND(404, "查無此轉帳紀錄！"),
	ITEM_EXISTED(400, "已存在相同物件。"),
	PAST_RECORD_DATE(400, "該款項不能記錄在過去時間。"),
	FUTURE_RECORD_DATE(400, "該款項不能記錄在未來時間。"),
	FUTURE_SEARCH_DATE(400, "無法搜尋未來的時間"),
	BAD_REQUEST(400, "請求參數錯誤"),
    FORBIDDEN(403, "無權限"),
    NOT_FOUND(404, "查無資料"),
	MISSING_REQUIRED_FIELD(400, "缺少必要欄位"),
    DUPLICATE_MEMBER(400, "群組擁有者與邀請人不能相同"),
    OWNER_NOT_FOUND(404, "擁有者帳號不存在"),
    INVITOR_NOT_FOUND(404, "邀請人帳號不存在"),
    CREATE_FAMILY_FAIL(400, "新增家族群組失敗"),
    FAMILY_NOT_FOUND(404,"家族群組不存在"),
	UPDATE_FAMILY_FAIL(400,"更新家族群組失敗"),
	DELETE_FAMILY_FAIL(400, "刪除家庭群組失敗"),
	NO_PERMISSION(403, "沒有 owner 權限"),
	MEMBER_NOT_FOUND(404,"家族成員不存在"),
	PAYMENT_HAS_BEEN_DELETED(400, "無法更新已刪除的款項資料。"),
	BALANCE_WITH_NO_OWNER(400, "帳戶不能沒有所屬對象。"),
	BALANCE_WITH_MULTIPLE_OWNER(400, "帳戶所屬對象只能是唯一。"),
	UPDATE_USER_FAIL(400, "更新會員失敗"),
	ACCOUNT_EXIST(400, "帳號已存在"),
	DUPLICATE_FAMILY_MEMBERS(400, "家庭成員重複"),
	PASSWORD_NOT_MATCH(400, "密碼不一致"),
	NULL_SAVINGS_VALUE(500, "Savings取值錯誤。原因：該帳戶不存在 Savings 設定"),
	BALANCE_ACTIVATION(400, "帳戶未停用，無法刪除"),
	SAME_BALANCE_OPERATION(400, "無法對同一個帳戶進行操作"),
	ACCOUNT_NOT_IN_FAMILY(400, "帳號不存在於此家庭群組");
	
	private int code;
	
	private String message;

	private ResponseMessages(int code, String message) 
	{

		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

}
