package com.example.FinCore.vo;

import com.example.FinCore.constants.ConstantsMessage;

import jakarta.validation.constraints.Min;

/**
 * 用來簡化循環週期的值對象，包含年、月、日。
 * @author 羊羊
 */
public record RecurringPeriodVO(
		@Min(value = 0, message = ConstantsMessage.RECURRING_PERIOD_NEGATIVE_ERROR)
		int year, 
		
		@Min(value = 0, message = ConstantsMessage.RECURRING_PERIOD_NEGATIVE_ERROR)
		int month, 
		
		@Min(value = 0, message = ConstantsMessage.RECURRING_PERIOD_NEGATIVE_ERROR)
		int day) 
{
	
	/**
	 * 檢查是否有週期
	 * @return 如果存在週期（年月日任一存在）時返回 {@code TRUE}，否則返回 {@code FALSE}
	 */
	public boolean hasPeriod()
	{
		return year != 0 || month != 0 || day != 0;
	}
	
	/**
	 * 檢查該週期設定是否有效。
	 * @return 如果有效則傳回 {@code TRUE}
	 */
	public boolean isPeriodValid()
	{
		return year > 0 || month > 0 || day > 0;
	}
	
}
