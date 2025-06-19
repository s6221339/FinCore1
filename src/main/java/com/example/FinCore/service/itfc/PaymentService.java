package com.example.FinCore.service.itfc;

import com.example.FinCore.vo.request.AccountWithDateFilterRequest;
import com.example.FinCore.vo.request.CreatePaymentRequest;
import com.example.FinCore.vo.request.RecoveryPaymentRequest;
import com.example.FinCore.vo.request.UpdatePaymentRequest;
import com.example.FinCore.vo.response.BasicResponse;
import com.example.FinCore.vo.response.SearchPaymentResponse;

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
	public SearchPaymentResponse getPaymentInfoWithDateFilter(AccountWithDateFilterRequest req);
	
	/**
	 * 可以復原被刪除的款項資料（僅在款項被真正刪除前可用）。
	 * @param req 要恢復的款項編號列表請求資料
	 * @return 基本回應資料
	 */
	public BasicResponse recovery(RecoveryPaymentRequest req);
	
}
