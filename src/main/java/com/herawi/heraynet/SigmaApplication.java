package com.herawi.heraynet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication

public class HeraynetApplication {


	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoderBean() {
		return new BCryptPasswordEncoder();
	}

	public static void main(String[] args) {
		SpringApplication.run(HeraynetApplication.class, args);
	}



}
