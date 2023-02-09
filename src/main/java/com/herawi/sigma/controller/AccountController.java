package com.herawi.sigma.controller;

import com.herawi.sigma.dto.AccountDTO;
import com.herawi.sigma.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public ResponseEntity<?> getUser(HttpServletRequest request){
        AccountDTO accountInfo = accountService.getAccount(request);
        if(accountInfo != null){
            return ResponseEntity.ok().body(accountInfo);
        }
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{email}")
    public ResponseEntity<?> getUsers(@PathVariable String email){
        AccountDTO accountInfo = accountService.getAccount(email);
        if(accountInfo != null){
            return ResponseEntity.ok().body(accountInfo);
        }
        return ResponseEntity.noContent().build();
    }

}
