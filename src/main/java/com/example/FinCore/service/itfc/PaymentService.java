package com.example.FinCore.service.itfc;

import com.example.FinCore.vo.request.AccountWithDateFilterRequest;
import com.example.FinCore.vo.request.CreatePaymentRequest;
import com.example.FinCore.vo.request.RecoveryPaymentRequest;
import com.example.FinCore.vo.request.StatisticsRequest;
import com.example.FinCore.vo.request.UpdatePaymentRequest;
import com.example.FinCore.vo.response.BasicResponse;
import com.example.FinCore.vo.response.SearchPaymentResponse;
import com.example.FinCore.vo.response.StatisticsIncomeAndOutlayResponse;
import com.example.FinCore.vo.response.StatisticsIncomeAndOutlayWithBalanceInfoResponse;
import com.example.FinCore.vo.response.StatisticsPersonalBalanceWithPaymentTypeResponse;
import com.example.FinCore.vo.response.StatisticsLookupPaymentTypeWithAllBalanceResponse;
import com.example.FinCore.vo.response.StatisticsPaymentDetailsWithBalanceResponse;

public interface PaymentService 
{
	
	/**
	 * 創建款項服務。
	 * @param req 創建請求資料
	 * @return 基本回應資料
	 */
	public BasicResponse create(CreatePaymentRequest req) throws Exception;
	
	/**
	 * 刪除指定帳款服務。值得一提的是，該服務並非直接刪除資料，而是將資料的
	 * 刪除日期進行更新。
	 * @param paymentId 指定款項編號
	 * @return 基本回應資料
	 */
	public BasicResponse delete(int paymentId);
	
	/**
	 * 更新款項內容服務。
	 * @param req 更新請求資料
	 * @return 基本回應資料
	 */
	public BasicResponse update(UpdatePaymentRequest req) throws Exception;
	
	/**
	 * 取得和帳號相關聯的所有款項。
	 * @param account 帳號
	 * @return 存放款項資料的回應資料
	 */
	public SearchPaymentResponse getPaymentInfoByAccount(String account);
	
	/**
	 * 和 {@link PaymentService#getPaymentInfoByAccount(String)} 類似，但增加
	 * 了年月過濾，因此回傳資料時將會鎖定指定年月的款項
	 * @param req 請求資料
	 * @return 存放款項資料的回應資料
	 */
	public SearchPaymentResponse getPaymentInfoByAccountWithDateFilter(AccountWithDateFilterRequest req);
	
	/**
	 * 取得該帳號所有群組相關聯的所有款項。
	 * @param account 帳號
	 * @return 存放款項資料的回應資料，源自於該帳號所在的群組
	 */
	public SearchPaymentResponse getPaymentInfoOfFamily(String account);
	
	/**
	 * 和 {@link PaymentService#getPaymentInfoOfFamily(String)} 類似，但增加
	 * 了年月過濾，因此回傳資料時將會鎖定指定年月的款項
	 * @param req 請求資料
	 * @return 存放款項資料的回應資料，源自於該帳號所在的群組
	 */
	public SearchPaymentResponse getPaymentInfoOfFamilyWithDateFilter(AccountWithDateFilterRequest req);
	
	/**
	 * 可以復原被刪除的款項資料（僅在款項被真正刪除前可用）。
	 * @param req 要恢復的款項編號列表請求資料
	 * @return 基本回應資料
	 */
	public BasicResponse recovery(RecoveryPaymentRequest req);
	
	/**
	 * 查看該帳號所有被刪除的款項
	 * @param account 帳號
	 * @return 存放款項資料的回應資料
	 */
	public SearchPaymentResponse getDeletedPayment(String account);
	
	/**
	 * 查看帳號在指定年月的各項帳款統計。（帳戶分開統計）
	 * @param req 統計專用請求資料
	 * @return 所有帳戶的帳款統計，每個帳戶將分開統計，包含群組帳戶
	 */
	public StatisticsLookupPaymentTypeWithAllBalanceResponse statisticsLookupPaymentTypeWithAllBalance(StatisticsRequest req);
	
	/**
	 * 查看帳號在指定年月的各項帳款統計。（帳戶合併統計）
	 * @param req 統計專用請求資料
	 * @return 指定年月的帳款類型數據統計，所有帳戶將合併統計，不包含群組帳戶
	 */
	public StatisticsPersonalBalanceWithPaymentTypeResponse statisticsLookupPaymentTypeSummarize(StatisticsRequest req);

	/**
	 * 取得帳號在指定時間的所有帳戶支出與收入統計數據。（帳戶合併統計）
	 * @param req 統計專用請求資料
	 * @return 支出與收入的統計數據列表，所有帳戶將合併統計，不包含群組帳戶
	 */
	public StatisticsIncomeAndOutlayResponse statisticsIncomeAndOutlaySummarize(StatisticsRequest req);
	
	/**
	 * 取得帳號在指定時間的所有帳戶支出與收入統計數據。（帳戶分開統計）
	 * @param req 統計專用請求資料
	 * @return 支出與收入的統計數據列表，每個帳戶將分開統計，包含群組帳戶
	 */
	public StatisticsIncomeAndOutlayWithBalanceInfoResponse statisticsIncomeAndOutlayWithAllBalance(StatisticsRequest req);
	
	/**
	 * 取得帳號在指定時間的所有帳戶收入與其細項的統計數據。（帳戶分開統計）
	 * @param req 統計專用請求資料
	 * @return 收入的細項統計數據列表，每個帳戶將分開統計，包含群組帳戶
	 */
	public StatisticsPaymentDetailsWithBalanceResponse statisticsIncomeDetailsWithAllBalance(StatisticsRequest req);
	
	
//	public void statisticsIncomeDetailsSummarize(StatisticsRequest req);
	
}
