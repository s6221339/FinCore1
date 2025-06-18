package com.example.FinCore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

//因為有使用 spring-boot-starter-security 此依賴，要排除預設的基本安全性設定(帳密登入驗證)
//排除帳密登入驗證就是加上 exclude = SecurityAutoConfiguration.class
//等號後面若有多個 class 時，就要用大括號，一個時大括號可有可無
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class FinCoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinCoreApplication.class, args);
	}
	
	//Transfer 建立紀錄時要自動寫入帳款
	//Payment API 解除刪除帳款（recovery）
	//Family Entity 新增將成員轉為 String 的方法


}
