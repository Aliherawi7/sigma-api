package com.herawi.sigma.services;

import com.herawi.sigma.models.FriendRequest;
import com.herawi.sigma.repositories.FriendRequestRepository;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;

@Service
public class FriendRequestService {

    private final FriendRequestRepository connectionRequestRepository;

    public FriendRequestService(FriendRequestRepository connectionRequestRepository) {
        this.connectionRequestRepository = connectionRequestRepository;
    }

    public FriendRequest addFriendRequest(FriendRequest friendRequest){
        if(friendRequest.getRequestReceiverId() == friendRequest.getRequestSenderId())
            throw new InvalidParameterException("Invalid receiverId");

        return connectionRequestRepository.save(friendRequest);
    }

}
