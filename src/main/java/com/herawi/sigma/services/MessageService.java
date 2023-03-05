package com.herawi.sigma.services;


import com.herawi.sigma.models.Message;
import com.herawi.sigma.repositories.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }


    public void addMessage(Message message){
        messageRepository.save(message);
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
