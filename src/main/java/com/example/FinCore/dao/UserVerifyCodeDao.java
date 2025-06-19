package com.example.FinCore.dao;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.FinCore.entity.UserVerifyCode;

import jakarta.transaction.Transactional;

@Repository
public interface UserVerifyCodeDao extends JpaRepository<UserVerifyCode, String>{
	
	/**
	 * 根據編號查詢單一會員
	 * 
	 * @param account 會員帳號(Email)
	 * @return
	 */
	@Query(value = "select * from user_verify_code where account = ?1", nativeQuery = true)
	public UserVerifyCode selectById(String account);

	/**
     * 新增或更新驗證碼
     * 
     * 若帳號已存在則更新 code 與 expire_at，否則新增一筆資料。
     * @param code 驗證碼
     * @param expireAt 過期時間
     * @param account 會員帳號
     */
    @Modifying
    @Transactional
    @Query(value = "insert into user_verify_code (account, code, expire_at) values (?3, ?1, ?2) " +
            "on duplicate key update code = values(code), expire_at = values(expire_at)", nativeQuery = true)
    public void insertOrUpdateVerified(String code, LocalDateTime expireAt, String account);
	 
	/**
	 * 刪除驗證碼資料
	 */
	@Modifying
	@Transactional
	@Query(value = "delete	from user_verify_code where account = ?1", nativeQuery = true)
	public void deleteByAccount(String account);
}
