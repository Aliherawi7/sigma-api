package com.herawi.sigma.controllers;

import com.herawi.sigma.dto.AccountDTO;
import com.herawi.sigma.dto.AccountRegistrationRequest;
import com.herawi.sigma.services.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping( value = "api/v1/accounts", produces = {MediaType.APPLICATION_JSON_VALUE})
public class AccountController {

    private final AccountService accountService;
    Logger log = LoggerFactory.getLogger(AccountController.class);
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
        return ResponseEntity.ok(accountDTO);
    }

    @GetMapping("email/{email}")
    public ResponseEntity<?> getUsers(@PathVariable String email){
        AccountDTO accountInfo = accountService.getAccount(email);
        return ResponseEntity.ok().body(accountInfo);
    }
    @GetMapping("/pagination/{offset}/{pageSize}")
    public ResponseEntity<?> getAll(@PathVariable int offset, @PathVariable int pageSize){
        return ResponseEntity.ok().body(accountService.getAllAccountWithPagination(offset, pageSize));
    }

    @GetMapping("{userName}/friends/pagination/{offset}/{pageSize}")
    public ResponseEntity<?> getAllFriends(
            @PathVariable String userName,
            @PathVariable int offset,
            @PathVariable int pageSize){
        return ResponseEntity.ok().body(accountService.getAllFriendsWithPagination(userName, offset, pageSize));
    }

    @GetMapping("/search/{keyword}/pagination/{offset}/{pageSize}")
    public ResponseEntity<List<AccountDTO>> getAccountByNameAndLastName(
            @PathVariable String keyword,
            @PathVariable int offset,
            @PathVariable int pageSize){

        log.info(keyword, offset, pageSize);
        return ResponseEntity.ok(accountService.getAccountDOTsByName(keyword, offset, pageSize));
    }

}
