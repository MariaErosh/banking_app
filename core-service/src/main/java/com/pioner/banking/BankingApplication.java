package com.pioner.banking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableScheduling
@SpringBootApplication
public class BankingApplication {

	public static void main(String[] args) {
		String hashed = new BCryptPasswordEncoder().encode("admin123");
		System.out.println("Pass:" + hashed);
		SpringApplication.run(BankingApplication.class, args);
	}

}
