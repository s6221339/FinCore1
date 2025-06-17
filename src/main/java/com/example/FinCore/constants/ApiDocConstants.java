package com.example.FinCore.constants;

public final class ApiDocConstants 
{
	
	/* === API 簡介（@Operation#summary） === */
	
	public final static String TRANSFERS_CREATE_SUMMARY = "建立新紀錄";
	
	public final static String TRANSFERS_DELETE_SUMMARY = "刪除紀錄";
	
	public final static String TRANSFERS_DELETE_BY_BALANCE_ID_SUMMARY = "刪除帳戶的轉帳紀錄";
	
	public final static String TRANSFERS_GET_ALL_BY_BALANCE_ID_SUMMARY = "取得帳戶的轉帳紀錄資料";
	
	/* === API 詳述（@Operation#description），每段末尾都需添加<br>換行 === */
	
	public final static String TRANSFERS_CREATE_DESC = "建立一筆轉帳紀錄。如果設定轉出或匯入的帳戶不存在時建立失敗。<br>";
	
	public final static String TRANSFERS_DELETE_DESC = "刪除一筆轉帳紀錄，注意只有超級管理員能執行。<br>";
	
	public final static String TRANSFERS_DELETE_BY_BALANCE_ID_DESC = "刪除所有關聯兩個帳戶的轉帳紀錄，注意該操作會永久刪除資料。"
			+ "該操作必須在兩個帳戶均不存在才可執行，否則操作失敗。<br>";
	
	public final static String TRANSFERS_GET_ALL_BY_BALANCE_ID_DESC = "取得指定帳戶的所有轉帳紀錄資料。<br>";
	
	/* === 請求資料規則，多條規則使用<ul>標籤 === 
	 * === 命名格式：「API名稱_請求資料名稱_REQUEST_BODY_RULE」 ===*/
	
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
	
	/* === 各種錯誤訊息，多條同代碼訊息使用<li>或<ol>包覆 === 
	 * === 對於同代碼訊息變數命名遵照「API名稱_方法_RESPONSE_代碼」格式 === */
	
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
