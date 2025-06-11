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
	 * 註冊會員
	 * @param account 會員帳號(Email)
	 * @param name 會員名稱
	 * @param password 密碼
	 * @param phone 手機號碼
	 * @param createDate 建立日期
	 */
	@Modifying
	@Transactional
	@Query(value = "insert into user "
            + "(account, name, password, phone, super_admin, create_date) "
            + "values (:account, :name, :password, :phone, 0, :createDate)",
            nativeQuery = true)
	public void register(//
			@Param("account") String account, //
			@Param("name") String name, //
			@Param("password") String password, //
			@Param("phone") String phone, //
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
	 * @param avatar 頭像
	 * @param superAdmin 是否為超級管理員
	 * @param createDate 建立日期
	 * @return
	 */
	@Modifying
	@Transactional
	 @Query(value = "update user set name = :name, phone = :phone,"
	 		+ " avatar = :avatar, birthday = :birthday where account = :account", nativeQuery = true)
	public int update(@Param("account") String account,
			@Param("name") String name,
			@Param("phone") String phone,
			@Param("birthday") LocalDate birthday,
			@Param("avatar") byte[] avatar);
	
	/**
	 * 註銷會員
	 * @param account會員帳號(Email)
	 */
	@Modifying
	@Transactional
	@Query(value = "delete from user where account = :account", nativeQuery = true)
	public void cancel(@Param("account") String account);
	
	/**
	 * 註銷多個會員
	 * @param account會員帳號(Email)
	 */
	@Modifying
	@Transactional
	@Query(value = "delete from user where account in (:account)", nativeQuery = true)
	public void cancelByAccountIn(@Param("account") List<String> account);
	
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
     * 查詢指定帳號在資料庫中的筆數
     * @param account 會員帳號(Email)
     * @return 筆數（只有0或1）
     */
	@Query(value = "select count(account) from user where account = ?1 ", nativeQuery = true)
	public int selectCountByAccount(String account);
	
	/**
     * 查詢多個帳號在資料庫中的筆數
     * @param accounts 會員帳號(Email)清單
     * @return 資料庫中存在的帳號數量
     */
	@Query(value = "select count(account) from user where account in (:accounts)", nativeQuery = true)
	public int countByAccountIn(@Param("accounts") List<String> accounts);
	
	/**
     * 更新密碼
     * @param account 會員帳號(Email)
     * @param password 新密碼（加密後）
     * @return 更新筆數（1 = 成功, 0 = 失敗）
     */
    @Modifying
    @Transactional
    @Query(value = "update user set password = :password where account = :account", nativeQuery = true)
    public int updatePassword(@Param("account") String account, @Param("password") String password);
	
	

}
