package com.herawi.sigma.services;


import antlr.debug.MessageAdapter;
import com.herawi.sigma.dto.MessageRequestDO;
import com.herawi.sigma.exceptions.AccountNotFoundException;
import com.herawi.sigma.models.Account;
import com.herawi.sigma.models.Message;
import com.herawi.sigma.repositories.MessageRepository;
import com.herawi.sigma.utils.JWTTools;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final AccountService accountService;

    public MessageService(MessageRepository messageRepository, AccountService accountService) {
        this.messageRepository = messageRepository;
        this.accountService = accountService;
    }

    public Message addMessage(HttpServletRequest request, MessageRequestDO messageRequestDO){
        String email = JWTTools.getUserEmailByJWT(request);
        Account account = accountService.getAccountWithDetails(email);
        if(account == null || !accountService.isAccountExistByUsername(messageRequestDO.getUsername())){
            throw new AccountNotFoundException("account not found with provided username");
        }
        Message message = new Message();
        message.setSenderUsername(account.getUserName());
        message.setReceiverUsername(messageRequestDO.getUsername());
        message.setText(messageRequestDO.getMessage());
        return messageRepository.save(message);
    }

    public Message addMessage(Message message){
        if(!accountService.isAccountExistByUsername(message.getSenderUsername())  ||
           !accountService.isAccountExistByUsername(message.getReceiverUsername())){
            throw new AccountNotFoundException("account not found with provided username");
        }
        return messageRepository.save(message);
    }

    public Collection<Message> getAllMessageBySenderUsernameAndReceiverUsername(String senderUsername, String receiverUsername){
        return messageRepository.findAllBySenderUsernameAndReceiverUsername(senderUsername, receiverUsername);
    }

    public Collection<Message> getAllBySenderUsername(String senderUsername){
        return messageRepository.findAllBySenderUsername(senderUsername);
    }

    public Collection<Message> getAllByReceiverUsername(String receiverName){
        return messageRepository.findAllByReceiverUsername(receiverName)
                .stream()
                .sorted()
                .collect(Collectors.toList());
    }

}
