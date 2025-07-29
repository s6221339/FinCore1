package com.example.FinCore.service.itfc;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.FinCore.dao.UserDao;
import com.example.FinCore.entity.User;

import jakarta.servlet.http.HttpSession;

@Service
public final class LoginService 
{
	
	@Autowired
	private UserDao userDao;
	
	/**
	 * 取得登入者的資料。
	 * @return 如果登入資料有效，返回該資料；未登入或登入資料不存在時返回 NULL
	 */
	public User getData()
	{
		HttpSession session = getSession();
		if(session.getId() == null)
			return null;
		
		String account = (String) session.getAttribute("account");
		Optional<User> user = userDao.findById(account);
		return user.isPresent() ? user.get() : null;
	}
	
	/**
	 * 取得當前的會話物件資料。
	 * @return 當前的會話物件資料
	 */
	public HttpSession getSession()
	{
		return ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes())
				.getRequest().getSession();
	}
	
	
}
