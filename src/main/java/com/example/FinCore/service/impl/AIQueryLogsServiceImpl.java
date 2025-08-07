package com.example.FinCore.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.FinCore.constants.ResponseMessages;
import com.example.FinCore.dao.AIQueryLogsDao;
import com.example.FinCore.dao.BalanceDao;
import com.example.FinCore.dao.PaymentDao;
import com.example.FinCore.dao.UserDao;
import com.example.FinCore.entity.AIQueryLogs;
import com.example.FinCore.entity.AIQueryLogsPK;
import com.example.FinCore.entity.Balance;
import com.example.FinCore.entity.Payment;
import com.example.FinCore.entity.User;
import com.example.FinCore.service.itfc.AIQueryLogsService;
import com.example.FinCore.service.itfc.AIService;
import com.example.FinCore.service.itfc.LoginService;
import com.example.FinCore.service.itfc.PaymentService;
import com.example.FinCore.vo.AIAnalysisVO;
import com.example.FinCore.vo.StatisticsIncomeAndOutlayWithBalanceInfoVO;
import com.example.FinCore.vo.StatisticsPaymentTypeVO;
import com.example.FinCore.vo.request.AICallRequest;
import com.example.FinCore.vo.request.AICreateRequest;
import com.example.FinCore.vo.request.GetAnalysisRequest;
import com.example.FinCore.vo.request.StatisticsRequest;
import com.example.FinCore.vo.response.AIAnalysisResponse;
import com.example.FinCore.vo.response.AICallbackResponse;
import com.example.FinCore.vo.response.BasicResponse;

import jakarta.transaction.Transactional;

@Service
@EnableScheduling
public class AIQueryLogsServiceImpl implements AIQueryLogsService 
{

	@Autowired
	private AIQueryLogsDao aiQueryLogsDao;
	
	@Autowired
	private PaymentDao paymentDao;
	
	@Autowired
	private BalanceDao balanceDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private AIService aiService;
	
	@Autowired
	private PaymentService paymentService;
	
	@Autowired
	private LoginService loginService;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	@Transactional(rollbackOn = Exception.class)
	public BasicResponse create(AICreateRequest req) throws Exception 
	{
		final int year = req.year();
		final int month = req.month();
		Boolean forcedWrite = req.forcedWrite();
		
		List<String> allAccounts = userDao.findAll()
				.stream()
				.filter(t -> t.isSubscription())
				.map(t -> t.getAccount())
				.toList();
		if(forcedWrite == null)
			forcedWrite = Boolean.FALSE;
		
		for(String account : allAccounts)
		{
			var res = call(new AICallRequest(account, year, month, forcedWrite));
			// 如果強制寫入被開啟，則無條件寫入
			// 如果強制寫入被關閉，則僅在無資料時寫入
			if(forcedWrite || !aiQueryLogsDao.existsById(new AIQueryLogsPK(account, year, month)))
			{
				if(res.getCode() != 200)
				{
					logger.error("帳號「" + account + "」分析失敗！原因：" + res.getCode() + "（" + res.getMessage() + "）");
					aiQueryLogsDao.save(new AIQueryLogs(account, year, month, null, LocalDate.now()));
				}
				else
				{
					logger.info("帳號「" + account + "」分析成功！");
					aiQueryLogsDao.save(new AIQueryLogs(account, year, month, res.getText(), LocalDate.now()));
				}
					
			}
			else
				logger.warn("帳號「" + account + "」無法寫入分析內容，因為該月帳號已被分析。如有需要請啟用 forcedWrite 來強制寫入資料。");
			
		}
		return new BasicResponse(ResponseMessages.SUCCESS);
	}

	@Override
	public AICallbackResponse call(AICallRequest req) throws Exception 
	{
		final String account = req.account();
		final int year = req.year();
		final int month = req.month();
		LocalDate previousDate = LocalDate.of(year, month, 1).minusMonths(1);
		final int previousYear = previousDate.getYear();
		final int previousMonth = previousDate.getMonthValue();
		Boolean forcedWrite = req.forcedWrite();
		
		if(!userDao.existsById(account))
			return new AICallbackResponse(ResponseMessages.ACCOUNT_NOT_FOUND);
		
		if(forcedWrite == null)
			forcedWrite = false;
		
		if(!forcedWrite && aiQueryLogsDao.existsById(new AIQueryLogsPK(account, year, month)))
			return new AICallbackResponse(ResponseMessages.DATA_HAS_BEEN_ANALYSED);
		
		// 取得已存在帳號的所有帳戶
		List<Balance> balances = balanceDao.getAllBalanceByAccount(account);
		
		// 取得關乎所有帳戶上個月的所有未刪除的帳款資料
		List<Payment> payments = getPaymentList(balances, year, month);
		List<Payment> previousPayments = getPaymentList(balances, previousYear, previousMonth);
		
		// 僅檢查要分析的月分資料
		if(payments.isEmpty())
			return new AICallbackResponse(ResponseMessages.NO_PAYMENT_DATA);
		
		// 帳款集合（對帳戶）
		Map<Balance, List<Payment>> bMap = new HashMap<>();
		generateBalanceMap(bMap, balances, payments);
		
		Map<Balance, List<Payment>> previousBMap = new HashMap<>();
		generateBalanceMap(previousBMap, balances, previousPayments);
		
		// 收入與支出的統計
		var ioStatisticsList = getIncomeOutlayStatisticsList(account, year, month);
		var pioStatisticsList = getIncomeOutlayStatisticsList(account, previousYear, previousMonth);
		
		// 款項種類的統計
		var ptStatisticsList = getPaymentTypeStatisticsList(account, year, month);
		var pptStatisticsList = getPaymentTypeStatisticsList(account, previousYear, previousMonth);
		
		String bq = generateBalanceQuery(balances);
		String pq = generatePaymentsQuery(bMap);
		String previousPQ = generatePaymentsQuery(previousBMap);
		String iostq = generateIncomeOutlayStatisticsQuery(ioStatisticsList);
		String piostq = generateIncomeOutlayStatisticsQuery(pioStatisticsList);
		String ptstq = generatePaymentTypeStatisticsQuery(ptStatisticsList);
		String pptstq = generatePaymentTypeStatisticsQuery(pptStatisticsList);
		String response = aiService.templateFromFileCode(variablesFactory(
				bq, 
				previousPQ, 
				piostq, 
				pptstq, 
				pq, 
				iostq, 
				ptstq));
		return new AICallbackResponse(ResponseMessages.SUCCESS, response);
	}
	
	private List<Payment> getPaymentList(List<Balance> balances, int year, int month)
	{
		return paymentDao.getPaymentListByBalanceIdList(balances.stream()
				.map(t -> t.getBalanceId())
				.toList()
			).stream()
			.filter(t -> !t.isDeleted() && t.isOnTime(year, month))
			.toList();
	}
	
	private void generateBalanceMap(Map<Balance, List<Payment>> target, List<Balance> bs, List<Payment> ps)
	{
		for(Balance b : bs)
			target.put(b, ps.stream().filter(t -> t.getBalanceId() == b.getBalanceId()).toList());
	}
	
	private List<StatisticsIncomeAndOutlayWithBalanceInfoVO> getIncomeOutlayStatisticsList(String account, int year, int month)
	{
		return paymentService.statisticsIncomeAndOutlayWithAllBalance(
				new StatisticsRequest(account, year, month))
				.getStatisticsList();
	}
	
	private List<StatisticsPaymentTypeVO> getPaymentTypeStatisticsList(String account, int year, int month)
	{
		return paymentService.statisticsLookupPaymentTypeSummarize(
				new StatisticsRequest(account, year, month)
				).getStatisticsList();
	}
	
	/**
	 * 每月排程分析（系統內部排程呼叫）<br>
	 * 執行時間：每月 1 號凌晨 3 點<br>
	 * 分析目標月份為上個月的記帳資料。
	 */
	@Scheduled(cron = "0 0 3 1 * ?")
	@Transactional(rollbackOn = Exception.class)
	public void analysis() throws Exception
	{
		logger.info("每月資料分析開始");
		LocalDate target = LocalDate.now().minusMonths(1);
		final int year = target.getYear();
		final int month = target.getMonthValue();
		create(new AICreateRequest(year, month, false));
		logger.info("分析結束");
	}
	
	private String generateBalanceQuery(List<Balance> bList)
	{
		if(bList.isEmpty())
		{
			logger.debug("沒有帳戶資料");
			return "＜查無資料＞";
		}
		
		StringBuilder sb = new StringBuilder();
		for(Balance b : bList)
			sb.append(String.format("－%s（balanceId：%d）\n", b.getName(), b.getBalanceId()));
		
		/*
		 * Ex. 
		 * －羊羊（balanceId：1）
		 * －桃莉羊（balanceId：2）
		 */
		return sb.toString();
	}
	
	private String generatePaymentsQuery(Map<Balance, List<Payment>> source)
	{
		if(source.isEmpty())
		{
			logger.debug("沒有帳款資料");
			return "＜查無資料＞";
		}
		StringBuilder sb = new StringBuilder();
		for(Entry<Balance, List<Payment>> entry : source.entrySet())
		{
			Balance b = entry.getKey();
			List<Payment> pList = entry.getValue();
			for(Payment p : pList)
				sb.append(String.format("%s | %s | %s%d | 源帳戶：%s（bId：%d）\n", 
						p.getRecordDate().toString(), 
						p.getType(), 
						p.isIncome() ? "+" : "-", 
						p.getAmount(), 
						b.getName(),
						b.getBalanceId()
						));
		}
		/*
		 * Ex. 
		 * －2025-07-01 | 飲食 | -100 | 源帳戶：羊羊（bId：1）
		 * －2025-07-01 | 收入 | +100 | 源帳戶：羊羊（bId：1）
		 */
		return sb.toString();
	}
	
	private String generateIncomeOutlayStatisticsQuery(List<StatisticsIncomeAndOutlayWithBalanceInfoVO> stList)
	{
		StringBuilder sb = new StringBuilder();
		if(stList.isEmpty())
		{
			logger.debug("沒有統計資料");
			return "＜查無資料＞";
		}
		var st = stList.get(0);
		sb.append(String.format("統計日期：%d 年 %d 月\n", 
				st.year(), 
				st.month()
				));
		for(var info : st.incomeAndOutlayInfoVOList())
			sb.append(String.format("－帳戶：%s（bId：%d） | 支出：%d | 收入：%d\n", 
					info.balanceInfo().name(), 
					info.balanceInfo().id(), 
					info.outlay(), 
					info.income()
					));
		/*
		 * Ex.
		 * 統計日期：2025 年 7 月
		 * －帳戶：羊羊（bId：1） | 支出：100 | 收入：100
		 */
		return sb.toString();
	}
	
	private String generatePaymentTypeStatisticsQuery(List<StatisticsPaymentTypeVO> stList)
	{
		StringBuilder sb = new StringBuilder();
		if(stList.isEmpty())
			return "＜查無資料＞";
		
		var st = stList.get(0);
		sb.append(String.format("統計日期：%d 年 %d 月\n", 
				st.year(), 
				st.month()
				));
		for(var info : st.paymentInfo())
			sb.append(String.format("－%s｜%d\n", 
					info.type(), 
					info.totalAmount()
					));
		/*
		 * Ex.
		 * 統計日期：2025 年 7 月
		 * －飲食｜100
		 * －收入｜100
		 */
		return sb.toString();
	}

	@Override
	public AIAnalysisResponse getAnalysis(GetAnalysisRequest req) 
	{
		User currentUser = loginService.getData();
		if(currentUser == null)
			return new AIAnalysisResponse(ResponseMessages.PLEASE_LOGIN_FIRST);
		
		var fromDate = req.from();
		var toDate = req.to();
		if(fromDate.year() == null || fromDate.month() == null)
			return new AIAnalysisResponse(ResponseMessages.DATE_REQUEST_ERROR);
					
		if(toDate.year() == null || toDate.month() == null)
			return new AIAnalysisResponse(ResponseMessages.DATE_REQUEST_ERROR);
		
		final int fromYear = fromDate.year();
		final int fromMonth = fromDate.month();
		final int toYear = toDate.year();
		final int toMonth = toDate.month();
		LocalDate from = LocalDate.of(fromYear, fromMonth, 1);
		LocalDate to = LocalDate.of(toYear, toMonth, 1);
		if(fromMonth == 0 || toMonth == 0)
			return new AIAnalysisResponse(ResponseMessages.DATE_REQUEST_ERROR);
		
		if(from.isAfter(to))
			return new AIAnalysisResponse(ResponseMessages.DATE_REQUEST_ERROR);
		
		// 取得登入者在指定日期區間的所有分析資料
		List<AIQueryLogs> logs = aiQueryLogsDao.findByYearRange(currentUser.getAccount(), fromYear, toYear)
				.stream()
				.filter(t -> {
					LocalDate analysisDate = LocalDate.of(t.getYear(), t.getMonth(), 1);
					return !analysisDate.isBefore(from) && !analysisDate.isAfter(to);
				})
				.toList();
		List<AIAnalysisVO> result = logs.stream().map(t -> t.toVO()).collect(Collectors.toCollection(ArrayList::new));
		var looper = from;
		while(!looper.isAfter(to))
		{
			int lpy = looper.getYear();
			int lpm = looper.getMonthValue();
			if(!result.stream().anyMatch(t -> t.year() == lpy && t.month() == lpm))
				result.add(new AIAnalysisVO(lpy, lpm, ""));
			
			looper = looper.plusMonths(1);
		}
		result.sort(Comparator
				.comparing(AIAnalysisVO::year)
				.thenComparing(AIAnalysisVO::month)
				);
		return new AIAnalysisResponse(ResponseMessages.SUCCESS, result);
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, Object> variablesFactory(
			String balances, 
			String previousPayments, 
			String previousIncomeOutlayStatistics, 
			String previousPaymentTypeStatistics, 
			String payments, 
			String incomeOutlayStatistics, 
			String paymentTypeStatistics
			)
	{
		Set<Entry<String, Object>> entrySet = new HashSet<>();
		entrySet.add(Map.entry("balances", balances));
		entrySet.add(Map.entry("previousPayments", previousPayments));
		entrySet.add(Map.entry("previousIncomeOutlayStatistics", previousIncomeOutlayStatistics));
		entrySet.add(Map.entry("previousPaymentTypeStatistics", previousPaymentTypeStatistics));
		entrySet.add(Map.entry("payments", payments));
		entrySet.add(Map.entry("incomeOutlayStatistics", incomeOutlayStatistics));
		entrySet.add(Map.entry("paymentTypeStatistics", paymentTypeStatistics));
		return Map.ofEntries(entrySet.toArray(Entry[]::new));
	}

}
