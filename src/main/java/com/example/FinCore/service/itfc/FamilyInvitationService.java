package com.example.FinCore.service.itfc;

import java.util.List;

import com.example.FinCore.vo.request.FamilyInvitationRequest;
import com.example.FinCore.vo.response.FamilyInvitationResponse;

/**
 * FamilyInvitation 服務層介面
 * <p>
 * 所有回傳型態都用 FamilyInvitationResponse DTO
 */
public interface FamilyInvitationService {

	/**
	 * 新增家族邀請
	 * 
	 * @param request 前端傳入的邀請請求 DTO
	 * @return 新增後的 FamilyInvitationResponse
	 */
	public FamilyInvitationResponse createInvitation(FamilyInvitationRequest request);

	/**
	 * 取得特定家族所有邀請
	 * 
	 * @param familyId 家族ID
	 * @return 該家族的所有邀請清單（List of Response DTO）
	 */
	public List<FamilyInvitationResponse> getInvitationsByFamilyId(int familyId);

	/**
	 * 查詢單一邀請（依帳號與家族ID）
	 * 
	 * @param account  帳號
	 * @param familyId 家族ID
	 * @return 查詢到的邀請 Response DTO（找不到時回傳 null）
	 */
	public FamilyInvitationResponse getInvitation(String account, int familyId);

	/**
	 * 刪除邀請
	 * 
	 * @param account  帳號
	 * @param familyId 家族ID
	 */
	public void deleteInvitation(String account, int familyId);
	
	/**
	 * 檢查是否已存在指定 familyId、account 的邀請
	 * @param account 受邀帳號
	 * @param familyId 家族ID
	 * @return true 表示已存在邀請，false 表示尚未邀請
	 */
	public boolean existsInvitation(String account, int familyId);

}