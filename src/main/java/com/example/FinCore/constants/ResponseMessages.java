package com.example.FinCore.constants;

public enum ResponseMessages 
{
	
	SUCCESS(200, "成功！"),
	VERIFICATION_CODE_VALID(200, "驗證碼有效！"),
	
	FAILED(400, "操作失敗"),
	EMPTY_LIST(400, "不得為空陣列！"),
	BAD_REQUEST(400, "請求參數錯誤"),
	ACCOUNT_EXIST(400, "帳號已存在"),
	ITEM_EXISTED(400, "已存在相同物件。"),
	UPDATE_USER_FAIL(400, "更新會員失敗"),
	PASSWORD_NOT_MATCH(400, "密碼錯誤"),
    UPDATE_FAMILY_FAIL(400,"更新家族群組失敗"),
    CREATE_FAMILY_FAIL(400, "新增家族群組失敗"),
	DELETE_FAMILY_FAIL(400, "刪除家庭群組失敗"),
	MISSING_REQUIRED_FIELD(400, "缺少必要欄位"),
	FUTURE_SEARCH_DATE(400, "無法搜尋未來的時間"),
	DUPLICATE_FAMILY_MEMBERS(400, "家庭成員重複"),
	BALANCE_ACTIVATION(400, "帳戶未停用，無法刪除"),
	PAST_RECORD_DATE(400, "該款項不能記錄在過去時間。"),
    DUPLICATE_MEMBER(400, "群組擁有者與受邀人不能相同"),
	FUTURE_RECORD_DATE(400, "該款項不能記錄在未來時間。"),
	BALANCE_WITH_NO_OWNER(400, "帳戶不能沒有所屬對象。"),
	ACCOUNT_NOT_IN_FAMILY(400, "帳號不存在於此家庭群組"),
	SAME_BALANCE_OPERATION(400, "無法對同一個帳戶進行操作"),
	DELETED_PAYMENT_CANNOT_UPDATE(400, "無法更新已刪除的款項資料。"),
	BALANCE_WITH_MULTIPLE_OWNER(400, "帳戶所屬對象只能是唯一。"),
	SAME_OWNER_TRANSFER_INVALID(400, "新舊 owner 相同，無法進行轉讓"),
	VERIFICATION_FAILED(400, "驗證失敗：驗證碼錯誤或已過期"),
	PAYMENT_HAS_BEEN_DELETED(400, "該帳款已刪除"),
	PAYMENT_PERIOD_UNABLE_MODIFYING(400, "無法變更該款項的週期"),
	INVITATION_ALREADY_SENT(400, "成員已發出邀請，請勿重複邀請："),
	INVITATION_ALREADY_ACCEPTED(400, "該邀請已被接受，無法重複操作！"),
	OWNER_CANNOT_KICK_SELF(400, "群組擁有者無法踢除自己"),
	PLEASE_LOGIN_FIRST(400, "請先登入！"),
	LOGIN_INFO_NOT_SYNC(400, "登入資料不同步"),
	UNABLE_TRANSFERS_TO_DIFF_FAMILY_ACCOUNT(400, "無法對不同家庭群組的帳號進行轉帳"),
	UNABLE_TRANSFERS_TO_FAMILY_BALANCE(400, "群組帳戶不支援轉帳功能"),
	
	FORBIDDEN(403, "無權限"),
    NO_PERMISSION(403, "沒有 owner 權限"),
	
    NOT_FOUND(404, "查無資料"),
	ACCOUNT_NOT_FOUND(404, "查無此帳號！"),
	BALANCE_NOT_FOUND(404, "查無此帳戶！"),
	PAYMENT_NOT_FOUND(404, "查無此帳款！"),
	MEMBER_NOT_FOUND(404,"家族成員不存在"),
	FAMILY_NOT_FOUND(404,"家族群組不存在"),
	OWNER_NOT_FOUND(404, "擁有者帳號不存在"),
	INVITOR_NOT_FOUND(404, "邀請人帳號不存在"),
	FAMLIY_NOT_FOUND(404, "查無此群組（家庭）！"),
	TRANSFERS_NOT_FOUND(404, "查無此轉帳紀錄！"),
	INVITATION_NOT_FOUND(404, "查無此邀請紀錄！"),

	NULL_ACCOUNT(500, "account 傳參空白！"),
	NULL_SAVINGS_VALUE(500, "Savings取值錯誤。原因：該帳戶不存在 Savings 設定"),
	INVALID_SESSION(500, "無效的 SESSION");
	
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
