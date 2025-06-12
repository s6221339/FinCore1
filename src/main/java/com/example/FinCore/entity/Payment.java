package com.example.FinCore.entity;

import java.time.LocalDate;

import org.springframework.util.Assert;

import com.example.FinCore.vo.RecurringPeriodVO;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;

/**
 * 款項，每個款項都依附在一個帳號之下
 */
@Table(name = "payment")
@Entity
public class Payment 
{
	
	@Id
	@Column(name = "payment_id")
	private int paymentId;
	
	@Column(name = "balance_id")
	private int balanceId;
	
	@Column(name = "description")
	private String description;
	
	/** 款項類型 */
	@Column(name = "type")
	private String type;
	
	/** 分類細項 */
	@Column(name = "item")
	private String item;
	
	@Column(name = "amount")
	private int amount;
	
	/** 循環週期，如果不為零代表該款項為循環款項 */
	@Column(name = "recurring_period_year")
	private int recurringPeriodYear;
	
	@Column(name = "recurring_period_month")
	private int recurringPeriodMonth;
	
	@Column(name = "recurring_period_day")
	private int recurringPeriodDay;
	
	@Column(name = "create_date")
	private LocalDate createDate;
	
	@Column(name = "record_date")
	private LocalDate recordDate;
	
	@Column(name = "delete_date")
	private LocalDate deleteDate;
	
	@Column(name = "year")
	private int year;
	
	@Column(name = "month")
	private int month;
	
	@Column(name = "day")
	private int day;
	
	private final static String INCOME = "收入";

	public int getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(int paymentId) {
		this.paymentId = paymentId;
	}

	public int getBalanceId() {
		return balanceId;
	}

	public void setBalanceId(int balanceId) {
		this.balanceId = balanceId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getRecurringPeriodYear() {
		return recurringPeriodYear;
	}

	public void setRecurringPeriodYear(int recurringPeriodYear) {
		this.recurringPeriodYear = recurringPeriodYear;
	}

	public int getRecurringPeriodMonth() {
		return recurringPeriodMonth;
	}

	public void setRecurringPeriodMonth(int recurringPeriodMonth) {
		this.recurringPeriodMonth = recurringPeriodMonth;
	}

	public int getRecurringPeriodDay() {
		return recurringPeriodDay;
	}

	public void setRecurringPeriodDay(int recurringPeriodDay) {
		this.recurringPeriodDay = recurringPeriodDay;
	}

	public LocalDate getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDate createDate) {
		this.createDate = createDate;
	}

	public LocalDate getRecordDate() {
		return recordDate;
	}

	public void setRecordDate(LocalDate recordDate) {
		this.recordDate = recordDate;
	}

	public LocalDate getDeleteDate() {
		return deleteDate;
	}

	public void setDeleteDate(LocalDate deleteDate) {
		this.deleteDate = deleteDate;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}
	
	/**
	 * 檢查該款項是否為「收入款項」。
	 * @return 若類型為收入時返回 {@code TRUE}，否則返回 {@code FALSE}
	 */
	public boolean isIncome()
	{
		return this.type.equals(INCOME);
	}
	
	/**
	 * 檢查該款項是否為「固定／循環款項」。
	 * @return 若存在循環週期時返回 {@code TRUE}，否則返回 {@code FALSE}
	 */
	public boolean hasPeriod()
	{
		var period = getPeriod();
		return period.hasPeriod();
	}
	
	/**
	 * 檢查該款項是否被標記刪除。
	 * @return 如果存在刪除日期，表示被標記為刪除，返回 {@code TRUE}，若不存在則返回 {@code FALSE}
	 */
	public boolean isDeleted()
	{
		return deleteDate != null;
	}
	
	/**
	 * 檢查該款項與傳入的「年月日」是否一致。
	 * @see Payment#isOnTime(int, int)
	 * @see Payment#isOnTime(int)
	 */
	public boolean isOnTime(int year, int month, int day)
	{
		return this.year == year && this.month == month && this.day == day;
	}
	
	/**
	 * 檢查該款項與傳入的「年月」是否一致。
	 * @see Payment#isOnTime(int, int, int)
	 * @see Payment#isOnTime(int)
	 */
	public boolean isOnTime(int year, int month)
	{
		return this.year == year && this.month == month;
	}
	
	/**
	 * 檢查該款項與傳入的「年」是否一致。
	 * @see Payment#isOnTime(int, int, int)
	 * @see Payment#isOnTime(int, int)
	 */
	public boolean isOnTime(int year)
	{
		return this.year == year;
	}
	
	/**
	 * 取得該款項的週期物件映射。
	 * @return 該款項的週期物件映射
	 */
	public RecurringPeriodVO getPeriod()
	{
		return new RecurringPeriodVO(
				recurringPeriodYear, 
				recurringPeriodMonth, 
				recurringPeriodDay
				);
	}
	
	/**
	 * 從該款項的記帳日期開始，計算到結束日期之間（不包含）的循環次數。<p>
	 * 
	 * 如果結束日期設定於記帳日期之前，或者該款項為「非循環」，皆返回 0。
	 * @param end 結束日期，如果值為 {@code NULL} 將預設為執行的日期
	 * @return 循環次數
	 */
	public int getRecurringTimes(@Nullable LocalDate end)
	{
		if(end == null)
			end = LocalDate.now();
		
		if(recordDate.isAfter(end))
			return 0;
		
		var temp = recordDate;
		int result = 0;
		var period = getPeriod();
		if(!period.hasPeriod())
			return 0;
		
		while(temp.isAfter(end))
		{
			result++;
			temp.plusDays(period.day());
			temp.plusMonths(period.month());
			temp.plusYears(period.year());
		}
		return result;
	}
	
	/**
	 * 判斷該循環款項是否「最靠近」設定的日期。最靠近的意思是該款項進入下次循環
	 * 之前沒有其他的循環週期。<p>
	 * 
	 * 如果在計算前就已經超過設定日期、或該款項不為循環款項時檢查不通過。
	 * @param date 要判斷的日期
	 * @return 如果前置檢查通過、且下次循環日期在檢查日期之後時返回 {@code TRUE}
	 */
	public boolean isClose(LocalDate date)
	{
		Assert.notNull(date, "檢查日期不得為空值");
		return getRecurringTimes(date) == 1;
	}
	
}
