package com.example.FinCore.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.FinCore.entity.Savings;
import com.example.FinCore.entity.SavingsPK;

import jakarta.transaction.Transactional;

@Repository
public interface SavingsDao extends JpaRepository<Savings, SavingsPK>
{
	
	/**
	 * 新增一筆自定義儲蓄資料
	 * @param balanceId 要依附的帳戶編號
	 * @param year 哪一年的儲蓄設定
	 * @param month 哪一個月的儲蓄設定
	 * @param amount 金額
	 */
	@Transactional
	@Modifying
	@Query(value = "insert into savings (balance_id, year, month, amount) "
			+ "values (?1, ?2, ?3, ?4)", nativeQuery = true)
	public void create(int balanceId, int year, int month, int amount);
	
	/**
	 * 更新儲蓄金額設定
	 * @param balanceId 指定的帳戶編號
	 * @param year 指定年
	 * @param month 指定月
	 * @param amount 更新的金額
	 */
	@Transactional
	@Modifying
	@Query(value = "update savings set amount = :amount "
			+ "where balance_id = :balanceId and year = :year and month = :month", nativeQuery = true)
	public void update(
			@Param("balanceId") int balanceId, 
			@Param("year") int year, 
			@Param("month") int month, 
			@Param("amount") int amount
			);
	
	/**
	 * 刪除指定帳戶下的所有餘額設定。
	 * @param balanceId 指定帳戶編號
	 */
	@Transactional
	@Modifying
	@Query(value = "delete from savings where balance_id in (?1)", nativeQuery = true)
	public void deleteByBalanceIdList(List<Integer> balanceIdList);
	
	/**
	 * 取得指定帳戶在某年月的儲蓄設定數值。
	 * @param balanceId 指定帳戶編號
	 * @param year 指定年
	 * @param month 指定月
	 * @return 該帳戶在該年月的儲蓄設定數值
	 */
	@Query(value = "select amount from savings where balance_id = ?1 and year = ?2 and month = ?3", nativeQuery = true)
	public int getSavingsAmount(int balanceId, int year, int month);
	
	/**
	 * 取得一系列帳戶在某年月的儲蓄設定數值列表。
	 * @param idList 帳戶編號列表
	 * @param year 指定年
	 * @param month 指定月
	 * @return 儲蓄設定數值列表，和帳戶編號列表為單映射關聯
	 */
	@Query(value = "select * from savings where balance_id in (?1) and year = ?2 and month = ?3", nativeQuery = true)
	public List<Savings> getSavingsListByBalanceIdList(List<Integer> idList, int year, int month);
	
}
