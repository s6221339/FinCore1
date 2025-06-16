package com.example.FinCore.constants;

public final class ApiDocConstants 
{
	
	/* === 請求資料規則 === */
	
	public final static String USER_REQUEST_BODY_RULE = "<ul>"
			+ "<li>帳號、密碼、名稱不可為空</li>"
			+ "<li>【未套用】帳號須符合標準信箱格式（regexp：^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$）</li>"
			+ "<li>【未套用】密碼長度為 8 ~ 16 碼</li>"
			+ "</ul>";
	
	public final static String USER_UPDATE_PW_REQUEST_BODY_RULE = "<ul>"
			+ "<li>帳號、新舊密碼不可為空</li>"
			+ "</ul>";
	
	public final static String TRANSFERS_CREATE_REQUEST_BODY_RULE = "<ul>"
			+ "<li>帳戶編號不得小於1</li>"
			+ "<li>轉帳金額不得為負數</li>"
			+ "</ul>";
	
	public final static String PAYMENT_TYPE_CREATE_REQUEST_BODY_RULE = 
			"<li>類型、項目、新增者均不得為空值</li>";
	
	public final static String FAMILY_REQUEST_BODY_RULE = "<ul>"
			+ "<li>至少需要一位owner</li>"
			+ "</ul>";
	
	/* === 各種錯誤訊息 === */
	
	public final static String ACCOUNT_NOT_EXIST = "帳號不存在";
	
	public final static String BALANCE_NOT_FOUND = "帳戶不存在";
	
	public final static String ACCOUNT_EXIST = "帳號已存在";
	
	public final static String FAMILY_NOT_EXIST = "群組不存在";
	
	public final static String NO_PERMISSION = "無權限";
	
	public final static String TRANSFERS_DELETE_RESPONSE_404 = "<ol>"
			+ "<li>指定刪除的紀錄不存在</li>"
			+ "<li>執行者不存在</li>"
			+ "</ol>";
	
	public final static String SAME_BALANCE_OPERATION = "無法對同一個帳戶進行操作";
	
	public final static String NOT_SUPER_ADMIN = "執行者不是超級管理員";
	
	public final static String BALANCE_ACTIVATION = "帳戶未停用";
	
	public final static String TRANSFERS_DELETE_BY_BALANCE_ID_RESPONSE_400 = 
			  "<li>" + BALANCE_ACTIVATION + "（任一存在皆會觸發）</li>"
			+ "<li>" + SAME_BALANCE_OPERATION + "</li>";
	
	public final static String ITEM_EXISTED = "已存在相同類型／項目";
	
	/* === 成功訊息 === */
	
	public final static String CREATE_SUCCESS = "新增成功";
	
	public final static String DELETE_SUCCESS = "刪除成功";
	
	public final static String UPDATE_SUCCESS = "更新成功";
	
	public final static String SEARCH_SUCCESS = "搜尋成功";

	/* === 測試狀態 === */

	public final static String NOT_TEST = "⚠️尚未進行任何測試";
	
	public final static String TEST_PASS = "⭕已通過測試";
	
	public final static String TEST_FAILED = "❌測試不通過";
	
	private ApiDocConstants() {}
	
}
