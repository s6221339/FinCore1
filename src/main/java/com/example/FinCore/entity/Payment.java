package com.example.FinCore.entity;

import java.time.LocalDate;

import org.springframework.util.Assert;

import com.example.FinCore.constants.ConstantsMessage;
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
	
	private final static String TYPE_INCOME = "收入";
	
	private final static String TYPE_OTHER = "其他";
	
	private final static String TRANSFERS_IN = "（轉帳）轉入";
	
	private final static String TRANSFERS_OUT = "（轉帳）轉出";

	public Payment() {
		super();
	}

	public Payment(int paymentId, int balanceId, String description, String type, String item, int amount, int recurringPeriodYear,
			int recurringPeriodMonth, int recurringPeriodDay, LocalDate createDate, LocalDate recordDate,
			LocalDate deleteDate, int year, int month, int day) {
		super();
		this.paymentId = paymentId;
		this.balanceId = balanceId;
		this.description = description;
		this.type = type;
		this.item = item;
		this.amount = amount;
		this.recurringPeriodYear = recurringPeriodYear;
		this.recurringPeriodMonth = recurringPeriodMonth;
		this.recurringPeriodDay = recurringPeriodDay;
		this.createDate = createDate;
		this.recordDate = recordDate;
		this.deleteDate = deleteDate;
		this.year = year;
		this.month = month;
		this.day = day;
	}

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
		return this.type.equals(TYPE_INCOME);
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
	 * 檢查該款項的週期性是否有效，如果檢查為非週期款項判定不通過。
	 * @return 檢查通過時傳回 {@code TRUE}
	 */
	public boolean isPeriodValid()
	{
		var period = getPeriod();
		return period.checkPeriodValid();
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
	 * 檢查該款項記帳日期與傳入的「年月日」是否一致。
	 * @param year 指定年
	 * @param month 指定月
	 * @param day 指定日
	 * @see Payment#isOnTime(int, int)
	 * @see Payment#isOnTime(int)
	 */
	public boolean isOnTime(int year, int month, int day)
	{
		return this.year == year && this.month == month && this.day == day;
	}
	
	/**
	 * 檢查該款項記帳日期與傳入的「年月」是否一致。
	 * @param year 指定年
	 * @param month 指定月
	 * @see Payment#isOnTime(int, int, int)
	 * @see Payment#isOnTime(int)
	 */
	public boolean isOnTime(int year, int month)
	{
		return this.year == year && this.month == month;
	}
	
	/**
	 * 檢查該款項記帳日期與傳入的「年」是否一致。
	 * @param year 指定年
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
	 * 從該款項的記帳日期開始（不包含），計算到結束日期之間（包含）的循環次數。
	 * @param end 結束日期，如果值為 {@code NULL} 將預設為執行的日期
	 * @return 循環次數
	 */
	public int getRecurringCount(@Nullable LocalDate end)
	{
		if(end == null)
			end = LocalDate.now();
		
		return getRecurringCount(recordDate, end, getPeriod());
	}
	
	/**
	 * 計算從開始日期（不包含）到結束日期（包含）的循環次數，未滿足一次週期
	 * 的次數將會視為一次週期。<p>
	 * 如果結束日期設定於開始日期之前，或者不存在循環週期，皆返回 0。
	 * <pre>
	 * LocalDate start = LocalDate.of(2025, 1, 1);
	 * LocalDate end = LocalDate.of(2025, 1, 10);
	 * RecurringPeriodVO period_1 = new RecurringPeriodVO(0, 0, 1);
	 * RecurringPeriodVO period_2 = new RecurringPeriodVO(0, 0, 5);
	 * RecurringPeriodVO period_3 = new RecurringPeriodVO(0, 0, 10);
	 * Payment.getRecurringCount(start, end, period_1) == 10
	 * Payment.getRecurringCount(start, end, period_2) == 2
	 * Payment.getRecurringCount(start, end, period_3) == 1
	 * </pre>
	 * @param start 開始日期
	 * @param end 結束日期
	 * @param period 循環週期
	 * @return 循環次數
	 */
	public static int getRecurringCount(LocalDate start, LocalDate end, RecurringPeriodVO period)
	{
		if(start.isAfter(end))
			return 0;
		
		var temp = start;
		int result = 0;
		if(!period.hasPeriod())
			return 0;
		
		while(true)
		{
			temp = plusPeriod(temp, period);
			result++;
			if(temp.isAfter(end)) break;
		}
		return result;
	}
	
	/**
	 * 判斷指定日期是否為這筆款項記錄日的「第一個週期位置」。
	 * <p>
	 * 根據款項是否為未來款項（{@link #isFuture()}），會以 {@code recordDate}
	 * 為起點或終點，計算與指定日期 {@code date} 間的週期次數。若剛好為第 1 次週期，則返回 {@code true}。
	 * <p>
	 * 例如：若款項記錄日為 2025/01/01，週期為每 10 天，則：
	 * <ul>
	 *   <li>{@code 2025/01/01} 為第一個週期位置，返回 {@code true}</li>
	 *   <li>{@code 2025/01/11} 為第一個週期位置，返回 {@code true}</li>
	 *   <li>{@code 2025/01/21} 為第二個週期位置，返回 {@code false}</li>
	 * </ul>
	 *
	 * @param date 欲檢查的日期，不能為 {@code null}
	 * @return 若該日期為第一個週期位置則返回 {@code true}，否則為 {@code false}
	 * @throws IllegalArgumentException 若 {@code date} 為 {@code null}
	 */
	public boolean isCloseDate(LocalDate date)
	{
		Assert.notNull(date, "檢查日期不得為空值");
		if(isFuture())
			return getRecurringCount(date, recordDate, getPeriod()) == 1;
		else
			return getRecurringCount(recordDate, date, getPeriod()) == 1;
	}
	
	/**
	 * 檢查該款項是否為記錄在相對於現在時間的未來款項（不包括現在）。
	 * @return 
	 */
	public boolean isFuture()
	{
		LocalDate today = LocalDate.now();
		return recordDate.isAfter(today);
	}
	
	/**
	 * 取得該筆款項的下一次循環款項實體。除了時間類屬性與款項編號，其他屬性
	 * 都將繼承給該款項；款項編號將重設為0、創建時間為執行時間、記帳時間為
	 * 歷經一次循環週期的時間。
	 * @return 款項實體
	 */
	public Payment nextRecurrence()
	{
		var period = getPeriod();
		if(!period.checkPeriodValid())
			return this;
		
		LocalDate nextRecordDate = recordDate;
		nextRecordDate = plusPeriod(nextRecordDate, period);
		return new Payment(
				0, 
				balanceId, 
				description, 
				type, item, 
				amount, 
				period.year(), period.month(), period.day(), 
				LocalDate.now(), 
				nextRecordDate, 
				null, 
				nextRecordDate.getYear(), nextRecordDate.getMonthValue(), nextRecordDate.getDayOfMonth()
				);
	}
	
	private static LocalDate plusPeriod(LocalDate date, RecurringPeriodVO period)
	{
		date = date.plusDays(period.day());
		date = date.plusMonths(period.month());
		date = date.plusYears(period.year());
		return date;
	}
	
	/**
	 * 快速建構一個轉帳轉入的帳款。<br>
	 * 注意下列事項：
	 * <li>款項類型與項目都已固定為「收入／（轉帳）轉入」</li>
	 * <li>該帳款必不為循環帳款，因此循環週期必為 0</li>
	 * <li>建立日期與記帳日期鎖定在建構當下的日期</li>
	 * <li>paymentId 預設為 0</li>
	 * @param balanceId 依附的帳戶
	 * @param description 該款項的敘述
	 * @param amount 轉入金額
	 * @return 建構完成的 Payment 實體
	 */
	public static Payment ofTransfersIn(
			int balanceId,
			String description,
			int amount
			)
	{
		if(balanceId < 1)
			throw new IllegalArgumentException(ConstantsMessage.BALANCE_ID_VALUE_ERROR);
		
		if(amount < 0)
			throw new IllegalArgumentException(ConstantsMessage.AMOUNT_NEGATIVE_ERROR);
		
		LocalDate now = LocalDate.now();
		return new Payment(
				0,
				balanceId, 
				description, 
				TYPE_INCOME, TRANSFERS_IN,
				amount,
				0, 0, 0,
				now,
				now,
				null,
				now.getYear(), now.getMonthValue(), now.getDayOfMonth());
	}
	
	/**
	 * 快速建構一個轉帳轉出的帳款。<br>
	 * 注意下列事項：
	 * <li>款項類型與項目都已固定為「其他／（轉帳）轉出」</li>
	 * <li>該帳款必不為循環帳款，因此循環週期必為 0</li>
	 * <li>建立日期與記帳日期鎖定在建構當下的日期</li>
	 * <li>paymentId 預設為 0</li>
	 * @param balanceId 依附的帳戶
	 * @param description 該款項的敘述
	 * @param amount 轉出金額
	 * @return 建構完成的 Payment 實體
	 */
	public static Payment ofTransfersOut(
			int balanceId,
			String description,
			int amount
			)
	{
		if(balanceId < 1)
			throw new IllegalArgumentException(ConstantsMessage.BALANCE_ID_VALUE_ERROR);
		
		if(amount < 0)
			throw new IllegalArgumentException(ConstantsMessage.AMOUNT_NEGATIVE_ERROR);
		
		LocalDate now = LocalDate.now();
		return new Payment(
				0, 
				balanceId, 
				description, 
				TYPE_OTHER, TRANSFERS_OUT,
				amount,
				0, 0, 0,
				now,
				now,
				null,
				now.getYear(), now.getMonthValue(), now.getDayOfMonth());
	}

	/**
	 * 檢查該帳款的屬性內容是否與另一筆帳款相同。
	 * @param obj 任意物件
	 * @return 如果內容相同時返回 {@code TRUE}
	 */
	public boolean contentEquals(Object obj) 
	{
		if(!(obj instanceof Payment payment))
			return false;
		
		return balanceId == payment.getBalanceId() &&
				description == payment.getDescription() &&
				type == payment.getType() && 
				item == payment.getItem() && 
				amount == payment.getAmount() && 
				getPeriod() == payment.getPeriod() &&
				recordDate == payment.getRecordDate();
	}

	@Override
	public String toString() {
		return "Payment [paymentId=" + paymentId + ", balanceId=" + balanceId + ", description=" + description
				+ ", type=" + type + ", item=" + item + ", amount=" + amount + ", recurringPeriodYear="
				+ recurringPeriodYear + ", recurringPeriodMonth=" + recurringPeriodMonth + ", recurringPeriodDay="
				+ recurringPeriodDay + ", createDate=" + createDate + ", recordDate=" + recordDate + ", deleteDate="
				+ deleteDate + ", year=" + year + ", month=" + month + ", day=" + day + "]";
	}
	
	/**
	 * 取得該款項的剩餘生命週期。<p>
	 * 
	 * 基本上，該屬性只會生效在「已刪除」的款項，對於未被刪除的款項永遠只會
	 * 返回值 99。對於已刪除的款項，會計算從執行當下到刪除日期屬性的時間差，
	 * 且為包含當下的生命週期。例如
	 * <pre>
	 * （如果已刪除）
	 * LocalDate deleteDate = 2025-06-15;
	 * LocalDate dateA = 2025-06-15;
	 * lifeTime = 9
	 * （如果未刪除）
	 * lifeTime = 99
	 * <pre>
	 * @return 該款項的生命週期
	 */
	public int getLifeTime()
	{
		int lifeTime = 99;
		if(isDeleted())
		{
			LocalDate deleteDate = getDeleteDate();
			LocalDate now = LocalDate.now();
			lifeTime = (int) (deleteDate.toEpochDay() - now.toEpochDay()) + 10 - 1;
		}
		return lifeTime;
	}
	
}
