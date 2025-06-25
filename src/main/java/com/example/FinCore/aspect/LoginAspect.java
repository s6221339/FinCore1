package com.example.FinCore.aspect;

import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
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
	
	@Pointcut("execute (public * com.example.FinCore.Controller.*.*(..)) "
			+ "&& !execute (public * com.example.FinCore.Controller.*.login(..)")
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
	
	@SuppressWarnings("unchecked")
	private BasicResponse loginCheck(HttpSession session, Object requestObj)
	{
		final ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> requestMap = mapper.convertValue(requestObj, Map.class);
		String account = (String) requestMap.get("account");
		String sessionAccount = (String) session.getAttribute("account");
		String sessionId = session.getId();
		if(sessionId == null || sessionAccount == null || 
				!sessionId.equals(session.getId()) || !sessionAccount.equals(account))
			return new BasicResponse(ResponseMessages.PLEASE_LOGIN_FIRST);
		return null;
	}

}
