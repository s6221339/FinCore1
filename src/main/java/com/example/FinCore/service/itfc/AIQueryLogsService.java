package com.example.FinCore.service.itfc;

import com.example.FinCore.vo.request.AICallRequest;
import com.example.FinCore.vo.request.AICreateRequest;
import com.example.FinCore.vo.response.AICallbackResponse;
import com.example.FinCore.vo.response.BasicResponse;

public interface AIQueryLogsService 
{
	
	/**
	 * 呼叫AI，並指定要分析的記帳時間，依此分析存在於資料庫的所有帳號記帳資料。分析完成後將<br>
	 * 所有分析結果根據帳號分別寫入資料庫中。如果先前已經分析過該年月的資料，但 {@code forcedWrite} <br>
	 * 未啟用，則寫入會失敗。<p>
	 * 
	 * 如果分析過程遭遇失敗會紀錄錯誤資訊，並跳過對該帳號的分析且在資料庫寫入「空分析資料」。<p>
	 * 
	 * @param year 指定年，限制最小為 {@code 2001}
	 * @param month 指定月，限制範圍為 {@code 1 <= month <= 12}
	 * @param forcedWrite 是否強制覆蓋資料，預設為 {@code FALSE}
	 * @param req year & month & forcedWrite
	 * @return 基本回應封裝資料
	 * @throws Exception 取得統計資料的過程出現錯誤時拋出
	 */
	public BasicResponse create(AICreateRequest req) throws Exception;
	
	/**
	 * 呼叫AI，並指定要分析的年月與要被分析的帳號，返回AI的分析資料。（不寫入資料庫）<br>
	 * 如果先前已經分析過該年月的資料，但 {@code forcedWrite} 未啟用，則視為分析失敗，將返回空資料。
	 * @param account 指定帳號，不可為空值
	 * @param year 指定年，限制最小為 {@code 2001}
	 * @param month 指定月，限制範圍為 {@code 1 <= month <= 12}
	 * @param forcedWrite 是否強制覆蓋資料，預設為 {@code FALSE}
	 * @param req account & year & month & forcedWrite
	 * @return 儲存AI分析結果的封裝資料
	 * @throws Exception 取得統計資料的過程出現錯誤時拋出
	 */
	public AICallbackResponse call(AICallRequest req) throws Exception;
	
}
