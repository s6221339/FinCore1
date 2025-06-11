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

	@Override
	public BasicResponse create(CreateFamilyRequest req) throws Exception {
		System.out.println(req);
		// 1. 檢查必要欄位
		if (req.getOwner() == null || req.getOwner().isEmpty()) {
			return new BasicResponse(ResponseMessages.MISSING_REQUIRED_FIELD.getCode(),
					ResponseMessages.MISSING_REQUIRED_FIELD.getMessage());
		}
		// 2. 成員不能重複
		for (int i = 0; i < req.getInvitor().size(); i++) {
			for (int j = i + 1; j < req.getInvitor().size(); j++) {
				String itemA = req.getInvitor().get(i);
				String itemB = req.getInvitor().get(j);
				if (itemA.equals(itemB)) {
					return new BasicResponse(ResponseMessages.INVITOR_NOT_FOUND.getCode(),
							ResponseMessages.INVITOR_NOT_FOUND.getMessage());
				}
			}
		}
		// 3. 檢查帳號是否存在
		if (!userDao.existsById(req.getOwner())) {
			return new BasicResponse(ResponseMessages.OWNER_NOT_FOUND.getCode(),
					ResponseMessages.OWNER_NOT_FOUND.getMessage());
		}
		for (String member : req.getInvitor())
			if (!userDao.existsById(member))
				return new BasicResponse(ResponseMessages.MEMBER_NOT_FOUND.getCode(),
						ResponseMessages.MEMBER_NOT_FOUND.getMessage());

		// List轉String
		ObjectMapper mapper = new ObjectMapper();
		String invitorStr = mapper.writeValueAsString(req.getInvitor());

		// 4. 建立 Family 物件
		Family family = new Family();
		family.setOwner(req.getOwner());
		family.setName(req.getName());
		family.setInvitor(invitorStr);
		family.setCreateDate(LocalDate.now());

		// 5. 儲存資料庫
		familyDao.save(family);
		return new BasicResponse(ResponseMessages.SUCCESS.getCode(), //
				ResponseMessages.SUCCESS.getMessage());

	}

	@Override
	public BasicResponse update(UpdateFamilyRequest req) {
		// 1. 檢查必要欄位
		if (req.getFamilyId() < 1 || req.getOwner() == null || req.getOwner().isEmpty()) {
			return new BasicResponse(ResponseMessages.MISSING_REQUIRED_FIELD.getCode(),
					ResponseMessages.MISSING_REQUIRED_FIELD.getMessage());
		}
		// 2. 檢查 invitor 重複
		Set<String> invitorSet = new HashSet<>(req.getInvitor());
		if (invitorSet.size() != req.getInvitor().size()) {
			return new BasicResponse(ResponseMessages.DUPLICATE_MEMBER.getCode(), //
					ResponseMessages.DUPLICATE_MEMBER.getMessage());
		}
		// 3. 檢查 owner 不能出現在 invitor
		if (invitorSet.contains(req.getOwner())) {
			return new BasicResponse(ResponseMessages.DUPLICATE_MEMBER.getCode(), //
					ResponseMessages.DUPLICATE_MEMBER.getMessage());
		}
		// 4. 檢查帳號是否都存在
		List<String> allAccounts = new ArrayList<>(invitorSet);
		allAccounts.add(req.getOwner());
		int found = userDao.countByAccountIn(allAccounts);
		if (found < allAccounts.size()) {
			return new BasicResponse(ResponseMessages.INVITOR_NOT_FOUND.getCode(), //
					ResponseMessages.INVITOR_NOT_FOUND.getMessage());
		}
		// 5. 查找家族
		Optional<Family> familyOpt = familyDao.findById(req.getFamilyId());
		if (!familyOpt.isPresent()) {
			return new BasicResponse(ResponseMessages.FAMILY_NOT_FOUND.getCode(), //
					ResponseMessages.FAMILY_NOT_FOUND.getMessage());
		}
		Family family = familyOpt.get();

		// 6. 更新資料（這裡假設 family 只有 owner、invitor 只能放一個）
		family.setOwner(req.getOwner());
		family.setName(req.getName());
		// family.setInvitor(...) // 若有這一欄，請依你實際結構設定
		family.setCreateDate(LocalDate.now());

		try {
			familyDao.save(family);
			return new BasicResponse(ResponseMessages.SUCCESS.getCode(), //
					ResponseMessages.SUCCESS.getMessage());
		} catch (Exception ex) {
			return new BasicResponse(ResponseMessages.UPDATE_FAMILY_FAIL.getCode(),
					ResponseMessages.UPDATE_FAMILY_FAIL.getMessage());
		}
	}

	@Override
	public BasicResponse delete(DeleteFamilyRequest req) {

		Optional<Family> familyOpt = familyDao.findById(req.getFamilyId());
		if (familyOpt.isEmpty()) {
			return new BasicResponse(ResponseMessages.FAMILY_NOT_FOUND.getCode(), //
					ResponseMessages.FAMILY_NOT_FOUND.getMessage());
		}

		try {
			familyDao.deleteById(req.getFamilyId());
			return new BasicResponse(ResponseMessages.SUCCESS.getCode(), //
					ResponseMessages.SUCCESS.getMessage());
		} catch (Exception ex) {
			return new BasicResponse(ResponseMessages.DELETE_FAMILY_FAIL.getCode(),
					ResponseMessages.DELETE_FAMILY_FAIL.getMessage());
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
	public BasicResponse inviteMember(InviteMemberRequest req) {
		// 1. 參數檢查
		if (req.getFamilyId() < 1 || req.getOwner() == null || req.getOwner().isEmpty() //
				|| req.getInvitor() == null || req.getInvitor().isEmpty()) {
			return new BasicResponse(ResponseMessages.MISSING_REQUIRED_FIELD.getCode(),
					ResponseMessages.MISSING_REQUIRED_FIELD.getMessage());
		}
		// 2. 查詢家族資料
		Optional<Family> familyOpt = familyDao.findById(req.getFamilyId());
		if (familyOpt.isEmpty()) {
			return new BasicResponse(ResponseMessages.FAMILY_NOT_FOUND.getCode(),
					ResponseMessages.FAMILY_NOT_FOUND.getMessage());
		}
		Family family = familyOpt.get();// 取得家族 entity
		// 3. 權限驗證
		if (!family.getOwner().equals(req.getOwner())) {
			return new BasicResponse(ResponseMessages.NO_PERMISSION.getCode(), //
					ResponseMessages.NO_PERMISSION.getMessage());
		}
		// 4. 檢查被邀請帳號是否存在
		if (!userDao.existsById(req.getInvitor())) {
			return new BasicResponse(ResponseMessages.INVITOR_NOT_FOUND.getCode(),
					ResponseMessages.INVITOR_NOT_FOUND.getMessage());
		}
		// 5. 邀請操作
		family.setInvitor(req.getInvitor());
		// 6. 儲存資料 回傳成功
		familyDao.save(family);
		return new BasicResponse(ResponseMessages.SUCCESS.getCode(), //
				ResponseMessages.SUCCESS.getMessage());
	}

	@Override
	public BasicResponse dismissFamily(DismissFamilyRequest req) {
		// 1. 參數檢查：familyId、owner 不可空
		if (req.getFamilyId() < 1 || req.getOwner() == null || req.getOwner().isEmpty()) {
			return new BasicResponse(ResponseMessages.MISSING_REQUIRED_FIELD.getCode(),
					ResponseMessages.MISSING_REQUIRED_FIELD.getMessage());
		}
		// 2. 查詢家族
		Optional<Family> familyOpt = familyDao.findById(req.getFamilyId());
		if (familyOpt.isEmpty()) {
			return new BasicResponse(ResponseMessages.FAMILY_NOT_FOUND.getCode(),
					ResponseMessages.FAMILY_NOT_FOUND.getMessage());
		}
		Family family = familyOpt.get();
		// 3. 權限驗證：只有 owner 可解散
		if (!family.getOwner().equals(req.getOwner())) {
			return new BasicResponse(ResponseMessages.FORBIDDEN.getCode(), //
					ResponseMessages.FORBIDDEN.getMessage());
		}
		// 4. 執行刪除
		familyDao.delete(family);
		// 5. 回傳成功訊息
		return new BasicResponse(ResponseMessages.SUCCESS.getCode(), //
				ResponseMessages.SUCCESS.getMessage());
	}

	@Override
	public BasicResponse kickMember(KickMemberRequest req) {
		// 1. 基本參數檢查
		if (req.getFamilyId() < 1 || req.getOwner() == null || req.getOwner().isEmpty()
				|| req.getMemberAccount() == null || req.getMemberAccount().isEmpty()) {
			return new BasicResponse(ResponseMessages.MISSING_REQUIRED_FIELD.getCode(),
					ResponseMessages.MISSING_REQUIRED_FIELD.getMessage());
		}
		// 2. 查詢家族
		Optional<Family> familyOpt = familyDao.findById(req.getFamilyId());
		if (familyOpt.isEmpty()) {
			return new BasicResponse(ResponseMessages.FAMILY_NOT_FOUND.getCode(),
					ResponseMessages.FAMILY_NOT_FOUND.getMessage());
		}
		Family family = familyOpt.get();
		// 3. 權限驗證：只有 owner 能踢人
		if (!family.getOwner().equals(req.getOwner())) {
			return new BasicResponse(ResponseMessages.NO_PERMISSION.getCode(),
					ResponseMessages.NO_PERMISSION.getMessage());
		}

		// 4. 判斷要踢的人是不是 invitor
		if (family.getInvitor() == null || !family.getInvitor().equals(req.getMemberAccount())) {
			return new BasicResponse(ResponseMessages.MEMBER_NOT_FOUND.getCode(),
					ResponseMessages.MEMBER_NOT_FOUND.getMessage());
		}

		// 5. 執行踢出（設 invitor 為 null）
		family.setInvitor(null);
		familyDao.save(family);

		// 6. 回傳成功
		return new BasicResponse(ResponseMessages.SUCCESS.getCode(), //
				ResponseMessages.SUCCESS.getMessage());
	}

	@Override
	public BasicResponse ownerResignAndAssign(OwnerResignAndAssignRequest req) {
		// 1. 參數檢查
		if (req.getFamilyId() < 1 || req.getOldOwner() == null || req.getOldOwner().isEmpty()) {
			return new BasicResponse(ResponseMessages.MISSING_REQUIRED_FIELD.getCode(),
					ResponseMessages.MISSING_REQUIRED_FIELD.getMessage());
		}
		// 2. 查詢家族
		Optional<Family> familyOpt = familyDao.findById(req.getFamilyId());
		if (familyOpt.isEmpty()) {
			return new BasicResponse(ResponseMessages.FAMILY_NOT_FOUND.getCode(),
					ResponseMessages.FAMILY_NOT_FOUND.getMessage());
		}
		Family family = familyOpt.get();

		// 3. 權限驗證
		if (!family.getOwner().equals(req.getOldOwner())) {
			return new BasicResponse(ResponseMessages.NO_PERMISSION.getCode(), //
					ResponseMessages.NO_PERMISSION.getMessage());
		}

		// 4. 決定新 owner
		String newOwner = req.getNewOwner();
		if (newOwner == null || newOwner.isEmpty()) {
			// 如果沒指定新 owner，預設用 invitor
			if (family.getInvitor() != null && !family.getInvitor().isEmpty()) {
				newOwner = family.getInvitor();
			} else {
				// 這邊是用NO_PERMISSION 還是要新增其他Message========================================
				return new BasicResponse(ResponseMessages.NO_PERMISSION.getCode(),
						ResponseMessages.NO_PERMISSION.getMessage());
			}
		}

		// 5. 更新 owner，移除 invitor
		family.setOwner(newOwner);
		// 你可以選擇把原本的 invitor 欄設為 null（因為已經成為 owner）
		if (family.getInvitor() != null && family.getInvitor().equals(newOwner)) {
			family.setInvitor(null);
		}

		familyDao.save(family);
		return new BasicResponse(ResponseMessages.SUCCESS.getCode(), //
				ResponseMessages.SUCCESS.getMessage());
	}

	@Override
	public BasicResponse quitFamily(QuitFamilyRequest req) {
		// 1. 參數檢查
		if (req.getFamilyId() < 1 || req.getMemberAccount() == null || req.getMemberAccount().isEmpty()) {
			return new BasicResponse(ResponseMessages.MISSING_REQUIRED_FIELD.getCode(),
					ResponseMessages.MISSING_REQUIRED_FIELD.getMessage());
		}

		// 2. 查詢家族
		Optional<Family> familyOpt = familyDao.findById(req.getFamilyId());
		if (familyOpt.isEmpty()) {
			return new BasicResponse(ResponseMessages.FAMILY_NOT_FOUND.getCode(),
					ResponseMessages.FAMILY_NOT_FOUND.getMessage());
		}
		Family family = familyOpt.get();

		// 3. 判斷是否為 owner
		if (family.getOwner().equals(req.getMemberAccount())) {
			return new BasicResponse(ResponseMessages.FORBIDDEN.getCode(), //
					ResponseMessages.FORBIDDEN.getMessage());
		}

		// 4. 判斷 invitor 是否為此人
		if (family.getInvitor() != null && family.getInvitor().equals(req.getMemberAccount())) {
			family.setInvitor(null); // 把 invitor 清掉
			familyDao.save(family);
			return new BasicResponse(ResponseMessages.SUCCESS.getCode(), //
					ResponseMessages.SUCCESS.getMessage());
		} else {
			return new BasicResponse(ResponseMessages.INVITOR_NOT_FOUND.getCode(),
					ResponseMessages.INVITOR_NOT_FOUND.getMessage());
		}
	}

	@Override
	public BasicResponse renameFamily(RenameFamilyRequest req) {
		// 1. 參數檢查
		if (req.getFamilyId() < 1 || req.getOwner() == null || req.getOwner().isEmpty() || req.getNewName() == null
				|| req.getNewName().isEmpty()) {
			return new BasicResponse(ResponseMessages.MISSING_REQUIRED_FIELD.getCode(),
					ResponseMessages.MISSING_REQUIRED_FIELD.getMessage());
		}

		// 2. 查詢家族
		Optional<Family> familyOpt = familyDao.findById(req.getFamilyId());
		if (familyOpt.isEmpty()) {
			return new BasicResponse(ResponseMessages.FAMILY_NOT_FOUND.getCode(),
					ResponseMessages.FAMILY_NOT_FOUND.getMessage());
		}
		Family family = familyOpt.get();

		// 3. 權限驗證
		if (!family.getOwner().equals(req.getOwner())) {
			return new BasicResponse(ResponseMessages.NO_PERMISSION.getCode(), //
					ResponseMessages.NO_PERMISSION.getMessage());
		}

		// 4. 更新名稱
		family.setName(req.getNewName());
		familyDao.save(family);

		return new BasicResponse(ResponseMessages.SUCCESS.getCode(), //
				ResponseMessages.SUCCESS.getMessage());
	}
}
