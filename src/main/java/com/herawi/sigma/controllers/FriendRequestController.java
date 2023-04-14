package com.herawi.sigma.controllers;

import com.herawi.sigma.dto.FriendRequestRegisterationDTO;
import com.herawi.sigma.models.Account;
import com.herawi.sigma.models.FriendRequest;
import com.herawi.sigma.services.AccountService;
import com.herawi.sigma.services.FriendRequestService;
import com.herawi.sigma.utils.JWTTools;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1/friendRequests")
public class FriendRequestController {

    private final FriendRequestService friendRequestService;
    private final AccountService accountService;

    public FriendRequestController(FriendRequestService friendRequestService, AccountService accountService) {
        this.friendRequestService = friendRequestService;
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<FriendRequest> sendFriendRequest(HttpServletRequest request, @RequestBody FriendRequestRegisterationDTO friendRequestRegisterationDTO){
       return ResponseEntity
               .ok()
               .body(friendRequestService.addFriendRequest(request, friendRequestRegisterationDTO));
    }

    @PostMapping("accept")
    public ResponseEntity<?> acceptFriendRequest(HttpServletRequest request, @RequestBody FriendRequestRegisterationDTO friendRequestRegisterationDTO){
        friendRequestService.acceptFriendRequest(request, friendRequestRegisterationDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("reject")
    public ResponseEntity<?> rejectFriendRequest(HttpServletRequest request, @RequestBody FriendRequestRegisterationDTO friendRequestRegisterationDTO){
        String email = JWTTools.getUserEmailByJWT(request);
        Account account =null;
        if(email != null){
            account = accountService.getAccountWithDetails(email);
        }
        if(account != null){
            friendRequestService.rejectFriendRequest(account.getUserName(), friendRequestRegisterationDTO.getRequestSenderUserName());
            return ResponseEntity.ok("friend request rejected successfully");
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("allSent")
    public ResponseEntity<?> getAllFriendRequestsBySenderId(HttpServletRequest request){
        String email = JWTTools.getUserEmailByJWT(request);
        Account account = null;
        if(email != null){
             account = accountService.getAccountWithDetails(email);
        }
        if(account != null){
            return ResponseEntity.ok(friendRequestService.getAllFriendRequestsBySenderId(account.getUserName()));
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("allReceived")
    public ResponseEntity<?> getAllFriendRequestsByReceiverId(HttpServletRequest request){
        String email = JWTTools.getUserEmailByJWT(request);
        Account account = null;
        if(email != null){
          account = accountService.getAccountWithDetails(email);
        }
        if(account != null){
            return ResponseEntity.ok(friendRequestService.getAllFriendRequestsByReceiverId(account.getUserName()));
        }
        return ResponseEntity.noContent().build();
    }

}
