package com.example.FinCore.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.FinCore.constants.ResponseMessages;
import com.example.FinCore.dao.FamilyDao;
import com.example.FinCore.dao.FamilyInvitationDao;
import com.example.FinCore.dao.UserDao;
import com.example.FinCore.entity.Family;
import com.example.FinCore.entity.FamilyInvitation;
import com.example.FinCore.entity.FamilyInvitationPK;
import com.example.FinCore.entity.User;
import com.example.FinCore.service.itfc.FamilyService;
import com.example.FinCore.vo.FamilyVO;
import com.example.FinCore.vo.SimpleUserVO;
import com.example.FinCore.vo.request.InviteRequest;
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
import com.example.FinCore.vo.response.FamilyInvitationListResponse;
import com.example.FinCore.vo.response.FamilyListResponse;
import com.example.FinCore.vo.response.InviteMemberResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

/**
 * 家族群組相關服務實作
 */
@Service
public class FamilyServiceImpl implements FamilyService {

	@Autowired
	private FamilyDao familyDao;

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private FamilyInvitationDao familyInvitationDao;

	private final ObjectMapper mapper = new ObjectMapper();

	@Override
	@Transactional
	public BasicResponse create(CreateFamilyRequest req) throws Exception {
	    // 1. 檢查必要欄位
	    if (req.getOwner() == null || req.getOwner().isEmpty()) {
	        return new BasicResponse(ResponseMessages.MISSING_REQUIRED_FIELD);
	    }
	    // 2. 成員不能重複
	    for (int i = 0; i < req.getInvitor().size(); i++) {
	        for (int j = i + 1; j < req.getInvitor().size(); j++) {
	            String itemA = req.getInvitor().get(i);
	            String itemB = req.getInvitor().get(j);
	            if (itemA.equals(itemB)) {
	                return new BasicResponse(ResponseMessages.DUPLICATE_FAMILY_MEMBERS);
	            }
	        }
	    }
	    // 2-1. owner 不能邀請自己
	    if (req.getInvitor().contains(req.getOwner())) {
	        return new BasicResponse(ResponseMessages.DUPLICATE_MEMBER);
	    }
	    // 3. 檢查帳號是否存在
	    if (!userDao.existsById(req.getOwner())) {
	        return new BasicResponse(ResponseMessages.OWNER_NOT_FOUND);
	    }
	    for (String member : req.getInvitor())
	        if (!userDao.existsById(member))
	            return new BasicResponse(ResponseMessages.MEMBER_NOT_FOUND);

	    // List轉String
	    @SuppressWarnings("unused")
		String invitorStr = mapper.writeValueAsString(req.getInvitor());
	    
	    // 建立 Family 物件
	    Family family = new Family();
	    family.setOwner(req.getOwner());
	    family.setName(req.getName());
	    // 不要直接把受邀人設成 invitor
	    family.setInvitor(null); // 成員清單先不加人
	    family.setCreateDate(LocalDate.now());

	    // 儲存資料庫
	    familyDao.save(family);
	    int familyId = family.getId();

	    // 幫所有受邀成員建立 FamilyInvitation（邀請中狀態）
	    for (String invitee : req.getInvitor()) {
	        var br = createInvitation(invitee, familyId);
	        if (br != null)
	            return br;
	    }

	    return new BasicResponse(ResponseMessages.SUCCESS);
	}

	@Override
	@Transactional
	public BasicResponse update(UpdateFamilyRequest req) {

		// 1. 查找家族
		Optional<Family> familyOpt = familyDao.findById(req.familyId());
		if (!familyOpt.isPresent()) {
			return new BasicResponse(ResponseMessages.FAMILY_NOT_FOUND);
		}
		Family family = familyOpt.get();

		// 2. 更新資料（這裡假設 family 只有 owner、invitor 只能放一個）
		family.setName(req.name());

		try {
			familyDao.save(family);
			return new BasicResponse(ResponseMessages.SUCCESS);
		} catch (Exception ex) {
			return new BasicResponse(ResponseMessages.UPDATE_FAMILY_FAIL);
		}
	}

	@Override
	@Transactional
	public BasicResponse delete(DeleteFamilyRequest req) {

		Optional<Family> familyOpt = familyDao.findById(req.getFamilyId());
		if (familyOpt.isEmpty()) {
			return new BasicResponse(ResponseMessages.FAMILY_NOT_FOUND);
		}

		try {
			familyDao.deleteById(req.getFamilyId());
			return new BasicResponse(ResponseMessages.SUCCESS);
		} catch (Exception ex) {
			return new BasicResponse(ResponseMessages.DELETE_FAMILY_FAIL);
		}
	}

	public FamilyIdResponse getById(int familyId) {
		if (familyId < 1) {
			return new FamilyIdResponse(ResponseMessages.MISSING_REQUIRED_FIELD);
		}
		Optional<Family> familyOpt = familyDao.findById(familyId);
		if (familyOpt.isPresent()) {

			return new FamilyIdResponse(ResponseMessages.SUCCESS, familyOpt.get());
		} else {
			return new FamilyIdResponse(ResponseMessages.FAMILY_NOT_FOUND);
		}
	}
	
	
	@Override
	public FamilyListResponse listAllFamily() throws JsonProcessingException {
		List<Family> familyList = familyDao.selectAll();
		List<FamilyVO> voList = new ArrayList<>();
		for (Family family : familyList) {
		    // 1. owner 是帳號，要查名字
		    User ownerUser = userDao.selectById(family.getOwner());
		    SimpleUserVO ownerVO = new SimpleUserVO(family.getOwner(), ownerUser != null ? ownerUser.getName() : null);

		    // 2. invitorList: List<String> → List<SimpleUserVO>
		    List<String> invitorList = family.toMemberList();
		    List<SimpleUserVO> memberList = new ArrayList<>();
		    if (invitorList != null) {
		        for (String memberAccount : invitorList) {
		            User memberUser = userDao.selectById(memberAccount);
		            memberList.add(new SimpleUserVO(
		                    memberAccount,
		                    memberUser != null ? memberUser.getName() : null
		            ));
		        }
		    }

		    // 3. 用 SimpleUserVO 填入 FamilyVO
		    FamilyVO vo = new FamilyVO(
		        family.getId(),
		        family.getName(),
		        ownerVO,
		        memberList // 或 memberdata，看你record定義
		    );
		    voList.add(vo);
		}
		return new FamilyListResponse(ResponseMessages.SUCCESS, voList);
	}

	@Override
	@Transactional
	public BasicResponse inviteMember(InviteMemberRequest req) throws JsonProcessingException {
	    // 1. 查詢家族資料
	    Optional<Family> familyOpt = familyDao.findById(req.familyId());
	    if (familyOpt.isEmpty()) {
	        return new BasicResponse(ResponseMessages.FAMILY_NOT_FOUND);
	    }
	    Family family = familyOpt.get();

	    // 2. 權限驗證
	    if (!family.getOwner().equals(req.owner())) {
	        return new BasicResponse(ResponseMessages.NO_PERMISSION);
	    }

	    Set<String> invitorSet = new HashSet<>(req.invitor());
	    // 3. 檢查 invitor 有沒有重複
	    if (invitorSet.size() != req.invitor().size()) {
	        return new BasicResponse(ResponseMessages.DUPLICATE_MEMBER);
	    }
	    // 4. 檢查 owner 不能出現在 invitor
	    if (invitorSet.contains(req.owner())) {
	        return new BasicResponse(ResponseMessages.DUPLICATE_MEMBER);
	    }
	    // 5. 檢查帳號是否都存在
	    int found = userDao.countByAccountIn(new ArrayList<>(invitorSet));
	    if (found < invitorSet.size()) {
	        return new BasicResponse(ResponseMessages.INVITOR_NOT_FOUND);
	    }

	    // 6. 取得目前成員清單
	    String invitorStr = family.getInvitor();
	    List<String> currentList = new ArrayList<>();
	    if (StringUtils.hasText(invitorStr)) {
	        try {
	            currentList = family.toMemberList();
	        } catch (Exception e) {
	            return new BasicResponse(ResponseMessages.UPDATE_FAMILY_FAIL);
	        }
	    }

	    Set<String> allSet = new HashSet<>(currentList);
	    for (String invitee : req.invitor()) {
	        if (invitee.equals(req.owner()) || !allSet.add(invitee)) {
	            return new BasicResponse(ResponseMessages.DUPLICATE_FAMILY_MEMBERS);
	        }
	    }
	 // 7. 發送邀請流程，只要有一個重複邀請就直接 return，不會做批次
	    List<SimpleUserVO> inviteeInfoList = new ArrayList<>();
	    for (String invitee : req.invitor()) {
	        BasicResponse br = createInvitation(invitee, req.familyId());
	        if (br != null) {
	            return br; // 只要有重複邀請就直接 return
	        }
	        String name = userDao.findNameByAccount(invitee);
	        inviteeInfoList.add(new SimpleUserVO(invitee, name));
	    }

	    // 8. 封裝回傳
	    return new InviteMemberResponse(
	        ResponseMessages.SUCCESS.getCode(),
	        ResponseMessages.SUCCESS.getMessage(),
	        inviteeInfoList
	    );
	}
	
	//私有方法
	private BasicResponse createInvitation(String invitee, int familyId)
    {
		FamilyInvitationPK pk = new FamilyInvitationPK(invitee, familyId);
    	if (familyInvitationDao.existsById(pk)) {
            // 已存在邀請：直接回傳 null 或你可以回傳一個特殊的 Response（也可自訂錯誤訊息）
            return new BasicResponse(ResponseMessages.INVITATION_ALREADY_SENT);
        }
    	FamilyInvitation invitation = new FamilyInvitation();
    	invitation.setAccount(invitee);
    	invitation.setFamilyId(familyId);
    	invitation.setStatus(false);
    	familyInvitationDao.save(invitation);
    	return null;
    }
	
	
	@Override
	@Transactional
	public BasicResponse dismissFamily(DismissFamilyRequest req) {
	    // 1. 查詢家族
	    Optional<Family> familyOpt = familyDao.findById(req.getFamilyId());
	    if (familyOpt.isEmpty()) {
	        return new BasicResponse(ResponseMessages.FAMILY_NOT_FOUND);
	    }
	    Family family = familyOpt.get();

	    // 2. 權限驗證：只有 owner 可解散
	    if (!family.getOwner().equals(req.getOwner())) {
	        return new BasicResponse(ResponseMessages.FORBIDDEN);
	    }

	    // 3. 刪除該群組所有邀請資料（family_invitation）
	    familyInvitationDao.deleteAllByFamilyId(req.getFamilyId());

	    // 4. 執行刪除 Family
	    familyDao.delete(family);

	    // 5. 回傳成功訊息
	    return new BasicResponse(ResponseMessages.SUCCESS);
	}

	@Override
	@Transactional
	public BasicResponse kickMember(KickMemberRequest req) {
	    // 1. 查詢家族
	    Optional<Family> familyOpt = familyDao.findById(req.familyId());
	    if (familyOpt.isEmpty()) {
	        return new BasicResponse(ResponseMessages.FAMILY_NOT_FOUND);
	    }
	    Family family = familyOpt.get();

	    // 2. 權限驗證：只有 owner 能踢人
	    if (!family.getOwner().equals(req.owner())) {
	        return new BasicResponse(ResponseMessages.NO_PERMISSION);
	    }

	    // === 檢查陣列不能包含 owner 本人 ===
	    if (req.memberAccounts().contains(family.getOwner())) {
	        return new BasicResponse(ResponseMessages.OWNER_CANNOT_KICK_SELF);
	    }

	    // 3. 取得現有成員名單
	    String invitorStr = family.getInvitor();
	    if (invitorStr == null || invitorStr.isEmpty()) {
	        return new BasicResponse(ResponseMessages.MEMBER_NOT_FOUND);
	    }

	    ObjectMapper mapper = new ObjectMapper();
	    List<String> invitorList;
	    try {
	        invitorList = family.toMemberList();
	    } catch (Exception e) {
	        return new BasicResponse(ResponseMessages.UPDATE_FAMILY_FAIL);
	    }

	    boolean anyRemoved = false;
	    for (String member : req.memberAccounts()) {
	        boolean removed = invitorList.remove(member);
	        anyRemoved = anyRemoved || removed;
	    }
	    if (!anyRemoved) {
	        return new BasicResponse(ResponseMessages.MEMBER_NOT_FOUND);
	    }

	    // 5. 更新成員清單
	    try {
	    	family.toInvitor(invitorList);
//	    	family.setInvitor(mapper.writeValueAsString(invitorList));
	    } catch (Exception e) {
	        return new BasicResponse(ResponseMessages.UPDATE_FAMILY_FAIL);
	    }

	    // 6. 存進資料庫
	    familyDao.save(family);

	    // 7. 同步刪除 family_invitation 的邀請紀錄
	    familyInvitationDao.deleteByFamilyIdAndAccounts(req.familyId(), req.memberAccounts());

	    // 8. 回傳成功
	    return new BasicResponse(ResponseMessages.SUCCESS);
	    }
	
	@Override
	@Transactional
	public BasicResponse ownerQuit(OwnerResignAndAssignRequest req) {
		// 1. 檢查帳號是否存在於資料庫
	    if (!userDao.existsById(req.getOldOwner())) {
	        return new BasicResponse(ResponseMessages.ACCOUNT_NOT_FOUND);
	    }

	    // 2. 查詢家族
	    Optional<Family> familyOpt = familyDao.findById(req.getFamilyId());
	    if (familyOpt.isEmpty()) {
	        return new BasicResponse(ResponseMessages.FAMILY_NOT_FOUND);
	    }
	    Family family = familyOpt.get();

	    // 3. 檢查此帳號是否存在於這個家族（owner 或 invitor）
	    boolean inFamily = false;
	    // 3-1. owner
	    if (family.getOwner().equals(req.getOldOwner())) {
	        inFamily = true;
	    }
	    // 3-2. invitor（多成員的情境，需要把 JSON 轉 List 來比對）
	    String invitorStr = family.getInvitor();
	    if (!inFamily && invitorStr != null && !invitorStr.isEmpty()) {
	        try {
	            List<String> invitorList = family.toMemberList();
	            if (invitorList.contains(req.getOldOwner())) {
	                inFamily = true;
	            }
	        } catch (Exception e) {
	            return new BasicResponse(ResponseMessages.UPDATE_FAMILY_FAIL);
	        }
	    }
	    if (!inFamily) {
	        return new BasicResponse(ResponseMessages.ACCOUNT_NOT_IN_FAMILY);
	    }

	    // 4. 權限驗證（必須是 owner）
	    if (!family.getOwner().equals(req.getOldOwner())) {
	        return new BasicResponse(ResponseMessages.NO_PERMISSION);
	    }

	    // 5. 決定新 owner
	    String newOwner = req.getNewOwner();
	    if (newOwner == null || newOwner.isEmpty()) {
	        // 沒指定新 owner，預設用 invitor 的第一人
	        if (invitorStr != null && !invitorStr.isEmpty()) {
	            List<String> invitorList;
	            try {
	                invitorList = family.toMemberList();
	            } catch (Exception e) {
	                return new BasicResponse(ResponseMessages.UPDATE_FAMILY_FAIL);
	            }
	            if (invitorList.isEmpty()) {
	                return new BasicResponse(ResponseMessages.NO_PERMISSION); // 或自定義訊息「無可指派成員」
	            }
	            newOwner = invitorList.get(0);
	            invitorList.remove(0);
	            if (invitorList.isEmpty()) {
	                family.setInvitor(null);
	            } else {
	                try {
	                    family.setInvitor(mapper.writeValueAsString(invitorList));
	                } catch (Exception e) {
	                    return new BasicResponse(ResponseMessages.UPDATE_FAMILY_FAIL);
	                }
	            }
	        } else {
	            return new BasicResponse(ResponseMessages.NO_PERMISSION);
	        }
	    } else {
	        // 有指定新 owner，且他在 invitor 名單內也要移除
	        if (invitorStr != null && !invitorStr.isEmpty()) {
	            List<String> invitorList;
	            try {
	                invitorList = family.toMemberList();

	            } catch (Exception e) {
	                return new BasicResponse(ResponseMessages.UPDATE_FAMILY_FAIL);
	            }
	            boolean removed = invitorList.remove(newOwner);
	            if (removed) {
	                try {
	                    if (invitorList.isEmpty()) {
	                        family.setInvitor(null);
	                    } else {
	                        family.setInvitor(mapper.writeValueAsString(invitorList));
	                    }
	                } catch (Exception e) {
	                    return new BasicResponse(ResponseMessages.UPDATE_FAMILY_FAIL);
	                }
	            }
	        }
	    }

	    // 6. 更新 owner
	    family.setOwner(newOwner);
	    familyDao.save(family);
	    return new BasicResponse(ResponseMessages.SUCCESS);
	}
	
	@Override
	@Transactional
	public BasicResponse transferOwner(OwnerResignAndAssignRequest req) {
		// 1. 檢查舊 owner 帳號是否存在於資料庫
	    if (!userDao.existsById(req.getOldOwner())) {
	        return new BasicResponse(ResponseMessages.ACCOUNT_NOT_FOUND);
	    }

	    // 2. 查詢家族資料
	    Optional<Family> familyOpt = familyDao.findById(req.getFamilyId());
	    if (familyOpt.isEmpty()) {
	        return new BasicResponse(ResponseMessages.FAMILY_NOT_FOUND);
	    }
	    Family family = familyOpt.get();

	    // 3. 檢查舊 owner 是否存在於該家族（owner 或 invitor）
	    boolean inFamily = false;
	    // 3-1. 檢查是否為 owner
	    if (family.getOwner().equals(req.getOldOwner())) {
	        inFamily = true;
	    }
	    // 3-2. 檢查是否在 invitor 清單
	    String invitorStr = family.getInvitor();
	    List<String> invitorList = null;
	    if (!inFamily && invitorStr != null && !invitorStr.isEmpty()) {
	        try {
	            invitorList = family.toMemberList();
	            if (invitorList.contains(req.getOldOwner())) {
	                inFamily = true;
	            }
	        } catch (Exception e) {
	            return new BasicResponse(ResponseMessages.UPDATE_FAMILY_FAIL);
	        }
	    }
	    if (!inFamily) {
	        return new BasicResponse(ResponseMessages.ACCOUNT_NOT_IN_FAMILY);
	    }

	    // 4. 權限驗證（必須是現任 owner）
	    if (!family.getOwner().equals(req.getOldOwner())) {
	        return new BasicResponse(ResponseMessages.NO_PERMISSION);
	    }

	    // 5. 驗證新 owner（不能為空，不能為現任 owner，本身要是家庭成員）
	    String newOwner = req.getNewOwner();
	    if (newOwner == null || newOwner.isEmpty()) {
	        return new BasicResponse(ResponseMessages.MISSING_REQUIRED_FIELD); // 新 owner 必須指定
	    }
	    if (newOwner.equals(req.getOldOwner())) {
	        return new BasicResponse(ResponseMessages.SAME_OWNER_TRANSFER_INVALID); // 不可轉讓給自己
	    }

	    // 取得 invitorList，如果沒取過就初始化
	    if (invitorList == null) {
	        try {
	            invitorList = family.toMemberList();
	        } catch (Exception e) {
	            return new BasicResponse(ResponseMessages.UPDATE_FAMILY_FAIL);
	        }
	    }
	    if (invitorList == null) {
	        invitorList = new java.util.ArrayList<>();
	    }

	    // 新 owner 必須是家庭成員（invitor）
	    if (!invitorList.contains(newOwner)) {
	        return new BasicResponse(ResponseMessages.ACCOUNT_NOT_IN_FAMILY); // 指定的新 owner 不在家庭名單
	    }

	    // 6. 權限轉讓處理
	    // 6-1. 將新 owner 從 invitor 名單移除（因為他要升級為 owner）
	    invitorList.remove(newOwner);

	    // 6-2. 將舊 owner 加回 invitor 名單（若已經不是 invitor，再加入）
	    if (!invitorList.contains(req.getOldOwner())) {
	        invitorList.add(req.getOldOwner());
	    }

	    // 6-3. 更新 invitor 欄位（轉回 JSON）
	    try {
	        if (invitorList.isEmpty()) {
	            family.setInvitor(null);
	        } else {
	            family.setInvitor(new ObjectMapper().writeValueAsString(invitorList));
	        }
	    } catch (Exception e) {
	        return new BasicResponse(ResponseMessages.UPDATE_FAMILY_FAIL);
	    }

	    // 6-4. 更新 owner 欄位
	    family.setOwner(newOwner);

	    // 7. 儲存異動
	    familyDao.save(family);

	    return new BasicResponse(ResponseMessages.SUCCESS);
	}

	@Override
	@Transactional
	public BasicResponse quitFamily(QuitFamilyRequest req) {
	    // 1. 查詢家族
	    Optional<Family> familyOpt = familyDao.findById(req.getFamilyId());
	    if (familyOpt.isEmpty()) {
	        return new BasicResponse(ResponseMessages.FAMILY_NOT_FOUND);
	    }
	    Family family = familyOpt.get();

	    String memberAccount = req.getMemberAccount();

	    // 2. 檢查欲退出成員是否在家族中（owner 或 invitor）
	    boolean inFamily = false;
	    String invitorStr = family.getInvitor();
	    List<String> invitorList = null;
	    if (family.getOwner().equals(memberAccount)) {
	        inFamily = true;
	    }
	    if (invitorStr != null && !invitorStr.isEmpty()) {
	        try {
	            invitorList = family.toMemberList();
	            if (!inFamily && invitorList.contains(memberAccount)) {
	                inFamily = true;
	            }
	        } catch (Exception e) {
	            return new BasicResponse(ResponseMessages.UPDATE_FAMILY_FAIL);
	        }
	    }
	    if (!inFamily) {
	        return new BasicResponse(ResponseMessages.ACCOUNT_NOT_IN_FAMILY);
	    }

	    // 3. 判斷是否為 owner
	    if (family.getOwner().equals(memberAccount)) {
	        // 檢查是否只有 owner 一人
	        if (invitorList == null || invitorList.isEmpty()) {
	            // 只剩 owner，直接解散群組（包含刪除 family_invitation）
	            familyInvitationDao.deleteAllByFamilyId(req.getFamilyId());
	            familyDao.delete(family);
	            return new BasicResponse(ResponseMessages.SUCCESS);
	        } else {
	            // 有成員可指派，owner 退出並升級第一位成員為新 owner
	            String newOwner = invitorList.get(0);
	            invitorList.remove(0);

	            try {
	    	    	family.toInvitor(invitorList);
//	    	    	family.setInvitor(mapper.writeValueAsString(invitorList));
	    	    } catch (Exception e) {
	    	        return new BasicResponse(ResponseMessages.UPDATE_FAMILY_FAIL);
	    	    }

	            family.setOwner(newOwner);
	            familyDao.save(family);

	            // owner 退出 → 刪除原 owner 在邀請表的紀錄（若有）
	            familyInvitationDao.deleteByAccountAndFamilyId(memberAccount, req.getFamilyId());
	            return new BasicResponse(ResponseMessages.SUCCESS);
	        }
	    } else {
	        // 4. 一般成員退出
	        if (invitorList == null || !invitorList.remove(memberAccount)) {
	            return new BasicResponse(ResponseMessages.MEMBER_NOT_FOUND);
	        }

	        try {
    	    	family.toInvitor(invitorList);
//    	    	family.setInvitor(mapper.writeValueAsString(invitorList));
    	    } catch (Exception e) {
    	        return new BasicResponse(ResponseMessages.UPDATE_FAMILY_FAIL);
    	    }
	        familyDao.save(family);

	        // 刪除 family_invitation 紀錄
	        familyInvitationDao.deleteByAccountAndFamilyId(memberAccount, req.getFamilyId());
	        return new BasicResponse(ResponseMessages.SUCCESS);
	    }
	}
	
	

	@Override
	public BasicResponse renameFamily(RenameFamilyRequest req) {
		// 1. 查詢家族
		Optional<Family> familyOpt = familyDao.findById(req.getFamilyId());
		if (familyOpt.isEmpty()) {
			return new BasicResponse(ResponseMessages.FAMILY_NOT_FOUND);
		}
		Family family = familyOpt.get();

		// 2. 權限驗證
		if (!family.getOwner().equals(req.getOwner())) {
			return new BasicResponse(ResponseMessages.NO_PERMISSION);
		}

		// 3. 更新名稱
		family.setName(req.getNewName());
		familyDao.save(family);

		return new BasicResponse(ResponseMessages.SUCCESS);
	}
	
	@Override
	@Transactional
	public BasicResponse acceptInvite(InviteRequest req) {
	    // 檢查參數
	    if (req.getAccount() == null || req.getAccount().isEmpty()) {
	        return new BasicResponse(ResponseMessages.MISSING_REQUIRED_FIELD);
	    }

	    // 查詢邀請紀錄
	    FamilyInvitationPK pk = new FamilyInvitationPK(req.getAccount(), req.getFamilyId());
	    Optional<FamilyInvitation> invitationOpt = familyInvitationDao.findById(pk);
	    if (invitationOpt.isEmpty()) {
	        return new BasicResponse(ResponseMessages.INVITATION_NOT_FOUND); // 你可以自訂訊息
	    }

	    FamilyInvitation invitation = invitationOpt.get();
	    // 狀態本來就接受過就直接 return
	    if (invitation.isStatus()) {
	        return new BasicResponse(ResponseMessages.INVITATION_ALREADY_ACCEPTED);
	    }

	    // 設定為已接受
	    invitation.setStatus(true);
	    familyInvitationDao.save(invitation);

	    // 加入 family 的 invitor 名單
	    Optional<Family> familyOpt = familyDao.findById(req.getFamilyId());
	    if (familyOpt.isEmpty()) {
	        return new BasicResponse(ResponseMessages.FAMILY_NOT_FOUND);
	    }
	    Family family = familyOpt.get();

	    // 取得成員清單
	    List<String> invitorList = new ArrayList<>();
	    String invitorStr = family.getInvitor();
	    if (StringUtils.hasText(invitorStr)) {
	        try {
	            invitorList = family.toMemberList();
	        } catch (Exception e) {
	            return new BasicResponse(ResponseMessages.UPDATE_FAMILY_FAIL);
	        }
	    }
	    if (!invitorList.contains(req.getAccount())) {
	        invitorList.add(req.getAccount());
	        try {
	            family.setInvitor(mapper.writeValueAsString(invitorList));
	            familyDao.save(family);
	        } catch (Exception e) {
	            return new BasicResponse(ResponseMessages.UPDATE_FAMILY_FAIL);
	        }
	    }

	    return new BasicResponse(ResponseMessages.SUCCESS);
	}
	
	@Override
	@Transactional
	public BasicResponse rejectInvite(InviteRequest req) {
	    // 檢查參數
	    if (req.getAccount() == null || req.getAccount().isEmpty()) {
	        return new BasicResponse(ResponseMessages.MISSING_REQUIRED_FIELD);
	    }

	    // 查詢邀請紀錄
	    FamilyInvitationPK pk = new FamilyInvitationPK(req.getAccount(), req.getFamilyId());
	    Optional<FamilyInvitation> invitationOpt = familyInvitationDao.findById(pk);
	    if (invitationOpt.isEmpty()) {
	        return new BasicResponse(ResponseMessages.INVITATION_NOT_FOUND); // 你可以自訂訊息
	    }

	    // 刪除該邀請
	    familyInvitationDao.deleteById(pk);
	    return new BasicResponse(ResponseMessages.SUCCESS);
	}
	
	/**
	 * 查詢某 family_id 目前發出的「邀請中」名單
	 * @param familyId 家庭群組ID
	 * @return 邀請中的成員清單（account、name）
	 */
	public FamilyInvitationListResponse getInvitingList(int familyId) {
	    // 從資料庫查詢該群組「邀請中」(status=0) 的邀請
	    List<FamilyInvitation> invites = familyInvitationDao.findByFamilyIdAndStatusFalse(familyId);

	    List<FamilyInvitationListResponse.InviteeInfo> inviteeList = new ArrayList<>();
	    for (FamilyInvitation inv : invites) {
	        FamilyInvitationListResponse.InviteeInfo info = new FamilyInvitationListResponse.InviteeInfo();
	        info.setAccount(inv.getAccount());
	        // 查詢受邀人名稱
	        User user = userDao.selectById(inv.getAccount());
	        info.setName(user != null ? user.getName() : null);
	        inviteeList.add(info);
	    }

	    FamilyInvitationListResponse resp = new FamilyInvitationListResponse();
	    resp.setFamilyId(familyId);
	    resp.setInviteeList(inviteeList);
	    return resp;
	}
}
