package com.example.FinCore.dao;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.FinCore.entity.AIQueryLogs;

import jakarta.transaction.Transactional;

@Repository
public interface AIQueryLogsDao extends JpaRepository<AIQueryLogs, Integer> 
{
	
	@Transactional
	@Modifying
	@Query(value = "insert into ai_query_logs (balance_id, query_text, response_text, create_date, year, month) "
			+ "values (:balanceId, :queryText, :responseText, :createDate, :year, :month)", nativeQuery = true)
	public void createLog(
			@Param("balanceId") 	int balanceId,
			@Param("queryText") 	String queryText,
			@Param("responseText") 	String responseText,
			@Param("createDate")	LocalDate createDate,
			@Param("year") 			int year,
			@Param("month") 		int month
			);
	
}
