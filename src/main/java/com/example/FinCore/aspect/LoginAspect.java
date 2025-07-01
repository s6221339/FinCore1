package com.example.FinCore.aspect;

import java.util.HashMap;
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

import com.example.FinCore.constants.ResponseMessages;
import com.example.FinCore.vo.response.BasicResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpSession;

@Component
@Aspect
public class LoginAspect 
{
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Pointcut("execution (public * com.example.FinCore.controller.*.*(..)) "
			+ "&& !execution (public * com.example.FinCore.controller.*.login(..))")
	public void pointcut()
	{
		
	}
	
	@Around("pointcut() && args(requestObj, ..)")
	public Object around(ProceedingJoinPoint pjp, Object requestObj) throws Throwable
	{
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpSession session = attributes.getRequest().getSession();
		var res = loginCheck(session, requestObj);
		if(res != null)
			return res;
		return pjp.proceed();
	}
	
	@SuppressWarnings({ "unchecked" })
	private BasicResponse loginCheck(HttpSession session, Object requestObj)
	{
		final ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> requestMap = new HashMap<>();
		try {
			requestMap = mapper.convertValue(requestObj, Map.class);
		}
		catch (IllegalArgumentException e) {
			logger.warn("存在無法被序列化的 requestObj，跳過此次驗證");
			return null;
		}
		String account = (String) requestMap.get("account");
		String sessionAccount = (String) session.getAttribute("account");
		String sessionId = session.getId();
		System.out.println("account：" + account + ", sessionAccount：" + sessionAccount + ", sessionId：" + sessionId);
		if(sessionId == null || sessionAccount == null)
			return new BasicResponse(ResponseMessages.PLEASE_LOGIN_FIRST);
		
		if((StringUtils.hasText(account) && !sessionAccount.equals(account)) || !sessionId.equals(session.getId()))
			return new BasicResponse(ResponseMessages.LOGIN_INFO_NOT_SYNC);
		return null;
	}

}
