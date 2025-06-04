package com.example.FinCore.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.FinCore.constants.ResponseMessages;
import com.example.FinCore.dao.FamilyDao;
import com.example.FinCore.dao.UserDao;
import com.example.FinCore.entity.Family;
import com.example.FinCore.service.itfc.FamilyService;
import com.example.FinCore.vo.request.CreateFamilyRequest;
import com.example.FinCore.vo.request.DeleteRequest;
import com.example.FinCore.vo.request.UpdateRequest;
import com.example.FinCore.vo.response.BasicResponse;

/**
 * 家族群組相關服務實作
 */
public class FamilyServiceImpl implements FamilyService {

	@Autowired
	private FamilyDao familyDao;

	@Autowired
	private UserDao userDao;

	/**
     * 新增家族群組
     * 1. 檢查必要欄位
     * 2. 成員不可重複
     * 3. 擁有者帳號需存在
     * 4. 新增家族資料
     * @param req 新增請求
     * @return 基本回應物件
     */
	@Override
	public BasicResponse create(CreateFamilyRequest req) {
		// 1. 檢查必要欄位
		if (req == null || req.getOwner() == null || req.getOwner().isEmpty() || req.getInvitor() == null
				|| req.getInvitor().isEmpty()) {
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

		// 4. 建立 Family 物件
		Family family = new Family();
		family.setOwner(req.getOwner());
		family.setCreateDate(LocalDate.now());

		// 5. 儲存資料庫
		try {
			familyDao.save(family);
			return new BasicResponse(ResponseMessages.SUCCESS.getCode(), ResponseMessages.SUCCESS.getMessage());
		} catch (Exception ex) {
			return new BasicResponse(ResponseMessages.CREATE_FAMILY_FAIL.getCode(),
					ResponseMessages.CREATE_FAMILY_FAIL.getMessage());
		}
	}

	
	/**
     * 更新家族群組資料
     * 1. 檢查必要欄位
     * 2. 成員不可重複且 owner 不能同時為受邀人
     * 3. 檢查所有帳號是否存在
     * 4. 檢查家族是否存在
     * 5. 更新資料
     * @param req 更新請求
     * @return 基本回應物件
     */
	@Override
	public BasicResponse update(UpdateRequest req) {
		// 1. 檢查必要欄位
		if (req == null || req.getFamilyId() < 1 || req.getOwner() == null || req.getOwner().isEmpty()) {
			return new BasicResponse(ResponseMessages.MISSING_REQUIRED_FIELD.getCode(),
					ResponseMessages.MISSING_REQUIRED_FIELD.getMessage());
		}
		// 2. 檢查 invitor 重複
		Set<String> invitorSet = new HashSet<>(req.getInvitor());
		if (invitorSet.size() != req.getInvitor().size()) {
			return new BasicResponse(ResponseMessages.DUPLICATE_MEMBER.getCode(), "受邀成員不可重複");
		}
		// 3. 檢查 owner 不能出現在 invitor
		if (invitorSet.contains(req.getOwner())) {
			return new BasicResponse(ResponseMessages.DUPLICATE_MEMBER.getCode(), "擁有者不可同時為受邀人");
		}
		// 4. 檢查帳號是否都存在
		List<String> allAccounts = new ArrayList<>(invitorSet);
		allAccounts.add(req.getOwner());
		int found = userDao.countByAccountIn(allAccounts);
		if (found < allAccounts.size()) {
			return new BasicResponse(ResponseMessages.INVITOR_NOT_FOUND.getCode(), "部分帳號不存在");
		}
		// 5. 查找家族
		Optional<Family> familyOpt = familyDao.findById(req.getFamilyId());
		if (!familyOpt.isPresent()) {
			return new BasicResponse(ResponseMessages.FAMILY_NOT_FOUND.getCode(), "家族不存在");
		}
		Family family = familyOpt.get();

		// 6. 更新資料（這裡假設 family 只有 owner、invitor 只能放一個）
		family.setOwner(req.getOwner());
		// family.setInvitor(...) // 若有這一欄，請依你實際結構設定
		family.setCreateDate(LocalDate.now());

		try {
			familyDao.save(family);
			return new BasicResponse(ResponseMessages.SUCCESS.getCode(), ResponseMessages.SUCCESS.getMessage());
		} catch (Exception ex) {
			return new BasicResponse(ResponseMessages.UPDATE_FAMILY_FAIL.getCode(),
					ResponseMessages.UPDATE_FAMILY_FAIL.getMessage());
		}
	}

	/**
     * 刪除家族群組
     * 1. 檢查必要欄位
     * 2. 查找家族是否存在
     * 3. 執行刪除
     * @param req 刪除請求
     * @return 基本回應物件
     */
	@Override
	public BasicResponse delete(DeleteRequest req) {
		// 1. 檢查必要欄位
		if (req == null || req.getFamilyId() < 1) {
			return new BasicResponse(ResponseMessages.MISSING_REQUIRED_FIELD.getCode(),
					ResponseMessages.MISSING_REQUIRED_FIELD.getMessage());
		}

		// 2. 查找是否存在
		Optional<Family> familyOpt = familyDao.findById(req.getFamilyId());
		if (!familyOpt.isPresent()) {
			return new BasicResponse(ResponseMessages.FAMILY_NOT_FOUND.getCode(), "家族不存在");
		}

		try {
			familyDao.deleteById(req.getFamilyId());
			return new BasicResponse(ResponseMessages.SUCCESS.getCode(), ResponseMessages.SUCCESS.getMessage());
		} catch (Exception ex) {
			return new BasicResponse(ResponseMessages.DELETE_FAMILY_FAIL.getCode(),
					ResponseMessages.DELETE_FAMILY_FAIL.getMessage());
		}
	}
	
	/**
     * 查詢單一家族群組資料
     * @param familyId 家族ID
     * @return 查詢結果，成功時 data 欄位可攜帶查詢結果
     */
	public BasicResponse getFamilyById(int familyId) {
	    if (familyId < 1) {
	        return new BasicResponse(ResponseMessages.MISSING_REQUIRED_FIELD.getCode(),
	                "familyId 不能為空或小於 1");
	    }
	    Optional<Family> familyOpt = familyDao.findById(familyId);
	    if (familyOpt.isPresent()) {
	        // 這裡可以包一層 VO，或者直接回物件
	        return new BasicResponse(ResponseMessages.SUCCESS.getCode(),//
	        		ResponseMessages.SUCCESS.getMessage());
	    } else {
	        return new BasicResponse(ResponseMessages.FAMILY_NOT_FOUND.getCode(),//
	        		ResponseMessages.FAMILY_NOT_FOUND.getMessage());
	    }
	}
	
	/**
	 * 查詢所有家族群組
	 * @return 家族清單
	 */
	public BasicResponse listAllFamily() {
	    List<Family> familyList = familyDao.selectAll();
	    return new BasicResponse(ResponseMessages.SUCCESS.getCode(),//
        		ResponseMessages.SUCCESS.getMessage());
	}
}
