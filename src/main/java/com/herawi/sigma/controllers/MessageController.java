package com.herawi.sigma.controllers;

import com.herawi.sigma.dto.MessageRequestDO;
import com.herawi.sigma.models.Message;
import com.herawi.sigma.services.MessageService;
import com.herawi.sigma.utils.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

@RestController
@RequestMapping("api/v1/messages")
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    public ResponseEntity<?> getAllMessages(HttpServletRequest request){
        String userName = JWTTools.getUsernameByJWT(request);
        Collection<Message> messages = messageService.getAllByReceiverUsername(userName);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/{userName}")
    public ResponseEntity<?> getAllMessagesByUsername(HttpServletRequest request,
                                                      @PathVariable("userName") String userName){
        String currentUser = JWTTools.getUsernameByJWT(request);
        Collection<Message> messages = messageService
                .getAllMessageBySenderUsernameAndReceiverUsername(userName, currentUser);
        return ResponseEntity.ok(
        messages.stream().sorted());
    }

    @PostMapping
    public ResponseEntity<Message> sendMessageTo(HttpServletRequest request, @RequestBody MessageRequestDO messageRequestDO){
        return new ResponseEntity<>(
                messageService.addMessage(request, messageRequestDO),
                HttpStatus.CREATED);
    }






}
