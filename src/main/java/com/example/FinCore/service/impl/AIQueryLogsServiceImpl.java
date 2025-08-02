package com.example.FinCore.service.impl;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import com.example.FinCore.service.itfc.AIQueryLogsService;
import com.example.FinCore.service.itfc.AIService;
import com.example.FinCore.service.itfc.PaymentService;
import com.example.FinCore.vo.StatisticsIncomeAndOutlayWithBalanceInfoVO;
import com.example.FinCore.vo.request.AICallRequest;
import com.example.FinCore.vo.request.AICreateRequest;
import com.example.FinCore.vo.request.StatisticsRequest;
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
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	@Transactional(rollbackOn = Exception.class)
	public BasicResponse create(AICreateRequest req) throws Exception 
	{
		final int year = req.year();
		final int month = req.month();
		Boolean forcedWrite = req.forcedWrite();
		
		List<String> allAccounts = userDao.findAll().stream().map(t -> t.getAccount()).toList();
		if(forcedWrite == null)
			forcedWrite = Boolean.FALSE;
		
		for(String account : allAccounts)
		{
			var res = call(new AICallRequest(account, year, month, forcedWrite));
			if(forcedWrite)
			{
				if(res.getCode() != 200)
				{
					logger.error("帳號「" + account + "」的 AI 分析失敗！原因：" + res.getCode() + "（" + res.getMessage() + "）");
					aiQueryLogsDao.save(new AIQueryLogs(account, year, month, null, LocalDate.now()));
				}
				else
					aiQueryLogsDao.save(new AIQueryLogs(account, year, month, res.getText(), LocalDate.now()));
			}
			else
				logger.warn("帳號「" + account + "」無法寫入分析資料，因為資料已分析。如有需要請啟用 forcedWrite 來強制寫入資料。");
			
		}
		return new BasicResponse(ResponseMessages.SUCCESS);
	}

	@Override
	public AICallbackResponse call(AICallRequest req) throws Exception 
	{
		final String account = req.account();
		final int year = req.year();
		final int month = req.month();
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
		List<Payment> payments = paymentDao.getPaymentListByBalanceIdList(balances.stream()
					.map(t -> t.getBalanceId())
					.toList()
				).stream()
				.filter(t -> !t.isDeleted() && t.isOnTime(year, month))
				.toList();
		
		if(payments.isEmpty())
			return new AICallbackResponse(ResponseMessages.NO_PAYMENT_DATA);
		
		Map<Balance, List<Payment>> bMap = new HashMap<>();
		for(Balance b : balances)
			bMap.put(b, payments.stream().filter(t -> t.getBalanceId() == b.getBalanceId()).toList());
		
		var statisticsList = paymentService.statisticsIncomeAndOutlayWithAllBalance(
				new StatisticsRequest(account, year, month))
				.getStatisticsList();
		
		String bq = generateBalanceQuery(balances);
		String pq = generatePaymentsQuery(bMap);
		String stq = generateStatisticsQuery(statisticsList);
		String response = aiService.templateFromFileCode(bq, pq, stq);
		return new AICallbackResponse(ResponseMessages.SUCCESS, response);
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
	
	private String generateStatisticsQuery(List<StatisticsIncomeAndOutlayWithBalanceInfoVO> stList)
	throws Exception
	{
		StringBuilder sb = new StringBuilder();
		if(stList.size() < 1)
			throw new Exception("擷取統計資料失敗");
		
		var st = stList.get(0);
		sb.append(String.format("統計日期：%d 年 %d 月\n", 
				st.year(), 
				st.month()
				));
		for(var info : st.incomeAndOutlayInfoVOList())
		{
			sb.append(String.format("帳戶：%s（bId：%d） | 支出：%d | 收入：%d\n", 
					info.balanceInfo().name(), 
					info.balanceInfo().id(), 
					info.outlay(), 
					info.income()
					));
		}
		/*
		 * Ex.
		 * 統計日期：2025 年 7 月
		 * 帳戶：羊羊（bId：1） | 支出：100 | 收入：100
		 */
		return sb.toString();
	}

}
