package com.example.FinCore.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.FinCore.entity.FamilyInvitation;
import com.example.FinCore.entity.FamilyInvitationPK;

import jakarta.transaction.Transactional;

/**
 * FamilyInvitation 資料庫存取介面
 *
 */
public interface FamilyInvitationDao extends JpaRepository<FamilyInvitation, FamilyInvitationPK> {

    /**
     * 根據 family_id 查詢所有邀請
     * 
     * @param familyId 家族ID
     * @return 該家族所有邀請紀錄
     */
    @Query(value = "select * from family_invitation where family_id = :familyId", nativeQuery = true)
    List<FamilyInvitation> findByFamilyId(@Param("familyId") int familyId);

    /**
     * 根據 account 及 family_id 查詢特定邀請
     * 
     * @param account 帳號
     * @param familyId 家族ID
     * @return 對應邀請紀錄
     */
    @Query(value = "select * from family_invitation where account = :account and family_id = :familyId", nativeQuery = true)
    FamilyInvitation findByAccountAndFamilyId(@Param("account") String account, @Param("familyId") int familyId);

    /**
     * 刪除邀請
     * 
     * @param account 帳號
     * @param familyId 家族ID
     */
    @Modifying
    @Transactional
    @Query(value = "delete from family_invitation where account = :account and family_id = :familyId", nativeQuery = true)
    void deleteByAccountAndFamilyId(@Param("account") String account, @Param("familyId") int familyId);
}