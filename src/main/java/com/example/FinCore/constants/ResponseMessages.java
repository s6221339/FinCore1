package com.example.FinCore.constants;

public enum ResponseMessages {
	
	SUCCESS(200, "成功"),
	BAD_REQUEST(400, "請求參數錯誤"),
    UNAUTHORIZED(401, "未授權"),
    FORBIDDEN(403, "無權限"),
    NOT_FOUND(404, "查無資料"),
    CONFLICT(409, "資料衝突"),
    SERVER_ERROR(500, "伺服器錯誤，請稍後再試"),
	MISSING_REQUIRED_FIELD(400, "缺少必要欄位"),
    DUPLICATE_MEMBER(400, "群組擁有者與邀請人不能相同"),
    OWNER_NOT_FOUND(400, "擁有者帳號不存在"),
    INVITOR_NOT_FOUND(400, "邀請人帳號不存在"),
    CREATE_FAMILY_FAIL(400, "新增家族群組失敗"),
    FAMILY_NOT_FOUND(400,"家族群組不存在"),
	UPDATE_FAMILY_FAIL(400,"更新家族群組失敗"),
	DELETE_FAMILY_FAIL(400, "刪除家庭群組失敗"),
	NO_PERMISSION(400, "沒有 owner 權限"),
	MEMBER_NOT_FOUND(400,"家族成員不存在");

	
	
	private int code;

	private String message;

	private ResponseMessages(int code, String message) {
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
