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
    
    /**
     * 查詢指定 family_id 下，所有尚未接受邀請（status 為 false，邀請中狀態）的家族邀請資料。
     *
     * @param familyId 家族群組 ID
     * @return 所有邀請中（status=false）的 FamilyInvitation 列表
     */
    @Query(value = "select * from family_invitation where family_id = :familyId and status = false", nativeQuery = true)
    List<FamilyInvitation> findByFamilyIdAndStatusFalse(@Param("familyId") int familyId);
    
    /**
     * 刪除指定家族群組的所有邀請紀錄
     *
     * @param familyId 家族群組ID
     */
    @Modifying
    @jakarta.transaction.Transactional
    @Query(value = "delete from family_invitation where family_id = :familyId", nativeQuery = true)
    void deleteAllByFamilyId(@Param("familyId") int familyId);
    
    /**
     * 刪除某個家庭群組內，指定多個帳號的邀請紀錄
     * @param familyId 家庭ID
     * @param accounts 受邀人帳號清單
     */
    @Modifying
    @jakarta.transaction.Transactional
    @Query(value = "delete from family_invitation where family_id = :familyId and account in :accounts", nativeQuery = true)
    void deleteByFamilyIdAndAccounts(@Param("familyId") int familyId, @Param("accounts") List<String> accounts);
    
    /**
     * 根據 account 查詢收到的所有邀請
     * @param account 受邀帳號
     * @return 邀請列表
     */
    @Query(value = "select * from family_invitation where account = :account", nativeQuery = true)
    List<FamilyInvitation> findByAccount(@Param("account") String account);
}