package com.herawi.sigma.services;

import com.herawi.sigma.models.FriendRequest;
import com.herawi.sigma.repositories.FriendRequestRepository;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.Collection;

@Service
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;

    public FriendRequestService(FriendRequestRepository friendRequestRepository) {
        this.friendRequestRepository = friendRequestRepository;
    }

    /*
    * get all friend requests by senderId
    * */
    public Collection<FriendRequest> getAllFriendRequestsBySenderId(long senderId){
        return friendRequestRepository.findAllByRequestSenderId(senderId);
    }
    /*
    * get all friend requests by receiverId
    * */
    public Collection<FriendRequest> getAllFriendRequestsByReceiverId(long receiverId){
        return friendRequestRepository.findAllByRequestReceiverId(receiverId);
    }

    public FriendRequest addFriendRequest(FriendRequest friendRequest){
        if(friendRequest.getRequestReceiverId() == friendRequest.getRequestSenderId())
            throw new InvalidParameterException("Invalid receiverId");

        return friendRequestRepository.save(friendRequest);
    }

}
