package com.example.FinCore.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.FinCore.constants.ResponseMessages;
import com.example.FinCore.vo.response.BasicResponse;

import jakarta.servlet.http.HttpServletRequest;

@Component
@Aspect
public class TestAPIAccessAspect 
{
	
	private final static String TEST_HEADER_NAME = "X-TEST-ACCESS";
	private final static String TEST_TOKEN = "token_WorkLog";
	
	@Pointcut("@annotation(com.example.FinCore.annotation.TestAPI)")
	public void pointcut()
	{
		
	}
	
	@Around(value = "pointcut()")
	public Object checkTestAccess(ProceedingJoinPoint pjp) throws Throwable
	{
		ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if(attrs != null)
		{
			HttpServletRequest req = attrs.getRequest();
			String origin = req.getHeader("Origin");
			String token = req.getHeader(TEST_HEADER_NAME);
			if(origin != null && !TEST_TOKEN.equals(token))
				return new BasicResponse(ResponseMessages.TEST_API_ONLY);
		}
		return pjp.proceed();
	}
	
}
