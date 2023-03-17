package com.herawi.sigma;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.herawi.sigma.dto.AccountDTO;
import com.herawi.sigma.dto.AccountRegistrationRequest;
import com.herawi.sigma.models.Account;
import com.herawi.sigma.models.Message;
import com.herawi.sigma.repositories.AccountRepository;
import com.herawi.sigma.services.AccountService;
import com.herawi.sigma.services.MessageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.TemporalType;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

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
	CommandLineRunner run(AccountService accountService, MessageService messageService){

		return args -> {
			ObjectMapper objectMapper = new ObjectMapper();
			ArrayList<AccountRegistrationRequest> accounts = new ArrayList<>();
			try {
				URL jsonUrl = Thread.currentThread().getContextClassLoader().getResource("static\\accounts.json");
				AccountRegistrationRequest[] array = objectMapper.readValue(jsonUrl, AccountRegistrationRequest[].class);
				accounts.addAll(Arrays.asList(array));
				accounts.forEach(item -> {
					try {
						accountService.addAccount(item);
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
				URL messagesJsonUrl = Thread.currentThread()
						.getContextClassLoader().getResource("static\\messages.json");
				Message[] messagesArray = objectMapper.readValue(messagesJsonUrl, Message[].class);
				AtomicInteger atomicInteger = new AtomicInteger(0);
				Arrays.asList(messagesArray).forEach(item -> {
					item.setDateTime(item.getDateTime().plusMinutes(atomicInteger.incrementAndGet()));
					messageService.addMessage(item);
				});

			} catch (Exception e) {
				e.printStackTrace();
			}
			List<AccountDTO> list = accountService.getAllAccount();
			list.forEach(item -> {

				for(int i = (int)(Math.random()*list.size()); i < list.size(); i++){
					AccountDTO account = accountService.getAccount(list.get(i).getEmail());
					accountService.addAsFriendById(item.getUserName(), account.getUserName());
				}
			});

		};
	}



}
