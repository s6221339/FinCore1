package com.example.FinCore.aspect;

import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.FinCore.constants.ConstantsMessage;
import com.example.FinCore.constants.ResponseMessages;
import com.example.FinCore.vo.response.BasicResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
@Aspect
public class LoginAspect 
{
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Pointcut("!execution (public * com.example.FinCore.controller.*.login(..)) "
			+ "&& !execution (public * com.example.FinCore.controller.*.register(..)) ")
	public void loginPointcut()
	{
		
	}
	
	@Pointcut("loginPointcut() && "
			+ "!execution (public * com.example.FinCore.controller.*.getNameByAccount(..))")
	public void accountCheckPointcut()
	{
		
	}
	
	@Pointcut("execution (public * com.example.FinCore.controller.*.*(..)) ")
	public void pointcut()
	{
		
	}
	
	@Around("pointcut() && loginPointcut()")
	public Object loginCheckAround(ProceedingJoinPoint pjp) throws Throwable
	{
		HttpSession session = getSession();
		var res = loginCheck(session);
		if(res != null)
			return res;
		return pjp.proceed();
	}
	
	@Around("pointcut() && accountCheckPointcut()")
	public Object accountCheckAround(ProceedingJoinPoint pjp) throws Throwable
	{
		HttpSession session = getSession();
		Object[] args = pjp.getArgs();
		String account = extractAccountFromArgs(args);
		var res = accountCheck(session, account);
		if(res != null)
			return res;
		return pjp.proceed();
	}
	
	private HttpSession getSession()
	{
		return ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes())
				.getRequest().getSession();
	}
	
	@SuppressWarnings("unchecked")
	private String extractAccountFromArgs(Object[] args)
	{
		for(Object arg : args)
		{
			if(arg instanceof String account && account.matches(ConstantsMessage.EMAIL_PATTERN))
				return account;
			
			if(!(arg instanceof HttpServletRequest) && !(arg instanceof HttpServletResponse))
			{
				final ObjectMapper mapper = new ObjectMapper();
				try {
					Map<String, Object> requestMap = mapper.convertValue(arg, Map.class);
					if(requestMap.containsKey("account"))
						return (String) requestMap.get("account");
				}
				catch (Exception e) {
					logger.warn("轉換物件時發生例外事件。");
					return null;
				}
			}
		}
		return null;
	}

	private BasicResponse loginCheck(HttpSession session)
	{
		String sessionAccount = (String) session.getAttribute("account");
		String sessionId = session.getId();
//		System.out.println("account：" + account + ", sessionAccount：" + sessionAccount + ", sessionId：" + sessionId);
		
//		確認登入狀態
		if(sessionId == null || sessionAccount == null)
			return new BasicResponse(ResponseMessages.PLEASE_LOGIN_FIRST);
		return null;
	}
	
	private BasicResponse accountCheck(HttpSession session, String account)
	{
		var res = loginCheck(session);
		if(res != null)
			return res;
		
		String sessionAccount = (String) session.getAttribute("account");
		String sessionId = session.getId();
//		System.out.println("account：" + account + ", sessionAccount：" + sessionAccount + ", sessionId：" + sessionId);
		
		
//		如果API有帳號操作，比對登入帳號與操作帳號是否一致
		if((StringUtils.hasText(account) && !sessionAccount.equals(account)) || !sessionId.equals(session.getId()))
			return new BasicResponse(ResponseMessages.LOGIN_INFO_NOT_SYNC);
		return null;
	}

}
