package com.example.FinCore.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.FinCore.entity.User;

import jakarta.transaction.Transactional;

@Repository
public interface UserDao extends JpaRepository<User, String>{
	
	/**
	 * 新增會員
	 * @param account 會員帳號(Email)
	 * @param name 會員名稱
	 * @param password 密碼
	 * @param phone 手機號碼
	 * @param avatarUrl 頭像
	 * @param superAdmin 是否為超級管理員
	 * @param createDate 建立日期
	 */
	@Modifying
	@Transactional
	@Query(value = "insert into user "
            + "(account, name, password, phone, avatar, super_admin, create_date) "
            + "values (:account, :name, :password, :phone, :avatar, :superAdmin, :createDate)",
            nativeQuery = true)
	public void create(//
			@Param("account") String account, //
			@Param("name") String name, //
			@Param("password") String password, //
			@Param("phone") String phone, //
			@Param("avatar") byte[] avatar, //
			@Param("superAdmin") Boolean superAdmin, //
			@Param("createDate") LocalDate createDate //
			);
	
	/**
	 * 取得所有會員資料
	 * @return 會員清單
	 */
	@Query(value = "select * from user", nativeQuery = true)
	public List<User> selectAll();

	/**
	 * 更新會員資料
	 * @param account 會員帳號(Email)
	 * @param name 會員名稱
	 * @param password 密碼
	 * @param phone 手機號碼
	 * @param avatarUrl 頭像
	 * @param superAdmin 是否為超級管理員
	 * @param createDate 建立日期
	 * @return
	 */
	@Modifying
	@Transactional
	 @Query(value = "update user set name = :name, password = :password, phone = :phone, avatar = :avatar,"
	            + " super_admin = :superAdmin, create_date = :createDate where account = :account", nativeQuery = true)
	public int update(@Param("account") String account,
			@Param("name") String name,
			@Param("password") String password,
			@Param("phone") String phone,
			@Param("avatar") byte[] avatar,
			@Param("superAdmin") Boolean superAdmin,
			@Param("createDate") LocalDate createDate);
	
	/**
	 * 刪除會員
	 * @param account會員帳號(Email)
	 */
	@Modifying
	@Transactional
	@Query(value = "delete from user where account = :account", nativeQuery = true)
	public void delete(@Param("account") String account);
	
	/**
	 * 刪除多個會員
	 * @param account會員帳號(Email)
	 */
	@Modifying
	@Transactional
	@Query(value = "delete from user where account in (:account)", nativeQuery = true)
	public void deleteByAccountIn(@Param("account") List<String> account);
	
	/**
	 * 根據編號查詢單一會員
	 * @param account 會員帳號(Email)
	 * @return
	 */
	@Query(value = "select * from user where account = ?1", nativeQuery = true)
	public User selectById(String account);
	
	/**
	 * 根據多個會員帳號查詢多筆會員
	 * @param AccountList 會員帳號(Email)
	 * @return 會員清單
	 */
	@Query(value = "select * from user where account in (?1)", nativeQuery = true)
	public List<User> selectByAccountIn(List<String> accountList);
	
	/**
	 * 取得帳號在資料庫的資料筆數
	 * @param account 會員帳號(Email)
	 * @return 返回值只有1跟0
	 */
	@Query(value = "select count(account) from user where account = ?1 ", nativeQuery = true)
	public int selectCountByAccount(String account);
	
	

}
