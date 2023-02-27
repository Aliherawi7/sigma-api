package com.herawi.sigma.controllers;

import com.herawi.sigma.dto.AccountDTO;
import com.herawi.sigma.dto.AccountRegistrationRequest;
import com.herawi.sigma.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping( value = "api/accounts", produces = {MediaType.APPLICATION_JSON_VALUE})
public class AccountController {

    private final AccountService accountService;
    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping()
    public ResponseEntity<?> addAccount(@ModelAttribute AccountRegistrationRequest accountRegistrationRequest) throws Exception {
        return accountService.addAccount(accountRegistrationRequest);
    }

    @GetMapping
    public ResponseEntity<?> getUser(HttpServletRequest request){
        AccountDTO accountInfo = accountService.getAccount(request);
        if(accountInfo != null){
            return ResponseEntity.ok().body(accountInfo);
        }
        return ResponseEntity.noContent().build();
    }
    @GetMapping("userName/{userName}")
    public ResponseEntity<?> getAccountService(@PathVariable String userName) {
        AccountDTO accountDTO = accountService.getAccountByUserName(userName);
        if(accountDTO != null){
            return ResponseEntity.ok(accountDTO);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("email/{email}")
    public ResponseEntity<?> getUsers(@PathVariable String email){
        AccountDTO accountInfo = accountService.getAccount(email);
        if(accountInfo != null){
            return ResponseEntity.ok().body(accountInfo);
        }
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/all")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok().body(accountService.getAllAccount());
    }

    @GetMapping("{userName}/friends")
    public ResponseEntity<?> getAllFriends(@PathVariable String userName){
        return ResponseEntity.ok().body(accountService.getAllFriends(userName));
    }

}
