package com.herawi.sigma;

import com.herawi.sigma.model.Account;
import com.herawi.sigma.service.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDate;

@SpringBootApplication

public class SigmaApplication {


	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoderBean() {
		return new BCryptPasswordEncoder();
	}

	public static void main(String[] args) {
		SpringApplication.run(SigmaApplication.class, args);
	}

//	@Bean
//	CommandLineRunner run(AccountService accountService){
//
//		return args -> {
//			Account account = new Account(
//					1L, "Ali", "Herawi", LocalDate.of(2000,3,12),
//					true, "aliherawi7@gmail.com","12345","0797608705",
//					"aliherawi7",false, 0, null, "Afg",
//					'M'
//			);
//			File file1 = new File("src/main/resources/templates/images/ProfileImages/user1.jpg");
//			FileInputStream fis = new FileInputStream(file1);
//			byte[] file1Bytes = new byte[(int) file1.length()];
//			accountService.addAccount(account, file1Bytes);
//		};
//	}



}
