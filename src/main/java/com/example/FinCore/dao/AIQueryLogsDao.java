package com.example.FinCore.dao;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.FinCore.entity.AIQueryLogs;
import com.example.FinCore.entity.AIQueryLogsPK;

import jakarta.transaction.Transactional;

@Repository
public interface AIQueryLogsDao extends JpaRepository<AIQueryLogs, AIQueryLogsPK> 
{
	
	@Transactional
	@Modifying
	@Query(value = "insert into ai_query_logs (account, response_text, create_date, year, month) "
			+ "values (:account, :responseText, :createDate, :year, :month)", nativeQuery = true)
	public void createLog(
			@Param("account") 	String account,
			@Param("responseText") 	String responseText,
			@Param("createDate")	LocalDate createDate,
			@Param("year") 			int year,
			@Param("month") 		int month
			);
	
	@Query(value = "select COUNT(*) from ai_query_logs where account = ?1 and year = ?2 and month = ?3", nativeQuery = true)
	public int existsByParam(String account, int year, int month);
	
}
