package com.example.FinCore.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.FinCore.entity.Transfers;

import jakarta.transaction.Transactional;

@Repository
public interface TransfersDao extends JpaRepository<Transfers, Integer> 
{
	
	/**
	 * 建立一筆轉帳紀錄。
	 * @param fromBalance 從哪個帳戶轉出（編號）
	 * @param toBalance 轉進哪個帳戶（編號）
	 * @param amount 轉帳金額
	 * @param description 轉帳說明
	 * @param createDate 記錄創建日期
	 * @param year 記錄創建年
	 * @param month 記錄創建月
	 */
	@Transactional
	@Modifying
	@Query(value = "insert into transfers (from_balance, to_balance, amount, description, create_date, year, month) "
			+ "values (:fromBalance, :toBalance, :amount, :description, :createDate, :year, :month)", nativeQuery = true)
	public void create(
			@Param("fromBalance") 	int fromBalance,
			@Param("toBalance") 	int toBalance,
			@Param("amount") 		int amount,
			@Param("description") 	String description,
			@Param("createDate") 	LocalDate createDate,
			@Param("year") 			int year,
			@Param("month") 		int month
			);
	
	/**
	 * 指定記錄編號刪除資料
	 * @param id 記錄編號
	 */
	@Transactional
	@Modifying
	@Query(value = "delete from transfers where id = ?1", nativeQuery = true)
	public void deleteById(int id);
	
	/**
	 * 指定轉出帳戶與匯入帳戶，刪除與它們同時關聯的所有紀錄。
	 * @param from 轉出帳戶
	 * @param to 匯入帳戶
	 */
	@Transactional
	@Modifying
	@Query(value = "delete from transfers where from_balance = ?1 or from_balance = ?2 and to_balance = ?1 or to_balance = ?2", nativeQuery = true)
	public void deleteByBalanceId(int from, int to);
	
	/**
	 * 指定帳戶編號，取得與其相關的所有轉帳紀錄資料。
	 * @param balanceId 帳戶編號
	 * @return 與該帳戶相關的所有轉帳紀錄資料
	 */
	@Query(value = "select * from transfers where from_balance = ?1 or to_balance = ?1", nativeQuery = true)
	public List<Transfers> getAllTransfersByBalanceId(int balanceId);
	
}
