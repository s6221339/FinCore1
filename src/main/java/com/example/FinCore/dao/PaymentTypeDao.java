package com.example.FinCore.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.FinCore.entity.PaymentType;
import com.example.FinCore.entity.PaymentTypePK;

import jakarta.transaction.Transactional;

@Repository
public interface PaymentTypeDao extends JpaRepository<PaymentType, PaymentTypePK> 
{
	
	/**
	 * 新增一個款項種類。
	 * @param type 種類
	 * @param item 細項
	 * @param account 新增者
	 */
	@Transactional
	@Modifying
	@Query(value = "insert into payment_type (type, item, account) "
			+ "values (?1, ?2, ?3)", nativeQuery = true)
	public void create(String type, String item, String account);
	
	/**
	 * 取得指定帳號創立的所有類別。
	 * @return 指定帳號創立的所有類別
	 */
	@Query(value = "select * from payment_type where account = ?1", nativeQuery = true)
	public List<PaymentType> getTypeByAccount(String account);
	
	/**
	 * 刪除指定類別。
	 * @param type 類別
	 * @param item 細項
	 * @param account 新增者
	 */
	@Transactional
	@Modifying
	@Query(value = "delete from payment_type where type = ?1 and item = ?2 and account = ?3", nativeQuery = true)
	public void delete(String type, String item, String account);
	
}
