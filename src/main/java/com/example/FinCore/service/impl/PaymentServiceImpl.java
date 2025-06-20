package com.example.FinCore.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.FinCore.constants.ResponseMessages;
import com.example.FinCore.dao.BalanceDao;
import com.example.FinCore.dao.PaymentDao;
import com.example.FinCore.dao.SavingsDao;
import com.example.FinCore.dao.UserDao;
import com.example.FinCore.entity.Payment;
import com.example.FinCore.service.itfc.PaymentService;
import com.example.FinCore.vo.BalanceWithPaymentVO;
import com.example.FinCore.vo.PaymentInfoVO;
import com.example.FinCore.vo.RecurringPeriodVO;
import com.example.FinCore.vo.request.AccountWithDateFilterRequest;
import com.example.FinCore.vo.request.CreatePaymentRequest;
import com.example.FinCore.vo.request.RecoveryPaymentRequest;
import com.example.FinCore.vo.request.UpdatePaymentRequest;
import com.example.FinCore.vo.response.BasicResponse;
import com.example.FinCore.vo.response.SearchPaymentResponse;

import jakarta.transaction.Transactional;

@EnableScheduling
@Service
public class PaymentServiceImpl implements PaymentService 
{
	
	@Autowired
	private PaymentDao paymentDao; 
	
	@Autowired
	private BalanceDao balanceDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private SavingsDao savingsDao;

	@Override
	@Transactional(rollbackOn = Exception.class)
	public BasicResponse create(CreatePaymentRequest req) throws Exception
	{
		if(!balanceDao.existsById(req.balanceId()))
			return new BasicResponse(ResponseMessages.BALANCE_NOT_FOUND);
		
		var period = req.recurringPeriod();
		LocalDate today = LocalDate.now();
//		如果 req 沒有提供紀錄日期，就預設是創建的當下
		LocalDate generatedRecordDate = req.recordDate() == null ? LocalDate.now() : req.recordDate();
		var res = checkDate(generatedRecordDate, period);
		if(res != null)
			return res;
		
		int year = generatedRecordDate.getYear();
		int month = generatedRecordDate.getMonthValue();
		int day = generatedRecordDate.getDayOfMonth();
		if(savingsDao.getCountById(req.balanceId(), year, month) == 0)
			savingsDao.create(req.balanceId(), year, month, 0);
		
		paymentDao.create(
				req.balanceId(), 
				req.description(), 
				req.type(), 
				req.item(), 
				req.amount(), 
				period.year(), 
				period.month(),
				period.day(), 
				today,
				generatedRecordDate, 
				year, month, day);
		return new BasicResponse(ResponseMessages.SUCCESS);
	}

	@Override
	public BasicResponse delete(int paymentId) 
	{
		var payment = paymentDao.getEntity(paymentId);
		if(payment == null)
			return new BasicResponse(ResponseMessages.PAYMENT_NOT_FOUND);
		
		if(payment.isDeleted())
			return new BasicResponse(ResponseMessages.PAYMENT_HAS_BEEN_DELETED);
		
		paymentDao.updateDeleteDate(paymentId, LocalDate.now());
		return new BasicResponse(ResponseMessages.SUCCESS);
	}
	
	@Override
	public BasicResponse update(UpdatePaymentRequest req) throws Exception 
	{
		Payment payment = paymentDao.getEntity(req.paymentId());
		if(payment == null)
			return new BasicResponse(ResponseMessages.PAYMENT_NOT_FOUND);
		
		if(payment.isDeleted())
			return new BasicResponse(ResponseMessages.DELETED_PAYMENT_CANNOT_UPDATE);
		
		var period = req.recurringPeriod();
		LocalDate recordDate = req.recordDate();
		var res = checkDate(recordDate, period);
		if(res != null)
			return res;
		
		if(!payment.isFuture() && !period.equals(payment.getPeriod()))
			return new BasicResponse(ResponseMessages.PAYMENT_PERIOD_UNABLE_MODIFYING);
		
		int year = recordDate.getYear();
		int month = recordDate.getMonthValue();
		int day = recordDate.getDayOfMonth();
		int balanceId = paymentDao.getBalanceId(req.paymentId());
		if(savingsDao.getCountById(balanceId, year, month) == 0)
			savingsDao.create(balanceId, year, month, 0);
		
		paymentDao.update(
				req.paymentId(), 
				req.description(), 
				req.type(), 
				req.item(), 
				req.amount(), 
				period.year(), 
				period.month(), 
				period.day(), 
				recordDate, 
				year, month, day);
		return new BasicResponse(ResponseMessages.SUCCESS);
	}
	
	/**
	 * 檢查日期合法性，項目如下：
	 * <ol>
	 * 	<li>如果該款項是固定款項，但記帳時間在過去（包含今天）時不通過</li>
	 * 	<li>如果該款項是非固定款項，但記帳時間在未來（不包含今天）時不通過</li>
	 * </ol>
	 * @param date 
	 * @param period
	 * @return
	 */
	private BasicResponse checkDate(LocalDate date, RecurringPeriodVO period)
	{
		LocalDate today = LocalDate.now();
		if(period.hasPeriod() && !date.isAfter(today))
			return new BasicResponse(ResponseMessages.PAST_RECORD_DATE);
		
		if(!period.hasPeriod() && date.isAfter(today))
			return new BasicResponse(ResponseMessages.FUTURE_RECORD_DATE);
		
		return null;
	}

	@Override
	public SearchPaymentResponse getPaymentInfoByAccount(String account) 
	{
		if(!userDao.existsById(account))
			return new SearchPaymentResponse(ResponseMessages.ACCOUNT_NOT_FOUND);
		
		var resultList = getPaymentInfoOpration(account, -1, 0, true);
		
		return new SearchPaymentResponse(ResponseMessages.SUCCESS, resultList);
	}
	
	@Override
	public SearchPaymentResponse getPaymentInfoWithDateFilter(AccountWithDateFilterRequest req) 
	{
		if(!userDao.existsById(req.account()))
			return new SearchPaymentResponse(ResponseMessages.ACCOUNT_NOT_FOUND);
		
		var resultList = getPaymentInfoOpration(req.account(), req.year(), req.month(), true);
		
		return new SearchPaymentResponse(ResponseMessages.SUCCESS, resultList);
	}
	
	/**
	 * 生成 BalanceWithPaymentVO 列表
	 * @param account 帳號
	 * @param year 指定年
	 * @param month 指定月
	 * @param deletedFilter 是否啟用刪除過濾器
	 * @return BalanceWithPaymentVO 列表
	 */
	private List<BalanceWithPaymentVO> getPaymentInfoOpration(String account, int year, int month, boolean deletedFilter)
	{
		List<Integer> balanceIdList = balanceDao.getBalanceIdListByAccount(account);
		List<Payment> paymentList = paymentDao.getPaymentListByBalanceIdList(balanceIdList);
		List<BalanceWithPaymentVO> resultList = new ArrayList<>();
		Map<Integer, List<PaymentInfoVO>> map = new HashMap<>();
		generateBalanceWithPaymentMap(map, paymentList, year, month, deletedFilter);
		for(Entry<Integer, List<PaymentInfoVO>> entry : map.entrySet())
			resultList.add(new BalanceWithPaymentVO(entry.getKey(), entry.getValue()));
		return resultList;
	}
	
	/**
	 * 設定 BalanceWithPaymentMap，首先會將取得的款項使用過濾器篩選，再將同一個帳戶的
	 * 款項綁定在一起。
	 * @param map 要設定的 Map
	 * @param paymentList 款項列表
	 * @param yearFilter 年過濾器，不為 -1 時將啟動，會過濾不在該年的款項
	 * @param monthFilter 月過濾器，不為 0 時將啟動，會過濾不在該月的款項；年過濾器
	 * 		未啟動時會忽略該過濾器
	 * @param deleteFilter 刪除過濾器，為 true 時將啟動，會過濾已標記刪除的款項；若不啟動將會反向
	 * 		過濾，意即會過濾未刪除的款項
	 */
	private void generateBalanceWithPaymentMap(Map<Integer, List<PaymentInfoVO>> map, List<Payment> paymentList, int yearFilter, int monthFilter, boolean deleteFilter)
	{
		for(Payment payment : paymentList)
		{
//			如果 deleteFilter 啟動，且 DeleteDate 存在，代表該款項已標記刪除，不進行設定
			if(deleteFilter && payment.isDeleted())
				continue;
			
			if(!deleteFilter && !payment.isDeleted())
				continue;
			
//			如果 yearFilter 啟動，再判斷 monthFilter 是否啟動，篩除不符合時段的款項
//			yearFilter 未啟動則不過濾年月
			if(yearFilter != -1)
				if(monthFilter != 0)
					if(!payment.isOnTime(yearFilter, monthFilter))
						continue;
				else
					if(!payment.isOnTime(yearFilter))
						continue;
			
			var paymentInfo = setPaymentInfoVO(payment);
			List<PaymentInfoVO> voList = 
					map.containsKey(payment.getBalanceId()) ? map.get(payment.getBalanceId()) : new ArrayList<>();
			voList.add(paymentInfo);
			map.put(payment.getBalanceId(), voList);
		}
	}
	
	/**
	 * 將 payment 實體轉為 PaymentInfoVO
	 * @param payment 款項
	 * @return 轉換完成的 VO
	 */
	private PaymentInfoVO setPaymentInfoVO(Payment payment)
	{
		var period = new RecurringPeriodVO(
				payment.getRecurringPeriodYear(), 
				payment.getRecurringPeriodMonth(), 
				payment.getRecurringPeriodDay()
				);
		int lifeTime = -1;
		if(payment.isDeleted())
		{
			LocalDate deleteDate = payment.getDeleteDate();
			LocalDate now = LocalDate.now();
			lifeTime = (int) (deleteDate.toEpochDay() - now.toEpochDay()) + 10 - 1;
		}
		var paymentInfo = new PaymentInfoVO(
				payment.getPaymentId(),
				payment.getDescription(), 
				payment.getType(), 
				payment.getItem(), 
				payment.getAmount(), 
				period, 
				payment.getRecordDate(),
				lifeTime
				);
		return paymentInfo;
	}

	@Override
	public BasicResponse recovery(RecoveryPaymentRequest req) 
	{
		for(int id : req.paymentIdList())
			if(!paymentDao.existsById(id))
				return new BasicResponse(
						ResponseMessages.PAYMENT_NOT_FOUND.getCode(), 
						ResponseMessages.PAYMENT_NOT_FOUND.getMessage() + "：" + id
						);
		
		paymentDao.undoDeleted(req.paymentIdList());
		return new BasicResponse(ResponseMessages.SUCCESS);
	}

	@Override
	public SearchPaymentResponse getDeletedPayment(String account) 
	{
		if(!userDao.existsById(account))
			return new SearchPaymentResponse(ResponseMessages.ACCOUNT_NOT_FOUND);
		
		var result = getPaymentInfoOpration(account, -1, 0, false);
		
		return new SearchPaymentResponse(ResponseMessages.SUCCESS, result);
	}
	
	/**
	 * 每日凌晨 3 點執行的排程任務，用於自動建立下一筆尚未記錄的循環帳款。
	 * <p>
	 * 任務會依據以下條件篩選出有效的循環款項：
	 * <ul>
	 *   <li>尚未被標記為刪除</li>
	 *   <li>週期設定為有效</li>
	 *   <li>記錄日為未來</li>
	 *   <li>與當前日期 {@link LocalDate#now()} 為第一個週期位置（{@link Payment#isCloseDate(LocalDate)}）</li>
	 * </ul>
	 * 符合條件者若其循環時間已到（{@link Payment#isOnTime(int, int, int)}），
	 * 則會依據該筆款項產生下一筆循環款項並寫入資料庫。
	 * <p>
	 * 本方法具備交易控制，若處理過程中任一筆操作失敗將觸發回滾。
	 *
	 * @throws Exception 若建立過程中發生例外
	 */
	@Scheduled(cron = "0 0 3 * * ?")
	@Transactional(rollbackOn = Exception.class)
	public void scheduledCreate() throws Exception
	{
//		System.out.println("循環");
//		蒐集所有資料庫中的循環帳款
		List<Payment> paymentList = paymentDao.getAllRecurringPayment();
		for(Payment payment : paymentList)
		{
//			System.out.println(payment);
//			過濾刪除的款項
			if(payment.isDeleted())
				continue;
			
//			過濾具有無效周期的款項（防患未然）
			if(!payment.isPeriodValid())
				continue;
			
//			只蒐集距離執行時間最近一個週期的款項
			LocalDate now = LocalDate.now();
			if(!payment.isCloseDate(now))
				continue;
			
			int year = now.getYear();
			int month = now.getMonthValue();
			int day = now.getDayOfMonth();
//			只要偵測到循環結束，就建立下一筆未來循環款項
			if(payment.isOnTime(year, month, day))
			{
				Payment next = payment.nextRecurrence();
				int nextYear = next.getRecordDate().getYear();
				int nextMonth = next.getRecordDate().getMonthValue();
				int nextDay = next.getRecordDate().getDayOfMonth();
				var period = next.getPeriod();
				paymentDao.create(
						next.getBalanceId(), 
						next.getDescription(), 
						next.getType(), 
						next.getItem(), 
						next.getAmount(), 
						period.year(), period.month(), period.day(), 
						now, 
						next.getRecordDate(), 
						nextYear, nextMonth, nextDay
						);
			}
		}
	}

}
