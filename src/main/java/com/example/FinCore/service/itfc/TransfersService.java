package com.example.FinCore.service.itfc;

import com.example.FinCore.vo.request.CreateTransfersRequest;
import com.example.FinCore.vo.response.BasicResponse;
import com.example.FinCore.vo.response.TransfersListResponse;

public interface TransfersService 
{
	
	/**
	 * 建立轉帳紀錄。
	 * @param req 建立請求資料
	 * @return 基本回應資料
	 */
	public BasicResponse create(CreateTransfersRequest req) throws Exception;
	
	/**
	 * 刪除指定編號的轉帳紀錄（僅超級管理員可用）
	 * @param id 轉帳紀錄編號
	 * @return 基本回應資料
	 */
	public BasicResponse delete(String account, int id);
	
	/**
	 * 指定帳戶編號（轉出與匯入），刪除所有與這兩個帳戶「完全關聯」的轉帳紀錄。<br>
	 * ⚠️：該操作僅在這兩個帳戶「均不存在」才可成功操作
	 * @param from 轉出的帳戶編號
	 * @param to 匯入的帳戶編號
	 * @return 基本回應資料
	 */
	public BasicResponse deleteByBalanceId(int from, int to);
	
	/**
	 * 取得所有與指定帳戶關聯的轉帳紀錄。
	 * @param balanceId 帳戶編號
	 * @return 包含與該帳戶關聯的所有轉帳紀錄的回應資料
	 */
	public TransfersListResponse getAllByBalanceId(int balanceId);
	
	/**
	 * 確認轉帳紀錄將要匯入到哪一個帳戶。
	 * @param transfersId 指定轉帳紀錄
	 * @param balanceId 指定帳戶
	 * @return 基本回應封裝資料
	 */
	public BasicResponse confirm(int transfersId, int balanceId);
	
	/**
	 * 取得尚未被登入者確認的所有轉帳紀錄資料。
	 * @return 尚未被確認的所有轉帳紀錄資料
	 */
	public TransfersListResponse getNotConfirmTransfers();
	
}
