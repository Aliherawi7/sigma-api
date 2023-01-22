package com.herawi.sigma.controller;

import com.herawi.sigma.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/person")
public class PersonController {

    private final AccountService accountService;

    public PersonController(AccountService accountService) {
        this.accountService = accountService;
    }

    public ResponseEntity<?> getUser(HttpServletRequest request){

        return ResponseEntity.noContent().build();
    }
}
