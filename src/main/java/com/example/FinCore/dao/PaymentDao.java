package com.example.FinCore.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.FinCore.entity.Payment;

import jakarta.transaction.Transactional;

@Repository
public interface PaymentDao extends JpaRepository<Payment, Integer>
{
	
	/**
	 * 建立一筆款項。
	 * @param balanceId 所屬帳戶編號
	 * @param description 款項描述
	 * @param type 款項類型
	 * @param item 類型細項
	 * @param amount 金額
	 * @param recurringPeriodYear 循環週期（年）
	 * @param recurringPeriodMonth　循環週期（月）
	 * @param recurringPeriodDay 循環週期（日）
	 * @param createDate 款項建立日期
	 * @param recordDate 款項記錄日期
	 * @param year 年，查詢用
	 * @param month 月，查詢用
	 * @param day 日，查詢用
	 */
	@Transactional
	@Modifying
	@Query(value = "insert into payment (balance_id, description, type, item, amount, recurring_period_year, recurring_period_month, recurring_period_day, create_date, record_date, year, month, day) "
			+ "values (:balanceId, :description, :type, :item, :amount, :recurringPeriodYear, :recurringPeriodMonth, :recurringPeriodDay, :createDate, :recordDate, :year, :month, :day)", nativeQuery = true)
	public void create(
			@Param("balanceId") 			int balanceId, 
			@Param("description") 			String description, 
			@Param("type") 					String type, 
			@Param("item") 					String item, 
			@Param("amount") 				int amount, 
			@Param("recurringPeriodYear") 	int recurringPeriodYear, 
			@Param("recurringPeriodMonth") 	int recurringPeriodMonth, 
			@Param("recurringPeriodDay") 	int recurringPeriodDay, 
			@Param("createDate") 			LocalDate createDate, 
			@Param("recordDate") 			LocalDate recordDate, 
			@Param("year") 					int year, 
			@Param("month") 				int month, 
			@Param("day") 					int day
			);
	
	/**
	 * 更新指定款項資料。如果敘述為 {@code NULL} 時不更新；對於類型與細項永遠更新；
	 * 更新金額與循環週期如果少於 0 時不更新。
	 * @param paymentId 指定款項編號
	 * @param description 敘述
	 * @param type 類型
	 * @param item 細項
	 * @param amount 金額
	 * @param recurringPeriodYear 循環週期（年）
	 * @param recurringPeriodMonth 循環週期（月）
	 * @param recurringPeriodDay 循環週期（日）
	 * @param recordDate 記帳日期
	 * @param year 記帳年，查詢用
	 * @param month 記帳月，查詢用
	 * @param day 記帳日，查詢用
	 */
	@Transactional
	@Modifying
	@Query(value = "update payment set "
			+ "description = case when :description is null then description else :description end, "
			+ "type = :type, "
			+ "item = :item, "
			+ "amount = case when :amount < 0 then amount else :amount end, "
			+ "recurring_period_year = case when :recurringPeriodYear < 0 then recurring_period_year else :recurringPeriodYear end, "
			+ "recurring_period_month = case when :recurringPeriodMonth < 0 then recurring_period_month else :recurringPeriodMonth end, "
			+ "recurring_period_day = case when :recurringPeriodDay < 0 then recurring_period_day else :recurringPeriodDay end, "
			+ "record_date = :recordDate, "
			+ "year = :year, "
			+ "month = :month, "
			+ "day = :day "
			+ "where payment_id = :paymentId", nativeQuery = true)
	public void update(
			@Param("paymentId") 			int paymentId, 
			@Param("description") 			String description, 
			@Param("type") 					String type, 
			@Param("item") 					String item, 
			@Param("amount") 				int amount, 
			@Param("recurringPeriodYear") 	int recurringPeriodYear, 
			@Param("recurringPeriodMonth") 	int recurringPeriodMonth, 
			@Param("recurringPeriodDay") 	int recurringPeriodDay,
			@Param("recordDate") LocalDate recordDate,
			@Param("year") int year,
			@Param("month") int month,
			@Param("day") int day
			);
	
	@Transactional
	@Modifying
	@Query(value = "update payment set delete_date = ?2 where payment_id = ?1", nativeQuery = true)
	public void updateDeleteDate(int paymentId, LocalDate deleteDate);
	
	/**
	 * 刪除指定款項。
	 * @param paymentId 款項編號
	 */
	@Transactional
	@Modifying
	@Query(value = "delete from payment where payment_id = ?1", nativeQuery = true)
	public void deleteByPaymentId(int paymentId);
	
	/**
	 * 透過比對刪除日期，刪除該日的所有款項。
	 * @param date 指定日期，會與資料庫的「delete_date」做比對
	 */
	@Transactional
	@Modifying
	@Query(value = "delete from payment where delete_date = ?1", nativeQuery = true)
	public void deleteByMatchingDeleteDate(int date);
	
	/**
	 * 在特定帳戶下，取得指定年的所有款項紀錄。
	 * @param balanceIdList 帳戶列表
	 * @param year 年
	 * @return 符合條件的所有款項紀錄
	 */
	@Query(value = "select * from payment where balance_id in (?1) and year = ?2", nativeQuery = true)
	public List<Payment> getPaymentListByYear(List<Integer> balanceIdList, int year);
	
	/**
	 * 在特定帳戶下，取得指定年、月的所有款項紀錄。
	 * @param balanceIdList 帳戶列表
	 * @param year 年
	 * @param month 月
	 * @return 符合條件的所有款項紀錄
	 */
	@Query(value = "select * from payment where balance_id in (?1) year = ?2 and month = ?3", nativeQuery = true)
	public List<Payment> getPaymentListByYearAndMonth(List<Integer> balanceIdList, int year, int month);
	
	/**
	 * 在特定帳戶下，取得指定年、月、日的所有款項紀錄。
	 * @param balanceIdList 帳戶列表
	 * @param year 年
	 * @param month 月
	 * @param day 日
	 * @return 符合條件的所有款項紀錄
	 */
	@Query(value = "select * from payment where balance_id in (:idList) year = :year and month = :month and day = :day", nativeQuery = true)
	public List<Payment> getPaymentListByYearAndMonthAndDay(
			@Param("idList") 	List<Integer> balanceIdList, 
			@Param("year") 		int year, 
			@Param("month") 	int month, 
			@Param("day") 		int day
			);
	
	/**
	 * 取得指定款項的刪除日期。
	 * @param paymentId 款項編號
	 * @return 該款項的刪除日期
	 */
	@Query(value = "select delete_date from payment where payment_id = ?1", nativeQuery = true)
	public LocalDate getDeleteDate(int paymentId);
	
	/**
	 * 取得指定款項的完整資料。
	 * @param paymentId 款項編號
	 * @return 實體
	 */
	@Query(value = "select * from payment where payment_id = ?1", nativeQuery = true)
	public Payment getEntity(int paymentId);
	
	/**
	 * 使用一組帳戶編號取得所有相關的款項資料
	 * @param balanceIdList 帳戶編號列表
	 * @return 與該帳戶編號相關的所有款項資料
	 */
	@Query(value = "select * from payment where balance_id in (?1)", nativeQuery = true)
	public List<Payment> getPaymentListByBalanceIdList(List<Integer> balanceIdList);
	
	/**
	 * 使用一組帳戶編號取得所有相關的款項編號
	 * @param balanceIdList 帳戶編號列表
	 * @return 與該帳戶編號相關的所有款項編號
	 * @see PaymentDao#getPaymentListByBalanceIdList(List)
	 */
	@Query(value = "select payment_id from payment where balance_id in (?1)", nativeQuery = true)
	public List<Integer> getPaymentIdListByBalanceIdList(List<Integer> balanceIdList);
	
	/**
	 * 使用一組款項編號刪除款項資料。<br>
	 * ⚠️：與標記刪除不同，該方法會永久刪除資料
	 * @param paymentIdList 款項編號列表
	 */
	@Query(value = "delete from payment where payment_id in (?1)", nativeQuery = true)
	public void deletePaymentByPaymentId(List<Integer> paymentIdList);
	
	/**
	 * 取得該帳款在哪一個帳戶編號。
	 * @param paymentId 帳款編號
	 * @return 依附的帳戶編號
	 */
	@Query(value = "select balance_id from payment where payment_id = ?1", nativeQuery = true)
	public int getBalanceId(int paymentId);
	
	/**
	 * 將款項的刪除標記取消。
	 * @param paymentIdList 要解除刪除標記的款項編號列表
	 */
	@Transactional
	@Modifying
	@Query(value = "update payment set delete_date = null where payment_id in (?1)", nativeQuery = true)
	public void undoDeleted(List<Integer> paymentIdList);
	
	@Query(value = "select MAX(payment_id) from payment", nativeQuery = true)
	public int getLastedId();
	
}
