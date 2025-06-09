package com.example.FinCore.service.itfc;

import com.example.FinCore.vo.request.CreateFamilyRequest;
import com.example.FinCore.vo.request.DeleteFamilyRequest;
import com.example.FinCore.vo.request.DismissFamilyRequest;
import com.example.FinCore.vo.request.InviteMemberRequest;
import com.example.FinCore.vo.request.KickMemberRequest;
import com.example.FinCore.vo.request.OwnerResignAndAssignRequest;
import com.example.FinCore.vo.request.QuitFamilyRequest;
import com.example.FinCore.vo.request.RenameFamilyRequest;
import com.example.FinCore.vo.request.UpdateFamilyRequest;
import com.example.FinCore.vo.response.BasicResponse;
import com.example.FinCore.vo.response.FamilyIdResponse;
import com.example.FinCore.vo.response.FamilyListResponse;
import com.fasterxml.jackson.core.JsonProcessingException;


public interface FamilyService {
	
	/**
     * 新增家族群組
     * @param req 新增請求
     * @return 基本回應物件
     */
	public BasicResponse create(CreateFamilyRequest req) throws Exception;
	
	/**
     * 更新家族群組資料
     * @param req 更新請求
     * @return 基本回應物件
     */
	public BasicResponse update(UpdateFamilyRequest req);
	
	/**
     * 刪除家族群組
     * @param req 刪除請求
     * @return 基本回應物件
     */
	public BasicResponse delete(DeleteFamilyRequest req);
	
	/**
     * 查詢單一家族群組資料
     * @param familyId 家族ID
     * @return 查詢結果，成功時 data 欄位可攜帶查詢結果
     */
	public FamilyIdResponse getFamilyById(int familyId);
	
	/**
	 * 查詢所有家族群組
	 * @return 家族清單
	 */
	public FamilyListResponse listAllFamily();
	
	/*
	 * TODO: 5個功能
	 * 1.owner 的邀請.
	 * 2.解散.
	 * 3.踢出，owner的退出需要指派家庭群組裡的一人，若沒指派，取群組成員的第一人
	 * 4.成員可退出
	 * 5.owner 可改群組名稱
	 */
	
	/**
	 * Owner 邀請新成員加入家族
	 * @param familyId 家族ID
	 * @param invitor 邀請的成員帳號
	 * @return 回應物件
	 */
	public BasicResponse inviteMember(InviteMemberRequest req) throws JsonProcessingException;
	
	/**
	 * 解散家族（僅限 owner）
	 * @param familyId 家族ID
	 * @param owner 操作者帳號（驗證身份）
	 * @return 回應物件
	 */
	public BasicResponse dismissFamily(DismissFamilyRequest req);
	
	/**
	 * 踢出家族成員（owner 或管理員操作）
	 * @param familyId 家族ID
	 * @param operator 操作者帳號（驗證身分）
	 * @param memberToKick 被踢成員帳號
	 * @return 回應物件
	 */
	public BasicResponse kickMember(KickMemberRequest req);

	/**
	 * owner 退出並指派新 owner
	 * @param familyId 家族ID
	 * @param oldOwner 現任 owner 帳號
	 * @param newOwner 指派的新 owner，若沒傳則預設用成員的第一人
	 * @return 回應物件
	 */
	public BasicResponse ownerResignAndAssign(OwnerResignAndAssignRequest req);
	
	/**
	 * 成員自行退出家族
	 * @param familyId 家族ID
	 * @param member 成員帳號
	 * @return 回應物件
	 */
	public BasicResponse quitFamily(QuitFamilyRequest req);
	
	/**
	 * owner 更改家族名稱
	 * @param familyId 家族ID
	 * @param owner 操作者帳號（驗證身份）
	 * @param newName 新名稱
	 * @return 回應物件
	 */
	public BasicResponse renameFamily(RenameFamilyRequest req);
	
	
	


}
