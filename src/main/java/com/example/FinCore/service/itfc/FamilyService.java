package com.example.FinCore.service.itfc;

import com.example.FinCore.vo.request.CreateFamilyRequest;
import com.example.FinCore.vo.request.DeleteFamilyRequest;
import com.example.FinCore.vo.request.DismissFamilyRequest;
import com.example.FinCore.vo.request.InviteMemberRequest;
import com.example.FinCore.vo.request.InviteRequest;
import com.example.FinCore.vo.request.KickMemberRequest;
import com.example.FinCore.vo.request.OwnerResignAndAssignRequest;
import com.example.FinCore.vo.request.QuitFamilyRequest;
import com.example.FinCore.vo.request.RenameFamilyRequest;
import com.example.FinCore.vo.request.UpdateFamilyRequest;
import com.example.FinCore.vo.response.BasicResponse;
import com.example.FinCore.vo.response.FamilyIdResponse;
import com.example.FinCore.vo.response.FamilyInvitationListResponse;
import com.example.FinCore.vo.response.FamilyInvitationStatusListResponse;
import com.example.FinCore.vo.response.FamilyListResponse;
import com.fasterxml.jackson.core.JsonProcessingException;


public interface FamilyService {
	
	/**
     * 新增家族群組
     * @param name 新建的群組名稱
     * @param owner 家族群組管理者
     * @param invitor 家族群組成員
     * @return 基本回應物件
     */
	public BasicResponse create(CreateFamilyRequest req) throws Exception;
	
	/**
     * 更新家族群組資料
     * @param familyId 家族ID
     * @param name 變更的群組名稱
     * @return 基本回應物件
     */
	public BasicResponse update(UpdateFamilyRequest req);
	
	/**
     * 刪除家族群組
     * @param familyId 家族ID(要刪除的群組)
     * @return 基本回應物件
     */
	public BasicResponse delete(DeleteFamilyRequest req);
	
	/**
     * 查詢單一家族群組資料
     * @param familyId 家族ID
     * @return 查詢結果，成功時 data 欄位可攜帶查詢結果
     */
	public FamilyIdResponse getById(int familyId);
	
	/**
	 * 查詢所有家族群組
	 * @return 家族清單
	 */
	public FamilyListResponse listAllFamily() throws JsonProcessingException;
	
	/**
	 * Owner 邀請新成員加入家族
	 * @param familyId 家族ID
	 * @param invitor 邀請的成員帳號
	 * @return 回應物件
	 */
	public BasicResponse inviteMember(InviteMemberRequest req) throws JsonProcessingException;
	
	/**
	 * 解散家族群組。
	 * 除了刪除 Family 之外，也會同步移除 family_invitation 表中該 familyId 的所有邀請資料。
	 *
	 * @param req DismissFamilyRequest，內含 familyId 與 owner
	 * @return BasicResponse
	 */
	public BasicResponse dismissFamily(DismissFamilyRequest req);
	
	/**
	 * 踢出家族成員（owner 或管理員操作）
	 * @param familyId 家族ID
	 * @param owner 操作者帳號（驗證身分）
	 * @param memberAccount 被踢成員帳號
	 * @return 回應物件
	 */
	public BasicResponse kickMember(KickMemberRequest req);

	/**
	 * owner 退出家庭群組
	 * 
	 * @param familyId 家族ID
	 * @param oldOwner 現任 owner 帳號
	 * @param newOwner 指派的新 owner，若沒傳則預設用成員的第一人
	 * @return 回應物件
	 */
	public BasicResponse ownerQuit(OwnerResignAndAssignRequest req);
	
	
	/**
	 * owner 指派新 owner ，舊 owner 變成家庭成員
	 * @param familyId 家族ID
	 * @param oldOwner 現任 owner 帳號
	 * @param newOwner 指派的新 owner
	 * @return 回應物件
	 */
	public BasicResponse transferOwner(OwnerResignAndAssignRequest req);
	
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
	 * @param owner 管理者帳號（驗證身份）
	 * @param newName 新名稱
	 * @return 回應物件
	 */
	public BasicResponse renameFamily(RenameFamilyRequest req);
	
	/**
	 * 被邀請人接受邀請並加入家族
	 * @param account 被邀請人
	 * @param　familyId 家族ID
	 * @return 回應物件
	 */
	public BasicResponse acceptInvite(InviteRequest req) throws JsonProcessingException;
	
	/**
     * 被邀請人拒絕邀請
     * @param account 被邀請人
	 * @param　familyId 家族ID
     * @return 回應物件
     */
    public BasicResponse rejectInvite(InviteRequest req) throws JsonProcessingException;
    
    
    
    /**
     * 查詢指定家庭群組（familyId）目前所有尚未接受邀請（邀請中）的成員資訊。
     * 
     * 只會回傳狀態為「邀請中」（status=false）的成員名單，
     * 並包含每位受邀人的帳號（account）與對應的名稱（name），
     * 不會顯示狀態。
     *
     * @param familyId 家庭群組的唯一識別碼
     * @return 家庭邀請中成員清單回應物件，內含家族ID與邀請中成員資訊（account、name）
     */
    public FamilyInvitationListResponse getInvitingList(int familyId);
   
    
    /**
     * Owner 取消發出的邀請（只能 owner 執行，刪除 family_invitation 資料）
     * @param familyId 家族群組ID
     * @param owner 帳號（必須是該家族 owner）
     * @param invitee 受邀人帳號
     * @return BasicResponse 執行結果
     */
    public BasicResponse cancelInvite(int familyId, String owner, String invitee);
    
    /**
     * 查詢該帳號收到的所有家族邀請
     * @param account 受邀帳號
     * @return 該帳號所有家族邀請資訊
     */
    public FamilyInvitationStatusListResponse getStatusByAccount(String account);

}
