package com.example.FinCore.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * FamilyInvitation 控制器
 * <p>
 * 提供家族邀請相關的 REST API
 */
@RestController
@RequestMapping(value = "finbook/familyInvitation/")
public class FamilyInvitationController {

//    @Autowired
//    private FamilyInvitationService familyInvitationService;

    /**
     * 新增家族邀請
     * @param request 前端傳入的邀請請求 DTO
     * @return 新增成功的 FamilyInvitationResponse
     */
//    @PostMapping("createInvitation")
//    public FamilyInvitationResponse createInvitation(@RequestBody FamilyInvitationRequest req) {
//        // 呼叫 Service 新增邀請
//        return familyInvitationService.createInvitation(req);
//    }

    /**
     * 查詢指定家族的所有邀請
     * @param familyId 家族ID
     * @return 邀請清單（List of FamilyInvitationResponse）
     */
//    @PostMapping("getInvitationsByFamilyId")
//    public List<FamilyInvitationResponse> getInvitationsByFamilyId(@PathVariable int familyId) {
//        // 取得家族所有邀請
//        return familyInvitationService.getInvitationsByFamilyId(familyId);
//    }

    /**
     * 查詢單一邀請
     * @param account 帳號
     * @param familyId 家族ID
     * @return 該邀請 Response DTO
     */
//    @PostMapping("getInvitation")
//    public FamilyInvitationResponse getInvitation(@PathVariable String account, @PathVariable int familyId) {
//        // 查詢特定邀請
//        return familyInvitationService.getInvitation(account, familyId);
//    }

    /**
     * 刪除邀請
     * @param account 帳號
     * @param familyId 家族ID
     */
//    @PostMapping("deleteInvitation")
//    public void deleteInvitation(@PathVariable String account, @PathVariable int familyId) {
//        // 刪除特定邀請
//        familyInvitationService.deleteInvitation(account, familyId);
//    }
}
