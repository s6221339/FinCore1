package com.example.FinCore.constants;

public final class ApiDocConstants 
{
	
	public final static String USER_REQUEST_BODY_RULE = "<ul>"
			+ "<li>帳號、密碼、名稱不可為空</li>"
			+ "<li>【未套用】帳號須符合標準信箱格式（regexp：^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$）</li>"
			+ "<li>【未套用】密碼長度為 8 ~ 16 碼</li>"
			+ "</ul>";
	
	public final static String USER_UPDATE_PW_REQUEST_BODY_RULE = "<ul>"
			+ "<li>帳號、新舊密碼不可為空</li>"
			+ "</ul>";
	
	public final static String FAMILY_REQUEST_BODY_RULE = "<ul>"
			+ "<li>至少需要一位owner</li>"
			+ "</ul>";
	
	public final static String ACCOUNT_NOT_EXIST = "帳號不存在";
	
	public final static String ACCOUNT_EXIST = "帳號已存在";
	
	public final static String FAMILY_NOT_EXIST = "群組不存在";
	
	public final static String CREATE_SUCCESS = "新增成功";
	
	public final static String DELETE_SUCCESS = "刪除成功";
	
	public final static String UPDATE_SUCCESS = "更新成功";
	
	public final static String SEARCH_SUCCESS = "搜尋成功";
	
	public final static String NO_PERMISSION = "無權限";
	
	public final static String NOT_TEST = "⚠️尚未進行任何測試";
	
	public final static String TEST_PASS = "⭕已通過測試";
	
	public final static String TEST_FAILED = "❌測試不通過";
	
	private ApiDocConstants() {}
	
}
