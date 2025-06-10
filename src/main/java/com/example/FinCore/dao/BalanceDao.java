package com.example.FinCore.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.FinCore.entity.Balance;

import jakarta.transaction.Transactional;

@Repository
public interface BalanceDao extends JpaRepository<Balance, Integer>
{
	
	/**
	 * 用帳號創建一個帳戶資料，代表該帳戶將綁定在一個帳號之下。
	 * @param account 帳號
	 * @param name 帳戶名稱
	 * @param createDate 創建日期
	 */
	@Transactional
	@Modifying
	@Query(value = "insert into balance (family_id, account, name, create_date) "
			+ "values (0, ?1, ?2, ?3)", nativeQuery = true)
	public void createByAccount(String account, String name, LocalDate createDate);
	
	/**
	 * 用家庭編號創建一個帳戶，代表該帳戶將綁定在一個群組之下。
	 * @param familyId 家庭編號
	 * @param name 帳戶名稱
	 * @param createDate 創建日期
	 */
	@Transactional
	@Modifying
	@Query(value = "insert into balance (family_id, account, name, create_date) "
			+ "values (?1, NULL, ?2, ?3)", nativeQuery = true)
	public void createByFamliyId(int familyId, String name, LocalDate createDate);
	
	/**
	 * 更新帳戶名稱與儲蓄金額。
	 * @param balanceId 指定帳戶編號
	 * @param name 要更新的名稱
	 * @param savings 儲蓄金額
	 */
	@Transactional
	@Modifying
	@Query(value = "update balance set name = ?2 where balance_id = ?1", nativeQuery = true)
	public void updateName(int balanceId, String name);
	
	/**
	 * 刪除指定帳戶。
	 * @param balanceId 帳戶編號
	 */
	@Transactional
	@Modifying
	@Query(value = "delete from balance where balance_id = ?1", nativeQuery = true)
	public void deleteByBalanceId(int balanceId);
	
	/**
	 * 取得與指定帳號關聯的所有帳戶編號。
	 * @param account 帳號
	 * @return 帳戶編號列表
	 */
	@Query(value = "select balance_id from balance where account = ?1", nativeQuery = true)
	public List<Integer> selectBalanceIdListByAccount(String account);
	
	/**
	 * 取得資料表中當前最新一筆資料編號。
	 * @return 最新的資料編號
	 */
	@Query(value = "select max(balance_id) from balance", nativeQuery = true)
	public int getLastedId();

}
