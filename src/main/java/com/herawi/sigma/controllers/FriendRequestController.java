package com.herawi.sigma.controllers;

import com.auth0.jwt.JWT;
import com.herawi.sigma.models.Account;
import com.herawi.sigma.models.FriendRequest;
import com.herawi.sigma.services.AccountService;
import com.herawi.sigma.services.FriendRequestService;
import com.herawi.sigma.utils.JWTTools;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/friendRequests")
public class FriendRequestController {

    private final FriendRequestService friendRequestService;
    private final AccountService accountService;

    public FriendRequestController(FriendRequestService friendRequestService, AccountService accountService) {
        this.friendRequestService = friendRequestService;
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<?> sendFriendRequest(HttpServletRequest request, @RequestBody FriendRequest friendRequest){
        String email = JWTTools.getUserEmailByJWT(request);
        Account account = null;
        if(email != null){
            account = accountService.getAccountWithDetails(email);
        }
        if(account != null){
            friendRequest.setRequestSenderUserName(account.getUserName());
            friendRequestService.addFriendRequest(friendRequest);
            return new ResponseEntity<>(request, HttpStatus.CREATED);
        }
        return ResponseEntity.badRequest().build();
    }
    @PostMapping("accept")
    public ResponseEntity<?> acceptFriendRequest(HttpServletRequest request, @RequestBody FriendRequest friendRequest){
        boolean isFriend = accountService.getAllConnections(request)
                .stream()
                .anyMatch(friend -> friend.getUserName().equals((friendRequest.getRequestReceiverUserName())));
        if(!isFriend){
            accountService.addAsConnection(request, friendRequest.getRequestReceiverUserName());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().body("You are already friends together");
    }

    @GetMapping("allSent")
    public ResponseEntity<?> getAllFriendRequestsBySenderId(HttpServletRequest request){
        String email = JWTTools.getUserEmailByJWT(request);
        Account account = null;
        if(email != null){
             account = accountService.getAccountWithDetails(email);
        }
        if(account != null){
            return ResponseEntity.ok(friendRequestService.getAllFriendRequestsBySenderId(account.getId()));
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
            return ResponseEntity.ok(friendRequestService.getAllFriendRequestsByReceiverId(account.getId()));
        }
        return ResponseEntity.noContent().build();
    }

}
