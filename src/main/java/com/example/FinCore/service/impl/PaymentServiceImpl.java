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
import org.springframework.util.StringUtils;

import com.example.FinCore.constants.ResponseMessages;
import com.example.FinCore.dao.BalanceDao;
import com.example.FinCore.dao.FamilyDao;
import com.example.FinCore.dao.PaymentDao;
import com.example.FinCore.dao.SavingsDao;
import com.example.FinCore.dao.UserDao;
import com.example.FinCore.entity.Balance;
import com.example.FinCore.entity.Family;
import com.example.FinCore.entity.Payment;
import com.example.FinCore.service.itfc.PaymentService;
import com.example.FinCore.vo.BalanceInfoVO;
import com.example.FinCore.vo.BalanceWithPaymentVO;
import com.example.FinCore.vo.FamilyInfoVO;
import com.example.FinCore.vo.PaymentAmountVO;
import com.example.FinCore.vo.PaymentInfoVO;
import com.example.FinCore.vo.RecurringPeriodVO;
import com.example.FinCore.vo.StatisticsInfoVO;
import com.example.FinCore.vo.StatisticsVO;
import com.example.FinCore.vo.request.AccountWithDateFilterRequest;
import com.example.FinCore.vo.request.CreatePaymentRequest;
import com.example.FinCore.vo.request.RecoveryPaymentRequest;
import com.example.FinCore.vo.request.StatisticsRequest;
import com.example.FinCore.vo.request.UpdatePaymentRequest;
import com.example.FinCore.vo.response.BasicResponse;
import com.example.FinCore.vo.response.SearchPaymentResponse;
import com.example.FinCore.vo.response.StatisticsResponse;

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
	
	@Autowired
	private FamilyDao familyDao;

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
		var paymentInfo = new PaymentInfoVO(
				payment.getPaymentId(),
				payment.getDescription(), 
				payment.getType(), 
				payment.getItem(), 
				payment.getAmount(), 
				period, 
				payment.getRecordDate(),
				payment.getLifeTime()
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
	
	@Override
	public StatisticsResponse statistics(StatisticsRequest req) 
	{
		if(!userDao.existsById(req.account()))
			return new StatisticsResponse(ResponseMessages.ACCOUNT_NOT_FOUND);
		
		/* 最終統計列表 */
		List<StatisticsVO> statisticsList = new ArrayList<>();
		
//		取得與帳號關聯的所有帳戶
		List<Balance> balanceFromAccountList = balanceDao.getAllBalanceByAccount(req.account());
		
//		取得與群組關聯的所有帳戶
		List<Family> allFamilyList = familyDao.findAll();
		List<Family> familyList = new ArrayList<>();
		List<Integer> familyIdList = new ArrayList<>();
		allFamilyList.forEach(family -> {
			if(family.isMember(req.account()))
				familyList.add(family);
		});
		Map<Integer, String> familyMap = new HashMap<>();
		familyList.forEach(t -> {
			familyIdList.add(t.getId());
			familyMap.put(t.getId(), t.getName());
		});
		List<Balance> balanceFromFamilyList = balanceDao.getAllBalanceByFamilyIdList(familyIdList);
		var balanceList = balanceFromAccountList;
		
//		合併帳號與群組的帳戶，並依照帳戶編號排序
		balanceList.addAll(balanceFromFamilyList);
		balanceList.sort((o1, o2) -> o1.getBalanceId() - o2.getBalanceId());
		List<Integer> balanceIdList = new ArrayList<>();
		Map<Integer, String> balanceMap = new HashMap<>();
		balanceList.forEach(balance -> {
			balanceIdList.add(balance.getBalanceId());
			balanceMap.put(balance.getBalanceId(), balance.getName());
		});
//		綁定 BalanceInfoVO 和 FamilyInfoVO
		Map<BalanceInfoVO, FamilyInfoVO> infoMap = new HashMap<>();
		setInfoMap(infoMap, balanceList, familyList);
		
//		取得這些帳戶保存的所有款項
		List<Payment> paymentList = paymentDao.getPaymentListByBalanceIdList(balanceIdList);
		
//		如果月份指定 1~12 月，就針對該設定進行篩選
//		如果不指定則僅篩選該年分的所有款項
		List<Payment> filtedPaymentList = new ArrayList<>();
		if(req.month() < 1 || req.month() > 12)
		{
			paymentList.forEach(payment -> {
				if(!payment.isDeleted() && payment.isOnTime(req.year()))
					filtedPaymentList.add(payment);
			});
//			只篩選年份的款項須考慮月份排序問題
			filtedPaymentList.sort((o1, o2) -> o1.getMonth() - o2.getMonth());
		}
		else
		{
			paymentList.forEach(payment -> {
				if(!payment.isDeleted() && payment.isOnTime(req.year(), req.month()))
					filtedPaymentList.add(payment);
			});
		}
		
		statisticsList = statisticsVOFactory(filtedPaymentList, infoMap, req.year());
		return new StatisticsResponse(ResponseMessages.SUCCESS, statisticsList);
	}
	
	/** 
	 * 設定 infoMap，藉此綁定 BalanceInfoVO 和 FamilyInfoVO
	 */
	private void setInfoMap(
			Map<BalanceInfoVO, FamilyInfoVO> infoMap, 
			List<Balance> balanceList,
			List<Family> familyList
			)
	{
		for(Balance b : balanceList)
		{
			BalanceInfoVO bInfo = new BalanceInfoVO(b.getBalanceId(), b.getName());
			
			int familyId = b.getFamliyId();
			String familyName = null;
//			如果該帳戶源自於一個群組，則該群組編號不為0
			if(familyId != 0)
			{
//				尋找對應的群組資料，通常能保證可找到對應的群組
				for(Family f : familyList)
					if(f.getId() == familyId)
					{
						familyName = f.getName();
						break;
					}
				FamilyInfoVO fInfo = new FamilyInfoVO(familyId, familyName);
//				將設定後的 bInfo 和 fInfo 設定至映射表
//				特別地，如果發生意外－familyList 中找不到對應的群組資料，則不進行設定
				if(StringUtils.hasText(familyName))
					infoMap.put(bInfo, fInfo);
				else
					infoMap.put(bInfo, null);
			}
			else
//				如果該帳戶不屬於群組，則不存在群組資料
				infoMap.put(bInfo, null);
		}
	}
	
	/**
	 * 處理 statisticsVO 列表的方法
	 * @param paymentList
	 * @param infoMap
	 * @param year
	 * @return
	 */
	private List<StatisticsVO> statisticsVOFactory(
			List<Payment> paymentList, 
			Map<BalanceInfoVO, FamilyInfoVO> infoMap,
			int year)
	{
		List<StatisticsVO> result = new ArrayList<>();
//		該映射表存放月分與分析資料
		Map<Integer, List<StatisticsInfoVO>> statisticsInfoVOMap = new HashMap<>();
		Map<String, Integer> paymentAmountMap = new HashMap<>();
		
		for(Payment payment : paymentList)
		{
			int month = payment.getMonth();
			List<StatisticsInfoVO> statisticsInfoVOList = 
					statisticsInfoVOMap.containsKey(month) ? statisticsInfoVOMap.get(month) : new ArrayList<>();

//			設定帳戶資訊與群組資訊
			BalanceInfoVO bInfo = null;
			FamilyInfoVO fInfo = null;
			for(Entry<BalanceInfoVO, FamilyInfoVO> infoEntry : infoMap.entrySet())
			{
				var k = infoEntry.getKey();
				var v = infoEntry.getValue();
//				通常可保證執行，因為每筆帳款必定源自某個帳戶
				if(payment.getBalanceId() == k.id())
				{
					bInfo = k;
					fInfo = v;
					break;
				}
			}
			
			StatisticsInfoVO sInfo = null;
//			檢查 statisticsInfoVOList 是否已存在資料
			for(StatisticsInfoVO sInfoVO : statisticsInfoVOList)
			{
				if(sInfoVO.balanceInfo().id() == bInfo.id())
				{
//					如果統計資料儲存的帳戶資訊與該帳款的帳戶資訊一致就設值
					sInfo = sInfoVO;
					break;
				}
			}
			if(sInfo != null)
			{
//				找到資料時執行
				var paymentAmountVOList = sInfo.paymentAmountList();
				String type = payment.getType();
				int amount = payment.getAmount();
//				檢查是否有相同類型的款項統計資料
				PaymentAmountVO paymentAmount = null;
				for(PaymentAmountVO paVO : paymentAmountVOList)
				{
					if(paVO.type().equals(type))
					{
						paymentAmount = paVO;
						break;
					}
				}
				if(paymentAmount != null)
				{
//					如果發現相同類型：
//					將統計金額累計，將原本的資料刪除，重新增加新的帳款資料
					paymentAmountVOList.remove(paymentAmount);
					paymentAmount = new PaymentAmountVO(type, paymentAmount.totalAmount() + amount);
				}
				else
				{
//					如果是新的類型：
//					直接新增新資料
					paymentAmount = new PaymentAmountVO(type, amount);
				}
//				設定款項統計列表與映射表
				paymentAmountVOList.add(paymentAmount);
				paymentAmountMap.put(type, amount);
//				設定統計資料
				sInfo = new StatisticsInfoVO(bInfo, fInfo, paymentAmountVOList);
			}
			else
			{
//				找不到對應資料
//				抽取帳款資料（類型與金額）
				String type = payment.getType();
				int amount = payment.getAmount();
//				初始化資料並加入陣列中
				var paymentAmount = new PaymentAmountVO(type, amount);
				List<PaymentAmountVO> paymentAmountVOList = new ArrayList<>();
				paymentAmountVOList.add(paymentAmount);
				paymentAmountMap.put(type, amount);
//				設定帳款資料
				sInfo = new StatisticsInfoVO(bInfo, fInfo, paymentAmountVOList);
			}
			statisticsInfoVOList.add(sInfo);
			statisticsInfoVOMap.put(month, statisticsInfoVOList);
		}
		for(Entry<Integer, List<StatisticsInfoVO>> statisticsInfoVOEntry : statisticsInfoVOMap.entrySet())
		{
			StatisticsVO statisticsVO = new StatisticsVO(year, statisticsInfoVOEntry.getKey(), statisticsInfoVOEntry.getValue());
			result.add(statisticsVO);
		}
		return result;
	}

	/**
	 * 每日凌晨 3 點執行的排程任務，用於自動建立下一筆尚未記錄的循環帳款。
	 * <p>
	 * 任務會依據以下條件篩選出有效的循環款項：
	 * <ul>
	 *   <li>尚未被標記為刪除</li>
	 *   <li>週期設定為有效</li>
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
	
	/**
	 * 每日凌晨 3 點執行的排程任務，用於永久刪除符合條件的帳款資料。
	 * <p>
	 * 該任務會檢查所有帳款資料，過濾出以下兩種條件皆符合者：
	 * <ul>
	 *   <li>已被標記為刪除（{@link Payment#isDeleted()}）</li>
	 *   <li>生命週期小於 0（{@link Payment#getLifeTime()}）</li>
	 * </ul>
	 * 此類資料視為「已刪除且無須保留」的紀錄，將透過 {@link PaymentDao#deleteByPaymentIdList(List)} 從資料庫中完全移除。
	 * <p>
	 * 本方法具備交易控制（{@link jakarta.transaction.Transactional}），可確保批次刪除的原子性。
	 */
	@Scheduled(cron = "0 0 3 * * ?")
	@Transactional
	public void scheduledDelete()
	{
		List<Payment> paymentList = paymentDao.findAll();
		List<Payment> deletedPaymentList = new ArrayList<>();
		/* 意旨蒐集所有生命週期為零的款項編號，被儲存在此列表的 
		 * 款項都將永遠從資料庫中移除。 */
		List<Integer> noLifePaymentIdList = new ArrayList<>();
		paymentList.forEach(t -> {
			if(t.isDeleted())
				deletedPaymentList.add(t);
		});
		deletedPaymentList.forEach(t -> {
			if(t.getLifeTime() < 0)
				noLifePaymentIdList.add(t.getPaymentId());
		});
		paymentDao.deleteByPaymentIdList(noLifePaymentIdList);
	}

}
