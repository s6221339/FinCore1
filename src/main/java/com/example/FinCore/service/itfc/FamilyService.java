package com.example.FinCore.service.itfc;

import com.example.FinCore.vo.request.CreateFamilyRequest;
import com.example.FinCore.vo.request.DeleteRequest;
import com.example.FinCore.vo.request.UpdateRequest;
import com.example.FinCore.vo.response.BasicResponse;


public interface FamilyService {
	
	/**
	 * 創建群組服務
	 * @param req 創建群族的請求
	 * @return 基本的回應資料
	 */
	public BasicResponse create(CreateFamilyRequest req);
	
	public BasicResponse update(UpdateRequest req);
	
	public BasicResponse delete(DeleteRequest req);
}
