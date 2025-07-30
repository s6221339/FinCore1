package com.example.FinCore.constants;

public final class ApiDocConstants 
{
	
	/* === API 簡介（@Operation#summary） === 
	 * === 命名格式：「API名稱_方法_SUMMARY」 === */
	
	public final static String PAYMENT_TYPE_CREATE_SUMMARY = "建立新項目";
	
	public final static String PAYMENT_TYPE_GET_TYPE_SUMMARY = "取得帳號創建的所有類型與項目資料";
	
	public final static String TRANSFERS_CREATE_SUMMARY = "建立新紀錄";
	
	public final static String TRANSFERS_DELETE_SUMMARY = "刪除紀錄";
	
	public final static String TRANSFERS_DELETE_BY_BALANCE_ID_SUMMARY = "刪除帳戶的轉帳紀錄";
	
	public final static String TRANSFERS_GET_ALL_BY_BALANCE_ID_SUMMARY = "取得帳戶的轉帳紀錄資料";
	
	public static final String TRANSFERS_CONFIRM_SUMMARY = "確認轉帳";
	
	public static final String TRANSFERS_GET_NOT_CONFIRM_SUMMARY = "取得尚未確認之轉帳紀錄";

	public final static String SAVINGS_GET_ALL_SUMMARY = "取得帳號的所有儲蓄設定";
	
	public final static String PAYMENT_CREATE_SUMMARY = "新增帳款";
	
	public final static String PAYMENT_DELETE_SUMMARY = "刪除帳款";
	
	public final static String PAYMENT_UPDATE_SUMMARY = "更新帳款";
	
	public final static String PAYMENT_GET_INFO_BY_ACCOUNT_SUMMARY = "取得指定帳號的所有帳款資訊";
	
	public static final String PAYMENT_GET_INFO_BY_ACCOUNT_WITH_DATE_FILTER_SUMMARY = "依帳號與日期篩選查詢付款資訊";
	
	public static final String PAYMENT_RECOVERY_SUMMARY = "復原被刪除的款項";

	public static final String PAYMENT_TRASH_CAN_SUMMARY = "查詢已刪除款項資料";
	
	public static final String PAYMENT_GET_FAMILY_INFO_SUMMARY = "查詢使用者所屬群組的帳戶款項";

	public static final String PAYMENT_GET_FAMILY_INFO_WITH_DATE_FILTER_SUMMARY = "查詢使用者所屬群組的帳戶款項（依據年月篩選）";

	public static final String PAYMENT_STATISTICS_SUMMARY_INCOME_AND_OUTLAY_SUMMARY = "統計帳號收入與支出總覽";

	public static final String PAYMENT_STATISTICS_PERSONAL_BALANCE_WITH_PAYMENT_TYPE_SUMMARY = "統計個人帳戶依類型分類之收入支出";

	public static final String PAYMENT_STATISTICS_LOOKUP_ALL_BALANCE_SUMMARY = "統計帳號及家庭帳戶款項資訊";
	
	public static final String PAYMENT_STATISTICS_INCOME_AND_OUTLAY_WITH_ALL_BALANCE_SUMMARY = "統計個人與群組帳戶的收支資訊";

	public static final String PAYMENT_STATISTICS_INCOME_DETAILS_WITH_ALL_BALANCE_SUMMARY = "統計個人與群組帳戶的收入明細";

	public static final String PAYMENT_STATISTICS_INCOME_DETAILS_SUMMARIZE_SUMMARY = "統計個人帳戶的收入明細分類";

	public static final String PAYMENT_SCHEDULED_CREATE_SUMMARY = "排程建立下一筆循環帳款（內部任務）";
	
	public static final String PAYMENT_SCHEDULED_DELETE_SUMMARY = "排程永久刪除帳款資料（內部任務）";

	public static final String BALANCE_CREATE_SUMMARY = "新增帳戶資料";
	
	public static final String BALANCE_UPDATE_SUMMARY = "更新帳戶名稱與當月儲蓄";

	public static final String BALANCE_DELETE_SUMMARY = "刪除帳戶資料";
	
	public static final String BALANCE_GET_BUDGET_SUMMARY = "查詢帳戶預算資訊";
	
	public static final String BALANCE_GET_BUDGET_BY_ACCOUNT_SUMMARY = "取得帳戶清單預算統計";

	public static final String BALANCE_GET_ALL_BY_ACCOUNT_SUMMARY = "取得帳戶清單資訊";

	public static final String BALANCE_GET_ALL_BY_FAMILY_SUMMARY = "取得帳號所屬群組之帳戶清單資訊_POST_SUMMARY";

	public final static String FAMILY_CREATE_SUMMARY = "建立家族";

	public static final String FAMILY_GET_BY_ID_SUMMARY = "查詢家族群組詳細資料";
	
	public final static String FAMILY_LIST_ALL_FAMILY_SUMMARY = "查詢所有家族群組清單";
	
	public static final String FAMILY_DISMISS_SUMMARY = "解散家族群組";
	
	public static final String FAMILY_KICK_MEMBER_SUMMARY = "踢除家族成員";
	
	public static final String FAMILY_QUIT_MEMBER_SUMMARY = "家族成員退出群組";
	
	public static final String FAMILY_RENAME_SUMMARY = "家族群組改名";
    
    public final static String FAMILY_ACCEPT_INVITE_SUMMARY = "接受家族邀請";
    
    public final static String FAMILY_REJECT_INVITE_SUMMARY = "拒絕家族邀請";
    
    public static final String FAMILY_OWNER_QUIT_SUMMARY = "群組擁有者退位並指派新擁有者";
    
    public static final String FAMILY_TRANSFER_OWNER_SUMMARY = "群組擁有者權限轉讓";

    public static final String FAMILY_GET_BY_ACCOUNT_SUMMARY = "查詢會員所屬家族清單";
    
    public static final String FAMILY_INVITATION_ACCEPT_SUMMARY = "接受家族邀請";
    	
    public static final String FAMILY_INVITATION_REJECT_SUMMARY = "拒絕家族邀請";
    
    public static final String FAMILY_INVITE_MEMBER_SUMMARY = "邀請家族新成員";
    
    public static final String FAMILY_INVITATION_LIST_SUMMARY = "查詢家族邀請中成員名單";
    
    public static final String FAMILY_INVITATION_CANCEL_SUMMARY = "取消家族邀請";
    
    public static final String FAMILY_INVITATION_STATUS_SUMMARY = "查詢帳號所有邀請中家族";
    
    public static final String USER_REGISTER_SUMMARY = "會員註冊";
    
    public static final String USER_UPDATE_SUMMARY = "會員資料更新";
    
    public static final String USER_CANCEL_SUMMARY = "會員註銷";
    
    public static final String USER_UPDATE_PASSWORD_SUMMARY = "會員修改密碼";
    
    public static final String USER_GET_SUMMARY = "查詢會員基本資料";
    
    public static final String USER_LOGIN_SUMMARY = "會員登入";
    
    public final static String USER_VERIFY_CODE_SEND_VERIFY_CODE_SUMMARY = "忘記密碼發送驗證信";
    
    public final static String USER_VERIFY_CODE_CHECK_VERIFY_CODE_SUMMARY = "忘記密碼認證驗證碼";
    
    public final static String USER_VERIFY_CODE_UPDATE_PWD_BY_EMAIL_SUMMARY = "忘記密碼－重設密碼";
    
    public static final String USER_GET_NAME_BY_ACCOUNT_SUMMARY = "查詢會員名稱";
    
    public static final String USER_LOGOUT_SUMMARY = "會員登出";
    
    public static final String USER_SEND_REGISTER_VERIFY_CODE_SUMMARY = "註冊會員發送驗證信";
    
    public static final String USER_CHECK_REGISTER_VERIFY_CODE_SUMMARY = "註冊驗證碼認證";
    
    public static final String USER_UPDATE_SUBSCRIPTION_SUMMARY = "更新會員訂閱狀態";
    
    public static final String USER_GET_SUBSCRIPTION_SUMMARY = "查詢會員訂閱狀態";
    
    public static final String USER_GET_ECPAY_FORM_SUMMARY = "產生綠界金流訂單參數（固定商品/金額）";
    
    public static final String USER_ECPAY_NOTIFY_SUMMARY = "綠界金流付款通知（notify）";
    
    /* === API 詳述（@Operation#description），每段末尾都需添加<br>換行 === 
	 * === 命名格式：「API名稱_方法_DESC」 === */
    
	public final static String PAYMENT_TYPE_CREATE_DESC = "建立一筆新的分類與細項，不允許使用者新增完全重複的類型與項目。<br>";
	
	public final static String PAYMENT_TYPE_GET_TYPE_DESC = "取得帳號創建的所有類型與項目資料，其中包含了預設的資料。該操作必定成功<del>（大不了就沒自己ㄉ東東）</del><br>";
	
	public final static String TRANSFERS_CREATE_DESC = "建立一筆帳款轉移紀錄，需檢查目標帳號是否為同家庭群組，並驗證轉出帳戶是否存在<br>";
	
	public final static String TRANSFERS_DELETE_DESC = "刪除一筆轉帳紀錄，注意只有超級管理員能執行。<br>";
	
	public final static String TRANSFERS_DELETE_BY_BALANCE_ID_DESC = "刪除所有關聯兩個帳戶的轉帳紀錄，注意該操作會永久刪除資料。"
			+ "該操作必須在兩個帳戶均不存在才可執行，否則操作失敗。<br>";
	
	public final static String TRANSFERS_GET_ALL_BY_BALANCE_ID_DESC = "取得指定帳戶的所有轉帳紀錄資料。<br>";
	
	public static final String TRANSFERS_CONFIRM_DESC = "確認一筆帳款轉移紀錄，並生成雙邊付款紀錄。<br>";

	public static final String TRANSFERS_GET_NOT_CONFIRM_DESC = "取得所有尚未被登入使用者確認的轉帳紀錄資料<br>";
	
	public final static String SAVINGS_GET_ALL_DESC = "取得帳號的所有儲蓄設定<br>";
	
	public final static String PAYMENT_CREATE_DESC = "創建一筆新帳款資料，該帳款將綁定於一個指定帳戶之下。<p>"
			+ "為了預防資料沒有提供「記帳日期」，該 API 會偵測缺失資料並為其填上<b>當日日期</b>；而帳款創建日期必定為"
			+ "當日日期。<br>"
			+ "對於「一般款項」與「循環款項」具有日期檢測，「一般款項」無法登錄於未來時間，「循環款項」則無法登錄於"
			+ "過去時間（包含當下）<br>"
			+ "呼叫此 API 的同時，系統將同時檢查儲蓄設定資料庫是否有對應的記帳月資料，如果沒有將會自動創建該日期的預設儲蓄設定。<br>";
	
	public final static String PAYMENT_DELETE_DESC = "刪除一筆帳款資料。<p>"
			+ "該 API 並不真正執行資料刪除，而只是將該資料標記上「刪除日期」。"
			+ "對於已經被刪除的帳款是無法被重複執行刪除的，因此將返回錯誤。<br>";
	
	public final static String PAYMENT_UPDATE_DESC = "更新一筆帳款資料。<p>"
			+ "該 API 無法更新已被標記刪除的資料，即便它還在資料庫中。<br>"
			+ "另外，對於「一般款項」與「循環款項」具有日期檢測，「一般款項」無法登錄於未來時間，「循環款項」則無法登錄於過去時間（包含當下），"
			+ "所以注意對於所有「已記錄的循環款項」是無法變更過去記帳時間的。<br>"
			+ "對於所有「過去的款項」均無法變更其循環週期，僅有標記於未來的循環帳款可進行修改。<br>"
			+ "呼叫此 API 的同時，系統將同時檢查儲蓄設定資料庫是否有對應的記帳月資料，如果沒有將會自動創建該日期的預設儲蓄設定。<br>";
	
	public final static String PAYMENT_GET_INFO_BY_ACCOUNT_DESC = "取得指定帳號的所有帳款資訊。<p>"
			+ "該 API 無法取得已標記刪除的帳款資料，如要取得標記刪除的資料請用「trashCan API」。<br>";
	
	public static final String PAYMENT_GET_INFO_BY_ACCOUNT_WITH_DATE_FILTER_DESC = 
			"可依帳號及指定的年月作為篩選條件，查詢付款紀錄。結果會依照帳戶編號對款項進行分組。<br>";
	
	public static final String PAYMENT_RECOVERY_DESC =
			"將指定的款項從刪除狀態復原。僅可對尚未被永久刪除的款項執行操作。<br>";

	public static final String PAYMENT_TRASH_CAN_DESC =
			"查詢指定帳號下，所有已被標記為刪除的款項。此操作不會返回未刪除的資料。<br>";
	
	public static final String PAYMENT_GET_FAMILY_INFO_DESC =
			"本 API 用於查詢目前帳號所參與的群組中，所有帳戶的記帳資料，僅包含「未被刪除」的款項資料。<br>"
			+ "系統會依照提供帳號比對群組成員關係，列出所有相關帳戶的資訊與款項紀錄。<br>";

	public static final String PAYMENT_GET_FAMILY_INFO_WITH_DATE_FILTER_DESC =
			"本 API 用於查詢目前帳號所參與的群組中，所有帳戶於指定年、月的款項資料。<br>"
			+ "系統會自動比對帳號參與的群組關聯，合併查詢其帳戶資料，僅包含「未被刪除」的款項。<br>"
			+ "若指定的年月無資料則回傳空陣列。<br>";
	
	public static final String PAYMENT_STATISTICS_SUMMARY_INCOME_AND_OUTLAY_DESC = 
			"統計指定帳號於某年（可選擇指定月份）之所有帳戶的收入與支出金額總覽資訊，會忽略"
			+ "未來與已刪除的資料。<br>";

	public static final String PAYMENT_STATISTICS_LOOKUP_ALL_BALANCE_DESC =
			"根據指定帳號與時間範圍，統計該帳號下所有個人及家庭帳戶的款項資料，依月份與類型分類統計。<br>";

	public static final String PAYMENT_STATISTICS_PERSONAL_BALANCE_WITH_PAYMENT_TYPE_DESC = 
			"統計指定帳號之所有帳戶中，各類型的支出與收入金額總和。可依據年月篩選資料，會忽略未來與已刪除的款項。<br>";
	
	public static final String PAYMENT_STATISTICS_INCOME_AND_OUTLAY_WITH_ALL_BALANCE_DESC = 
		    "根據指定帳號取得其個人帳戶與所屬群組帳戶，在指定年月的收入與支出統計資訊，資料會依照帳戶分開列出，並標示其來源。<br>";
	
	public static final String PAYMENT_STATISTICS_INCOME_DETAILS_WITH_ALL_BALANCE_DESC = 
		    "根據指定帳號與年月，統計該使用者在個人與所屬群組帳戶中的所有收入資料，依月份、帳戶、類別與項目進行分類與加總，並補全無資料月份與帳戶欄位。<br>";
	
	public static final String PAYMENT_STATISTICS_INCOME_DETAILS_SUMMARIZE_DESC = 
		    "根據指定帳號與年月，統計該使用者於個人帳戶中的所有收入資料，依月份、類別與項目進行分類加總，並補全無資料的月份統計欄位。<br>";
	
	public static final String PAYMENT_SCHEDULED_CREATE_DESC =
    		"每天凌晨自動執行的排程任務，根據循環帳款規則建立下一筆尚未記錄的款項。<br>"
    		+ "篩選條件包括：未刪除、有效週期、當前時間符合周期起點。符合者將自動產生下一筆紀錄。<br>";

	public static final String PAYMENT_SCHEDULED_DELETE_DESC =
			"每天凌晨執行的清除任務，將資料庫中「已被標記為刪除」且「壽命已歸零」的帳款資料永久移除。<br>"
			+ "刪除條件如下：<ul>"
			+ "<li>該帳款已標記刪除</li>"
			+ "<li>剩餘生命週期少於0</li>"
			+ "</ul>"
			+ "完全符合者將由系統自動清除，確保資料庫穩定性與正確性。<br>";
	
	public static final String BALANCE_CREATE_DESC = 
			"可新增個人或家庭帳戶。帳戶需指定所屬對象，且不可同時指定家庭與使用者帳號。<br>";

	public static final String BALANCE_UPDATE_DESC =
			"依據帳戶編號，更新帳戶名稱與儲蓄資料。若當月尚無儲蓄資料，系統會自動建立一筆。名稱與儲蓄欄位可擇一或同時更新。<br>";

	public static final String BALANCE_DELETE_DESC =
			"依帳戶 ID 刪除該筆帳戶資料，包含關聯的儲蓄紀錄與款項。<br>";

    public static final String BALANCE_GET_BUDGET_DESC = "根據帳戶編號與時間查詢該月份預算與實際支出資料<br>";

    public static final String BALANCE_GET_BUDGET_BY_ACCOUNT_DESC = "根據帳號與指定年月，取得所有帳戶之該月份預算統計資料，若查詢時間為未來則不允許查詢。<br>";

    public static final String BALANCE_GET_ALL_BY_ACCOUNT_DESC = "根據帳號取得該使用者所有帳戶資料。<br>";
    
    public static final String BALANCE_GET_ALL_BY_FAMILY_DESC = "根據帳號取得該使用者所屬群組的所有帳戶資料。<br>";
    
	public final static String FAMILY_CREATE_DESC = "由指定 owner 建立新的家族，並可一次邀請多位成員<br>";
	
	public static final String FAMILY_GET_BY_ID_DESC = "根據 familyId 查詢該家族群組的詳細資訊，包括擁有者與所有成員姓名。<br>";
	
	public final static String FAMILY_LIST_ALL_FAMILY_DESC = "查詢系統內所有家族群組，包含群組名稱、擁有者與成員資訊。<br>";
	
	public static final String FAMILY_DISMISS_DESC = "群組擁有者可解散指定的家族群組，並自動刪除所有邀請資料。<br>";
	
	public static final String FAMILY_KICK_MEMBER_DESC = "群組擁有者可將指定成員移出家族，並同步移除邀請資料。<br>";
	
	public static final String FAMILY_QUIT_MEMBER_DESC = "成員或擁有者可依規則退出家族群組。若僅剩 owner 一人則群組自動解散。<br>";
	
	public static final String FAMILY_RENAME_DESC = "群組擁有者可修改家族群組名稱。<br>";
    
    public final static String FAMILY_ACCEPT_INVITE_DESC = "被邀請人確認接受邀請，將移出邀請名單並正式加入家族<br>";
    
    public final static String FAMILY_REJECT_INVITE_DESC = "被邀請人拒絕邀請，將從家族邀請名單中移除<br>";
    
    public static final String FAMILY_OWNER_QUIT_DESC = "群組擁有者可退位並指定新 owner，若未指定則預設轉讓給成員第一人。<br>";
    
    public static final String FAMILY_TRANSFER_OWNER_DESC = "群組現任 owner 可將權限轉讓給指定家庭成員，新 owner 必須為現有成員且不可為自己。<br>";
    
    public static final String FAMILY_GET_BY_ACCOUNT_DESC = "根據會員帳號查詢其所屬的所有家族群組，包含群組名稱、擁有者與成員資訊。<br>";
    
    public static final String FAMILY_INVITE_MEMBER_DESC = "群組擁有者邀請新成員加入家族群組，支援多帳號同時邀請。<br>";
    
    public static final String FAMILY_INVITATION_ACCEPT_DESC = "用戶接受家族邀請，將邀請紀錄標記為已接受並加入家族群組。<br>";
    
    public static final String FAMILY_INVITATION_REJECT_DESC = "用戶拒絕家族邀請，會直接刪除邀請紀錄。<br>";
    
    public static final String FAMILY_INVITATION_LIST_DESC = "依據 familyId 查詢目前發出、尚未接受的邀請成員清單（status=0）。<br>";
    
    public static final String FAMILY_INVITATION_CANCEL_DESC = "群組擁有者可取消尚未被接受的家族邀請。<br>";
    
    public static final String FAMILY_INVITATION_STATUS_DESC = "根據會員帳號查詢目前尚未接受的所有家族邀請資訊。<br>";
    
    public static final String USER_REGISTER_DESC = "建立新會員帳號，帳號需唯一且密碼將自動加密。<br>";
    
    public static final String USER_UPDATE_DESC = "修改會員基本資料，需提供正確帳號，成功後回傳更新狀態。<br>";
    
    public static final String USER_CANCEL_DESC = "根據指定帳號註銷會員，並同步移除關聯的資產、支付、儲蓄與帳戶類型等資料。<br>";
    
    public static final String USER_UPDATE_PASSWORD_DESC = "會員可比對舊密碼並修改新密碼。<br>";
    
    public static final String USER_GET_DESC = "根據會員帳號查詢會員基本資訊，包括帳號、名稱、電話、生日、頭像、角色。<br>";
    
    public static final String USER_LOGIN_DESC = "會員輸入帳號與密碼進行登入驗證。<br>";
    
    public final static String USER_VERIFY_CODE_SEND_VERIFY_CODE_DESC = "發送一組驗證碼到指定會員的 email 信箱，10 分鐘內有效<br>";

    public final static String USER_VERIFY_CODE_CHECK_VERIFY_CODE_DESC = "比對指定會員的驗證碼是否正確且未過期，成功則設帳號為已驗證<br>";
    
    public final static String USER_VERIFY_CODE_UPDATE_PWD_BY_EMAIL_DESC = "會員經過信箱驗證成功後，使用此 API 進行新密碼設定<br>";
    
    public static final String USER_GET_NAME_BY_ACCOUNT_DESC = "根據會員帳號查詢對應名稱<br>";
    
    public static final String USER_LOGOUT_DESC = "會員進行登出動作。RESTful 架構多為前端刪除 token，若需強制登出請配合 session 或 token blacklist。<br>";

    public static final String PARAM_ACCOUNT_DESC = "使用者帳號，用於比對群組成員身份";
    
    public static final String USER_SEND_REGISTER_VERIFY_CODE_DESC = "會員註冊時發送電子郵件驗證碼，驗證碼有效時間 10 分鐘。<br>";
    
    public static final String USER_CHECK_REGISTER_VERIFY_CODE_DESC = "註冊會員時，根據輸入驗證碼與帳號進行驗證。<br>";
    
    public static final String USER_UPDATE_SUBSCRIPTION_DESC = "根據會員帳號設定訂閱狀態，訂閱後到期日自動延展一個月。<br>";
    
    public static final String USER_GET_SUBSCRIPTION_DESC = "根據會員帳號查詢其目前的訂閱狀態與訂閱到期日。<br>";
    
    public static final String USER_GET_ECPAY_FORM_DESC = "產生綠界金流 ECPay 訂單參數，金額固定為 60 元，商品名稱固定為「VIP Subscription」。<br>主要提供前端直接提交金流表單用。";
    
    public static final String USER_ECPAY_NOTIFY_DESC = "綠界付款完成後由金流平台回呼，訂單編號請帶入會員帳號與時間戳。付款成功（RtnCode=1）即自動啟用訂閱。<br>需回傳 \"1|OK\" 給藍新以完成通知流程。";
    
    /* === 請求資料規則，多條規則使用<ul>標籤 === 
	 * === 命名格式：「API名稱_請求資料名稱_REQUEST_BODY_RULE」 === */

    public static final String USER_REGISTER_REQUEST_BODY_RULE =
    	    "<ul>" +
    	    "<li>account：必填，會員帳號（不可重複）</li>" +
    	    "<li>name：必填，會員名稱</li>" +
    	    "<li>password：必填，會員密碼</li>" +
    	    "<li>phone：可選，手機號碼</li>" +
    	    "</ul>";
	
    public static final String USER_UPDATE_REQUEST_BODY_RULE =
    	    "<ul>" +
    	    "<li>account：必填，會員帳號</li>" +
    	    "<li>name：必填，會員名稱</li>" +
    	    "<li>phone：可選，手機號碼</li>" +
    	    "<li>birthday：可選，生日（yyyy-MM-dd）</li>" +
    	    "<li>avatar：可選，頭像（byte[]）</li>" +
    	    "</ul>";
	
    public static final String USER_UPDATE_PASSWORD_REQUEST_BODY_RULE =
    	    "<ul>" +
    	    "<li>account：必填，會員帳號</li>" +
    	    "<li>oldPassword：必填，舊密碼</li>" +
    	    "<li>newPassword：必填，新密碼</li>" +
    	    "</ul>";
	
    public static final String USER_LOGIN_REQUEST_BODY_RULE =
    	    "<ul>" +
    	    "<li>account：必填，會員帳號</li>" +
    	    "<li>password：必填，會員密碼</li>" +
    	    "</ul>";
	
	public final static String USER_VERIFY_CODE_UPDATE_PWD_BY_EMAIL_REQUEST_BODY_RULE = "<ul>"
			+ "<li>帳號、新密碼不得為空</li>"
			+ "<li>【未套用】帳號須符合標準信箱格式（regexp：^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$）</li>"
			+ "<li>【未套用】密碼長度為 8 ~ 16 碼</li>"
			+ "</ul>";
	
	public final static String TRANSFERS_CREATE_REQUEST_BODY_RULE = """
			<ul>
			  <li><b>fromBalance</b>：轉出帳戶 ID，必須為當前使用者所擁有之帳戶</li>
			  <li><b>toAccount</b>：接收帳號，必須為同一家庭群組成員</li>
			  <li><b>amount</b>：轉移金額，需為 0 或正整數</li>
			  <li><b>description</b>：備註（可選）</li>
			</ul>
			""";
	
	public final static String PAYMENT_TYPE_CREATE_REQUEST_BODY_RULE = 
			"<li>類型、項目、新增者均不得為空值</li>";
	
	public final static String PAYMENT_CREATE_REQUEST_BODY_RULE = "<ul>"
			+ "<li>帳戶編號不可小於1</li>"
			+ "<li>款項類型與項目均不可留空</li>"
			+ "<li>帳款金額不得為負值</li>"
			+ "<li>循環週期設定均不得為負值</li>"
			+ "</ul>";
	
	public final static String PAYMENT_UPDATE_REQUEST_BODY_RULE = "<ul>"
			+ "<li>帳款編號不可小於1</li>"
			+ "<li>款項類型與項目均不可留空</li>"
			+ "<li>帳款金額不得為負值</li>"
			+ "<li>循環週期設定均不得為負值</li>"
			+ "<li>記帳日期不可為空</li>"
			+ "</ul>";
	
	public static final String PAYMENT_ACCOUNT_WITH_DATE_FILTER_REQUEST_BODY_RULE =
			"<ul>"
			+ "<li>account：查詢使用者帳號，為必填</li>"
			+ "<li>year：指定查詢的年份，若為 -1 則不啟用年份篩選</li>"
			+ "<li>month：指定查詢的月份，0 表示不啟用月份篩選，只有在啟用年份篩選時才會生效</li>"
			+ "</ul>";
	
	public static final String PAYMENT_GET_FAMILY_INFO_WITH_DATE_FILTER_REQUEST_RULE =
			"<ul>"
			+ "<li><b>account</b>：使用者帳號，<code>不可為空</code>，系統會依此比對所屬群組</li>"
			+ "<li><b>year</b>：指定年份，<code>介於 0～9999</code></li>"
			+ "<li><b>month</b>：指定月份，<code>1～12</code> 之間</li>"
			+ "</ul>";
	
	public static final String PAYMENT_RECOVERY_REQUEST_BODY_RULE =
			"<ul>"
			+ "<li>paymentIdList：欲復原的款項 ID 清單，不可為空</li>"
			+ "</ul>";

	public static final String PAYMENT_STATISTICS_REQUEST_BODY_RULE =
			"<ul>"
			+ "<li>account：使用者帳號，必填</li>"
			+ "<li>year：統計年份（四位數），必填</li>"
			+ "<li>month：統計月份（1～12），若為 0 則表示統計全年</li>"
			+ "</ul>";

	public static final String BALANCE_CREATE_REQUEST_BODY_RULE =
			"<ul>"
			+ "<li>familyId：群組（家庭）編號，為 0 表示不使用</li>"
			+ "<li>account：使用者帳號，與 familyId 擇一必填，不可同時存在</li>"
			+ "<li>name：帳戶名稱，必填</li>"
			+ "</ul>";
	
	public static final String BALANCE_UPDATE_REQUEST_BODY_RULE =
			"<ul>"
			+ "<li>balanceId：欲更新的帳戶 ID，必填</li>"
			+ "<li>name：帳戶新名稱，若為空則不變更</li>"
			+ "<li>savings：欲設定的當月儲蓄金額，若為負數則不變更</li>"
			+ "</ul>";

	public static final String FAMILY_CREATE_REQUEST_BODY_RULE =
		    "<ul>" +
		    "<li>owner：必填，群組擁有者帳號</li>" +
		    "<li>name：必填，家族名稱</li>" +
		    "<li>invitor：必填，受邀成員帳號清單（不可重複、不可包含擁有者）</li>" +
		    "</ul>";
	
    public static final String BALANCE_GET_BUDGET_REQUEST_BODY_RULE =
    		"""
    		<ul>
    			<li>balanceId：欲查詢的帳戶編號，必須為正整數</li>
    			<li>year：年份，介於 0～9999 間</li>
    			<li>month：月份，介於 1～12 間</li>
    		</ul>
    		""";
    
    public static final String BALANCE_GET_BUDGET_BY_ACCOUNT_REQUEST_BODY_RULE = "<ul>"
    	    + "<li><b>account</b>：欲查詢的帳號，不能為空</li>"
    	    + "<li><b>year</b>：年份，範圍介於 0 至 9999</li>"
    	    + "<li><b>month</b>：月份，範圍介於 1 至 12</li>"
    	    + "</ul>";
    
	public static final String FAMILY_INVITE_MEMBER_REQUEST_BODY_RULE =
		    "<ul>" +
		    "<li>familyId：必填，家族群組ID</li>" +
		    "<li>owner：必填，群組擁有者帳號（權限驗證）</li>" +
		    "<li>invitor：必填，受邀人帳號清單（不可重複、不可包含擁有者）</li>" +
		    "</ul>";
	
	public static final String FAMILY_DISMISS_REQUEST_BODY_RULE =
		    "<ul>" +
		    "<li>familyId：必填，家族群組ID</li>" +
		    "<li>owner：必填，群組擁有者帳號（權限驗證）</li>" +
		    "</ul>";
	
	public static final String FAMILY_KICK_MEMBER_REQUEST_BODY_RULE =
		    "<ul>" +
		    "<li>familyId：必填，家族群組ID</li>" +
		    "<li>owner：必填，群組擁有者帳號（權限驗證）</li>" +
		    "<li>memberAccounts：必填，要被踢除的成員帳號清單（不可包含擁有者本人）</li>" +
		    "</ul>";
	
	public static final String FAMILY_OWNER_QUIT_REQUEST_BODY_RULE =
		    "<ul>" +
		    "<li>familyId：必填，家族群組ID</li>" +
		    "<li>oldOwner：必填，現任群組擁有者帳號</li>" +
		    "<li>newOwner：可選，指定新 owner，若不指定預設轉讓給成員第一人</li>" +
		    "</ul>";
	
	public static final String FAMILY_TRANSFER_OWNER_REQUEST_BODY_RULE =
		    "<ul>" +
		    "<li>familyId：必填，家族群組ID</li>" +
		    "<li>oldOwner：必填，現任 owner 帳號</li>" +
		    "<li>newOwner：必填，新 owner 帳號（不可為自己，且必須為現有家庭成員）</li>" +
		    "</ul>";
	
	public static final String FAMILY_QUIT_MEMBER_REQUEST_BODY_RULE =
		    "<ul>" +
		    "<li>familyId：必填，家族群組ID</li>" +
		    "<li>memberAccount：必填，欲退出的帳號</li>" +
		    "</ul>";
	
	public static final String FAMILY_RENAME_REQUEST_BODY_RULE =
		    "<ul>" +
		    "<li>familyId：必填，家族群組ID</li>" +
		    "<li>owner：必填，群組擁有者帳號（權限驗證）</li>" +
		    "<li>newName：必填，新名稱</li>" +
		    "</ul>";
	
	public static final String FAMILY_INVITATION_ACCEPT_REQUEST_BODY_RULE =
		    "<ul>" +
		    "<li>account：必填，使用者帳號</li>" +
		    "<li>familyId：必填，家族群組ID</li>" +
		    "</ul>";
	
	public static final String FAMILY_INVITATION_REJECT_REQUEST_BODY_RULE =
		    "<ul>" +
		    "<li>account：必填，使用者帳號</li>" +
		    "<li>familyId：必填，家族群組ID</li>" +
		    "</ul>";
	
	

	/* === 各種錯誤訊息，多條同代碼訊息使用<li>或<ol>包覆 === 
	 * === 同代碼訊息命名格式：「API名稱_方法_RESPONSE_代碼」格式 ===
	 * === 其他訊息格式：「任意，建議與 ResponseMessages 的變數名稱一致」 === */
	
	public final static String ACCOUNT_NOT_FOUND = "帳號不存在";
	
	public final static String BALANCE_NOT_FOUND = "帳戶不存在";
	
	public final static String ACCOUNT_EXIST = "帳號已存在";
	
	public final static String FAMILY_NOT_FOUND = "群組不存在";
	
	public final static String NO_PERMISSION = "無權限";
	
	public static final String MEMBER_NOT_FOUND = "家族成員不存在";
	
	public final static String TRANSFERS_CREATE_RESPONSE_400 = 
			"""
			<ol>
				<li>未登入時進行此操作</li>
				<li>無法對不同家庭群組的帳號進行轉帳</li>
			</ol>
			""";
	
	public final static String TRANSFERS_CREATE_RESPONSE_404 = 
			"""
			<ol>
				<li>查無帳號</li>
				<li>查無帳戶</li>
			</ol>
			""";
	
	public final static String TRANSFERS_DELETE_RESPONSE_404 = "<ol>"
			+ "<li>指定刪除的紀錄不存在</li>"
			+ "<li>執行者不存在</li>"
			+ "</ol>";
	
	public static final String TRANSFERS_CONFIRM_RESPONSE_400 = """
			<ul>
			  <li>請先登入！</li>
			  <li>轉帳資料已被設定</li>
			</ul>
			""";

	public static final String TRANSFERS_CONFIRM_RESPONSE_404 = """
			查無此轉帳紀錄！
			""";
	
	public final static String SAME_BALANCE_OPERATION = "無法對同一個帳戶進行操作";
	
	public final static String NOT_SUPER_ADMIN = "執行者不是超級管理員";
	
	public final static String BALANCE_ACTIVATION = "帳戶未停用";
	
	public final static String TRANSFERS_DELETE_BY_BALANCE_ID_RESPONSE_400 = 
			  "<li>" + BALANCE_ACTIVATION + "（任一存在皆會觸發）</li>"
			+ "<li>" + SAME_BALANCE_OPERATION + "</li>";
	
	public final static String ITEM_EXISTED = "已存在相同類型／項目";
	
	public final static String UPDATE_USER_FAIL = "更新會員失敗";

	public final static String PAYMENT_NOT_FOUND = "帳款不存在";
	
	public final static String PAYMENT_HAS_BEEN_DELETED = "該帳款已刪除";
	
	public final static String NO_PAYMENT_MODIFING_PERMISSION = "無法修改他人的帳款資料";
	
	public final static String DELETED_PAYMENT_CANNOT_UPDATE = "無法更新已刪除的款項資料";
	
	public final static String PAYMENT_PERIOD_UNABLE_MODIFYING = "無法變更該款項的週期";
	
	public final static String INVALID_PAYMENT_DATE = "無效的記帳日期";
	
	public final static String PAYMENT_UPDATE_RESPONSE_400 = ""
			+ "<li>" + INVALID_PAYMENT_DATE + "</li>"
			+ "<li>" + DELETED_PAYMENT_CANNOT_UPDATE + "</li>"
			+ "<li>" + PAYMENT_PERIOD_UNABLE_MODIFYING + "</li>";
	
	public static final String PAYMENT_RECOVERY_RESPONSE_404 =
			"<li>部分或全部的付款 ID 不存在，無法執行復原</li>";
	
	public static final String BALANCE_CREATE_RESPONSE_400 =
			"<ol>"
			+ "<li>帳戶不能沒有所屬對象。</li>"
			+ "<li>帳戶所屬對象只能是唯一。</li>"
			+ "</ol>";

	public static final String BALANCE_CREATE_RESPONSE_404 =
			"<ol>"
			+ "<li>查無此群組（家庭）！</li>"
			+ "<li>查無此帳號！</li>"
			+ "</ol>";
	
	public static final String FAMILY_INVITATION_ACCEPT_RESPONSE_400 =
		    "<ul>" +
		    "<li>缺少必要欄位</li>" +
		    "<li>該邀請已被接受，無法重複操作！</li>" +
		    "<li>更新家族群組失敗</li>" +
		    "</ul>";
	
	public static final String FAMILY_INVITATION_ACCEPT_RESPONSE_404 =
		    "<ul>" +
		    "<li>查無此邀請紀錄！</li>" +
		    "<li>家族群組不存在</li>" +
		    "</ul>";
	
	public static final String FAMILY_CREATE_RESPONSE_400 =
		    "<ul>" +
		    "<li>缺少必要欄位</li>" +
		    "<li>家庭成員重複</li>" +
		    "<li>群組擁有者與受邀人不能相同</li>" +
		    "</ul>";
	
	public static final String FAMILY_CREATE_RESPONSE_404 =
		    "<ul>" +
		    "<li>擁有者帳號不存在</li>" +
		    "<li>家族成員不存在</li>" +
		    "</ul>";
	
	public static final String FAMILY_INVITE_MEMBER_RESPONSE_400 =
		    "<ul>" +
		    "<li>群組擁有者與受邀人不能相同</li>" +
		    "<li>家庭成員重複</li>" +
		    "<li>更新家族群組失敗</li>" +
		    "</ul>";
	
	public static final String FAMILY_INVITE_MEMBER_RESPONSE_404 =
		    "<ul>" +
		    "<li>家族群組不存在</li>" +
		    "<li>邀請人帳號不存在</li>" +
		    "</ul>";
	
	public static final String FAMILY_KICK_MEMBER_RESPONSE_400 =
		    "<ul>" +
		    "<li>更新家族群組失敗</li>" +
		    "<li>擁有者本人不可被踢除</li>" +
		    "</ul>";
	
	public static final String FAMILY_OWNER_QUIT_RESPONSE_400 =
		    "<ul>" +
		    "<li>帳號不存在於此家庭群組</li>" +
		    "<li>更新家族群組失敗</li>" +
		    "</ul>";
	
	public static final String FAMILY_OWNER_QUIT_RESPONSE_404 =
		    "<ul>" +
		    "<li>查無此帳號！</li>" +
		    "<li>家族群組不存在</li>" +
		    "</ul>";
	
	public static final String FAMILY_TRANSFER_OWNER_RESPONSE_400 =
		    "<ul>" +
		    "<li>缺少必要欄位</li>" +
		    "<li>新舊 owner 相同，無法進行轉讓</li>" +
		    "<li>帳號不存在於此家庭群組</li>" +
		    "<li>更新家族群組失敗</li>" +
		    "</ul>";
	
	public static final String FAMILY_TRANSFER_OWNER_RESPONSE_404 =
		    "<ul>" +
		    "<li>查無此帳號！</li>" +
		    "<li>家族群組不存在</li>" +
		    "</ul>";
	
	public static final String FAMILY_QUIT_MEMBER_RESPONSE_400 =
		    "<ul>" +
		    "<li>更新家族群組失敗</li>" +
		    "</ul>";
	
	public static final String FAMILY_QUIT_MEMBER_RESPONSE_404 =
		    "<ul>" +
		    "<li>家族群組不存在</li>" +
		    "<li>家族成員不存在</li>" +
		    "</ul>";
	
	public static final String USER_UPDATE_PASSWORD_RESPONSE_400 =
		    "<ul>" +
		    "<li>密碼錯誤</li>" +
		    "<li>更新會員失敗</li>" +
		    "</ul>";
	
	public static final String FAMILY_INVITATION_CANCEL_RESPONSE_400 =
	    "<ul>" +
	    "<li>該邀請已被接受，無法重複操作！</li>" +
	    "</ul>";
	
	public static final String FAMILY_INVITATION_CANCEL_RESPONSE_404 =
		    "<ul>" +
		    "<li>家族群組不存在</li>" +
		    "<li>查無此邀請紀錄！</li>" +
		    "</ul>";
	
	public static final String USER_CHECK_REGISTER_VERIFY_CODE_RESPONSE_200 =
		    "<ul>" +
		    "<li>成功！</li>" +
		    "<li>驗證碼有效！</li>" +
		    "</ul>";
	
	public static final String USER_CHECK_REGISTER_VERIFY_CODE_RESPONSE_400 =
		    "<ul>" +
		    "<li>操作失敗</li>" +
		    "<li>驗證失敗：驗證碼錯誤或已過期</li>" +
		    "</ul>";
	
	public static final String USER_UPDATE_SUBSCRIPTION_RESPONSE_400 =
		    "<ul>" +
		    "<li>缺少必要欄位</li>" +
		    "<li>更新會員失敗</li>" +
		    "</ul>";
	
	public static final String PASSWORD_NOT_MATCH = "密碼錯誤";

	public final static String PARAM_FORMAT_ERROR = "參數格式錯誤";
	
	public final static String USER_VERIFY_CODE_FAIL = "驗證失敗或驗證碼錯誤";
	
	public final static String USER_VERIFY_CODE_NOT_FOUND = "查無此驗證資料";
	
	public final static String USER_UPDATE_PWD_NOT_VERIFIED = "尚未驗證，禁止重設密碼";
	
	public final static String FAMILY_CREATE_FAIL = "家庭群族新增失敗";
	
	public static final String MISSING_REQUIRED_FIELD = "缺少必要欄位";
	
	public static final String INVITATION_NOT_FOUND = "查無此邀請紀錄！";
	
	public static final String INTERNAL_ONLY = "<b>此 API 為系統排程用途，不可由前端或第三方呼叫！</b><br>";
	
	public static final String INVALID_SESSION = "無效的 SESSION";
	
	public static final String BALANCE_GET_BUDGET_RESPONSE_400 = "無法搜尋未來的時間";
	
	public static final String ECPAY_NOTIFY_RESPONSE_FAIL = "查無帳號，回傳 0|FAIL";
    
	public static final String FORBIDDEN = "無權限";
	
	public static final String PLEASE_LOGIN_FIRST = "需要先進行登入";
	
	/* === 成功訊息 === */
	
	public final static String CREATE_SUCCESS = "新增成功";
	
	public final static String DELETE_SUCCESS = "刪除成功";
	
	public final static String UPDATE_SUCCESS = "更新成功";
	
	public final static String SEARCH_SUCCESS = "搜尋成功";
	
	public final static String SEND_SUCCESS = "驗證信發送成功";
	
	public final static String VERIFY_SUCCESS = "驗證成功";
	
	public static final String LOGOUT_SUCCESS = "登出成功";
	
	public static final String LOGIN_SUCCESS = "登入成功";
	
	public static final String ECPAY_FORM_FIXED_SUCCESS = "產生表單參數成功";
	
	public static final String ECPAY_NOTIFY_SUCCESS = "回傳 1|OK 代表處理成功";

	/* === 測試狀態 === */

	public final static String NOT_TEST = "⚠️尚未進行任何測試";
	
	public final static String TEST_PASS = "⭕已通過測試";
	
	public final static String TEST_FAILED = "❌測試不通過";
	
	private ApiDocConstants() {}
	
}
