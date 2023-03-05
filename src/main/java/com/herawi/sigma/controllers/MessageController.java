package com.herawi.sigma.controllers;

import com.herawi.sigma.models.Message;
import com.herawi.sigma.services.MessageService;
import com.herawi.sigma.utils.JWTTools;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

@RestController
@RequestMapping("api/v1/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService){
        this.messageService = messageService;
    }

    @GetMapping
    public ResponseEntity<?> getAllMessages(HttpServletRequest request){
        String userName = JWTTools.getUsernameByJWT(request);
        Collection<Message> messages = messageService.getAllByReceiverUsername(userName);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/{userName}")
    public ResponseEntity<?> getAllMessagesByUsername(HttpServletRequest request, @PathVariable("userName") String userName){
        String currentUser = JWTTools.getUsernameByJWT(request);
        Collection<Message> messages = messageService
                .getAllMessageBySenderUsernameAndReceiverUsername(userName, currentUser);
        return ResponseEntity.ok(
        messages.stream().sorted());
    }


}
