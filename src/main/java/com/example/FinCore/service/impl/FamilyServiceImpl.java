package com.example.FinCore.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.FinCore.constants.ResponseMessages;
import com.example.FinCore.dao.FamilyDao;
import com.example.FinCore.dao.UserDao;
import com.example.FinCore.entity.Family;
import com.example.FinCore.service.itfc.FamilyService;
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
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 家族群組相關服務實作
 */
@Service
public class FamilyServiceImpl implements FamilyService {

	@Autowired
	private FamilyDao familyDao;

	@Autowired
	private UserDao userDao;
	
	private final ObjectMapper mapper = new ObjectMapper();

	@Override
	public BasicResponse create(CreateFamilyRequest req) throws Exception {
		System.out.println(req);
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
					return new BasicResponse(ResponseMessages.INVITOR_NOT_FOUND);
				}
			}
		}
		// 3. 檢查帳號是否存在
		if (!userDao.existsById(req.getOwner())) {
			return new BasicResponse(ResponseMessages.OWNER_NOT_FOUND);
		}
		for (String member : req.getInvitor())
			if (!userDao.existsById(member))
				return new BasicResponse(ResponseMessages.MEMBER_NOT_FOUND);

		// List轉String
		String invitorStr = mapper.writeValueAsString(req.getInvitor());

		// 4. 建立 Family 物件
		Family family = new Family();
		family.setOwner(req.getOwner());
		family.setName(req.getName());
		family.setInvitor(invitorStr);
		family.setCreateDate(LocalDate.now());

		// 5. 儲存資料庫
		familyDao.save(family);
		return new BasicResponse(ResponseMessages.SUCCESS);

	}

	@Override
	public BasicResponse update(UpdateFamilyRequest req) {
		
		// 5. 查找家族
		Optional<Family> familyOpt = familyDao.findById(req.familyId());
		if (!familyOpt.isPresent()) {
			return new BasicResponse(ResponseMessages.FAMILY_NOT_FOUND);
		}
		Family family = familyOpt.get();

		// 6. 更新資料（這裡假設 family 只有 owner、invitor 只能放一個）
		family.setName(req.name());
		// family.setInvitor(...) // 若有這一欄，請依你實際結構設定
		family.setCreateDate(LocalDate.now());

		try {
			familyDao.save(family);
			return new BasicResponse(ResponseMessages.SUCCESS);
		} catch (Exception ex) {
			return new BasicResponse(ResponseMessages.UPDATE_FAMILY_FAIL);
		}
	}

	@Override
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

	public FamilyIdResponse getFamilyById(int familyId) {
		if (familyId < 1) {
			return new FamilyIdResponse(ResponseMessages.MISSING_REQUIRED_FIELD.getCode(), //
					ResponseMessages.MISSING_REQUIRED_FIELD.getMessage());
		}
		Optional<Family> familyOpt = familyDao.findById(familyId);
		if (familyOpt.isPresent()) {

			return new FamilyIdResponse(ResponseMessages.SUCCESS.getCode(), //
					ResponseMessages.SUCCESS.getMessage());
		} else {
			return new FamilyIdResponse(ResponseMessages.FAMILY_NOT_FOUND.getCode(), //
					ResponseMessages.FAMILY_NOT_FOUND.getMessage());
		}
	}

	public FamilyListResponse listAllFamily() {
		List<Family> familyList = familyDao.selectAll();
		return new FamilyListResponse(ResponseMessages.SUCCESS.getCode(), //
				ResponseMessages.SUCCESS.getMessage(), familyList);
	}

	@Override
	public BasicResponse inviteMember(InviteMemberRequest req) throws JsonProcessingException {
		// 1. 查詢家族資料
		Optional<Family> familyOpt = familyDao.findById(req.familyId());
		if (familyOpt.isEmpty()) {
			return new BasicResponse(ResponseMessages.FAMILY_NOT_FOUND);
		}
		Family family = familyOpt.get();// 取得家族 entity
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
	    // 6. 更新 family 成員
	    String invitorStr = mapper.writeValueAsString(req.invitor());
	    family.setInvitor(invitorStr);
	    
	    // 7. 儲存資料
	    familyDao.save(family);

	    return new BasicResponse(ResponseMessages.SUCCESS);
	}

	@Override
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
		// 3. 執行刪除
		familyDao.delete(family);
		// 4. 回傳成功訊息
		return new BasicResponse(ResponseMessages.SUCCESS);
	}

	@Override
	public BasicResponse kickMember(KickMemberRequest req) {
		// 1. 查詢家族
		Optional<Family> familyOpt = familyDao.findById(req.getFamilyId());
		if (familyOpt.isEmpty()) {
			return new BasicResponse(ResponseMessages.FAMILY_NOT_FOUND);
		}
		Family family = familyOpt.get();
		// 2. 權限驗證：只有 owner 能踢人
		if (!family.getOwner().equals(req.getOwner())) {
			return new BasicResponse(ResponseMessages.NO_PERMISSION);
		}

		// 3. 判斷要踢的人是不是 invitor
		if (family.getInvitor() == null || !family.getInvitor().equals(req.getMemberAccount())) {
			return new BasicResponse(ResponseMessages.MEMBER_NOT_FOUND);
		}

		// 4. 執行踢出（設 invitor 為 null）
		family.setInvitor(null);
		familyDao.save(family);

		// 5. 回傳成功
		return new BasicResponse(ResponseMessages.SUCCESS);
	}

	@Override
	public BasicResponse ownerResignAndAssign(OwnerResignAndAssignRequest req) {
		// 1. 查詢家族
		Optional<Family> familyOpt = familyDao.findById(req.getFamilyId());
		if (familyOpt.isEmpty()) {
			return new BasicResponse(ResponseMessages.FAMILY_NOT_FOUND);
		}
		Family family = familyOpt.get();

		// 2. 權限驗證
		if (!family.getOwner().equals(req.getOldOwner())) {
			return new BasicResponse(ResponseMessages.NO_PERMISSION);
		}

		// 3. 決定新 owner
		String newOwner = req.getNewOwner();
		if (newOwner == null || newOwner.isEmpty()) {
			// 如果沒指定新 owner，預設用 invitor
			if (family.getInvitor() != null && !family.getInvitor().isEmpty()) {
				newOwner = family.getInvitor();
			} else {
				// 這邊是用NO_PERMISSION 還是要新增其他Message========================================
				return new BasicResponse(ResponseMessages.NO_PERMISSION);
			}
		}

		// 4. 更新 owner，移除 invitor
		family.setOwner(newOwner);
		// 你可以選擇把原本的 invitor 欄設為 null（因為已經成為 owner）
		if (family.getInvitor() != null && family.getInvitor().equals(newOwner)) {
			family.setInvitor(null);
		}

		familyDao.save(family);
		return new BasicResponse(ResponseMessages.SUCCESS);
	}

	@Override
	public BasicResponse quitFamily(QuitFamilyRequest req) {
		// 1. 查詢家族
		Optional<Family> familyOpt = familyDao.findById(req.getFamilyId());
		if (familyOpt.isEmpty()) {
			return new BasicResponse(ResponseMessages.FAMILY_NOT_FOUND);
		}
		Family family = familyOpt.get();

		// 2. 判斷是否為 owner
		if (family.getOwner().equals(req.getMemberAccount())) {
			return new BasicResponse(ResponseMessages.FORBIDDEN);
		}

		// 3. 判斷 invitor 是否為此人
		if (family.getInvitor() != null && family.getInvitor().equals(req.getMemberAccount())) {
			family.setInvitor(null); // 把 invitor 清掉
			familyDao.save(family);
			return new BasicResponse(ResponseMessages.SUCCESS);
		} else {
			return new BasicResponse(ResponseMessages.INVITOR_NOT_FOUND);
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
}
