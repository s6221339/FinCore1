package com.example.FinCore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class FinCoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinCoreApplication.class, args);
	}

}
