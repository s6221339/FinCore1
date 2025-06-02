package com.example.FinCore.Dao;

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
	 * @param owner
	 * @param invitor
	 * @param createDate
	 */
	@Modifying
	@Transactional
	@Query(value = "insert into family (owner, invitor, createDate) "//
			+ " values (:owner, :invitor, :createDate)", nativeQuery = true)
	public void create(//
			@Param("owner") String owner, //
			@Param("invitor") String invitor, //
			@Param("createDate") LocalDate createDate);
	
	/**
	 * 取得家族群組最新流水編號
	 * @return
	 */
	@Query(value = "select max(id) from family", nativeQuery = true)
	public int selectMaxId();
	
	/**
	 * 取得所有家族群組資料
	 * @return
	 */
	@Query(value = "select * from family", nativeQuery = true)
	public List<Family> selectAll();
	
	/**
	 * 更新家族群組資料
	 * @param id
	 * @param owner
	 * @param invitor
	 * @param createDate
	 * @return
	 */
	@Modifying
	@Transactional
	@Query(value = "update family set owner = :owner, invitor = :invitor, createDate = :createDate"
			+ " where id = :id", nativeQuery = true)
	public int update(@Param("id") int id, @Param("owner") String owner, @Param("invitor") String invitor //
			, @Param("createDate") LocalDate createDate);
	
	/**
	 * 刪除家族群組
	 * @param id
	 */
	@Modifying
	@Transactional
	@Query(value = "delete from family where id = :id", nativeQuery = true)
	void delete(@Param("id") int id);
	
	/**
	 * 刪除多個家族群組
	 * @param idList
	 */
	@Modifying
	@Transactional
	@Query(value = "delete from family where id in (?1)", nativeQuery = true)
	public void deleteByIdIn(List<Integer> idList);
	
	/**
	 * 搜尋指定家族群組
	 * @param id
	 * @return
	 */
	@Query(value = "select * from family where id = ?1", nativeQuery = true)
	public Family selectById(int id);
	
	/**
	 * 搜尋多個指定家族群組
	 * @param idList
	 * @return
	 */
	@Query(value = "select * from family where id in (?1)", nativeQuery = true)
	public List<Family> selectByIdIn(List<Integer> idList);
	
}
