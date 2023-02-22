package com.herawi.sigma;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.herawi.sigma.dto.AccountRegistrationRequest;
import com.herawi.sigma.services.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

@SpringBootApplication

public class SigmaApplication {


	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoderBean() {
		return new BCryptPasswordEncoder();
	}

	public static void main(String[] args) {
		SpringApplication.run(SigmaApplication.class, args);
	}

	@Bean
	CommandLineRunner run(AccountService accountService){

		return args -> {
			ObjectMapper objectMapper = new ObjectMapper();
			ArrayList<AccountRegistrationRequest> accounts = new ArrayList<>();
			try {
				URL jsonUrl = Thread.currentThread().getContextClassLoader().getResource("static\\accounts.json");
				System.out.println(jsonUrl);
				AccountRegistrationRequest[] array = objectMapper.readValue(jsonUrl, AccountRegistrationRequest[].class);
				accounts.addAll(Arrays.asList(array));
				accounts.forEach(item -> {
					try {
						accountService.addAccount(item);
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	}



}
