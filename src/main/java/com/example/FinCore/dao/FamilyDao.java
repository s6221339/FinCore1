package com.example.FinCore.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.FinCore.entity.Family;

import jakarta.transaction.Transactional;

@Repository
public interface FamilyDao extends JpaRepository<Family, Integer> {

	/**
	 * 新增家族群組
	 * @param owner      擁有者帳號
	 * @param invitor    邀請人帳號
	 * @param createDate 建立日期
	 */
	@Modifying
	@Transactional
	@Query(value = "insert into family (name, owner, invitor, createDate) "//
			+ " values (:name, :owner, :invitor, :createDate)", nativeQuery = true)
	public void create(//
			@Param("name")String name,
			@Param("owner") String owner, //
			@Param("invitor") String invitor, //
			@Param("createDate") LocalDate createDate);

	/**
	 * 取得目前資料庫中最大的家族群組編號（流水號）
	 * @return 最大家族群組編號
	 */
	@Query(value = "select max(id) from family", nativeQuery = true)
	public int selectMaxId();

	/**
	 * 取得所有家族群組資料
	 * @return 家族群組清單
	 */
	@Query(value = "select * from family", nativeQuery = true)
	public List<Family> selectAll();

	/**
	 * 更新家族群組資料
	 * @param id 家族群組編號
	 * @param owner 新的擁有者帳號
	 * @param invitor 新的邀請人帳號
	 * @param createDate 新的建立日期
	 * @return 影響的資料筆數（通常為1）
	 */
	@Modifying
	@Transactional
	@Query(value = "update family set name = :name, owner = :owner," //
			+ " invitor = :invitor, createDate = :createDate" //
			+ " where id = :id", nativeQuery = true)
	public int update(@Param("id") int id,@Param("name")String name, @Param("owner") String owner, @Param("invitor") String invitor //
			, @Param("createDate") LocalDate createDate);

	/**
	 * 刪除家族群組
	 * @param id 家族群組編號
	 */
	@Modifying
	@Transactional
	@Query(value = "delete from family where id = :id", nativeQuery = true)
	public void delete(@Param("id") int id);

	/**
	 * 根據多個編號批次刪除家族群組
	 * @param idList 家族群組編號清單
	 */
	@Modifying
	@Transactional
	@Query(value = "delete from family where id in (?1)", nativeQuery = true)
	public void deleteByIdIn(List<Integer> idList);

	/**
	 * 根據編號查詢單一家族群組
	 * @param id 家族群組編號
	 * @return 家族群組物件
	 */
	@Query(value = "select * from family where id = ?1", nativeQuery = true)
	public Family selectById(int id);

	/**
	 * 根據多個編號查詢多筆家族群組
	 * @param idList 家族群組編號清單
	 * @return 家族群組物件清單
	 */
	@Query(value = "select * from family where id in (?1)", nativeQuery = true)
	public List<Family> selectByIdIn(List<Integer> idList);

	/**
	 * 依據擁有者帳號查詢所有家族群組
	 * @param owner 擁有者帳號
	 * @return 擁有者建立的所有家族群組
	 */
	@Query(value = "select * from family where owner = :owner", nativeQuery = true)
	public List<Family> findByOwner(@Param("owner") String owner);
	
	/**
	 * 查詢會員在哪個家庭群組
	 * @param account
	 * @return
	 */
	@Query(value = "select * from family", nativeQuery = true)
	public List<Family> selectAllFamily();

}