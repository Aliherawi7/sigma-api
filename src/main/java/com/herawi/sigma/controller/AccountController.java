package com.herawi.sigma.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.herawi.sigma.dto.AccountDTO;
import com.herawi.sigma.dto.AccountRegistrationRequest;
import com.herawi.sigma.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/accounts")
public class AccountController {

    private final AccountService accountService;
    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping()
    public ResponseEntity<?> addAccount(@ModelAttribute AccountRegistrationRequest accountRegistrationRequest) throws Exception {
        if(accountService.addAccount(accountRegistrationRequest)){
            return new ResponseEntity<>("successfully saved", HttpStatus.CREATED);
        }else {
            return new ResponseEntity<>("invalid parameter", HttpStatus.BAD_REQUEST);
        }

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
    @GetMapping("all")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok().body(accountService.getAllAccount());
    }

}
