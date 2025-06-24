package com.example.FinCore.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.FinCore.dao.FamilyInvitationDao;
import com.example.FinCore.entity.FamilyInvitation;
import com.example.FinCore.entity.FamilyInvitationPK;
import com.example.FinCore.service.itfc.FamilyInvitationService;
import com.example.FinCore.vo.request.FamilyInvitationRequest;
import com.example.FinCore.vo.response.FamilyInvitationResponse;

@Service
public class FamilyInvitationServiceImpl implements FamilyInvitationService {

    @Autowired
    private FamilyInvitationDao familyInvitationDao;
    

    /**
     * 新增家族邀請
     */
    @Override
    public FamilyInvitationResponse createInvitation(FamilyInvitationRequest request) {
        // 1. 產生複合主鍵物件
        FamilyInvitationPK pk = new FamilyInvitationPK(request.getAccount(), request.getFamilyId());
        
        // 2. 判斷此邀請是否已存在
        if (familyInvitationDao.existsById(pk)) {
            // 已存在邀請：直接回傳 null 或你可以回傳一個特殊的 Response（也可自訂錯誤訊息）
            return null;
        }
        
        // 3. 不存在才新增
        FamilyInvitation entity = new FamilyInvitation();
        entity.setAccount(request.getAccount());
        entity.setFamilyId(request.getFamilyId());
        entity.setStatus(request.isStatus());
        
        FamilyInvitation saved = familyInvitationDao.save(entity);
        return toResponse(saved);
    }

    /**
     * 取得特定家族所有邀請
     */
    @Override
    public List<FamilyInvitationResponse> getInvitationsByFamilyId(int familyId) {
        List<FamilyInvitation> invitations = familyInvitationDao.findByFamilyId(familyId);
        // Entity List 轉 Response DTO List
        return invitations.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * 查詢單一邀請
     */
    @Override
    public FamilyInvitationResponse getInvitation(String account, int familyId) {
        FamilyInvitation invitation = familyInvitationDao.findByAccountAndFamilyId(account, familyId);
        if (invitation == null) {
            return null;
        }
        return toResponse(invitation);
    }

    /**
     * 刪除邀請
     */
    @Override
    public void deleteInvitation(String account, int familyId) {
        familyInvitationDao.deleteByAccountAndFamilyId(account, familyId);
    }

    /**
     * Entity 轉 Response DTO 的私有工具方法
     */
    private FamilyInvitationResponse toResponse(FamilyInvitation entity) {
        FamilyInvitationResponse resp = new FamilyInvitationResponse();
        resp.setAccount(entity.getAccount());
        resp.setFamilyId(entity.getFamilyId());
        resp.setStatus(entity.isStatus());
        return resp;
    }
    
    @Override
    public boolean existsInvitation(String account, int familyId) {
        FamilyInvitationPK pk = new FamilyInvitationPK(account, familyId);
        return familyInvitationDao.existsById(pk);
    }

}
