package com.herawi.sigma.services;

import com.herawi.sigma.dto.ConnectionRequest;
import com.herawi.sigma.repositories.ConnectionRequestRepository;
import org.springframework.stereotype.Service;

@Service
public class ConnectionRequestService {

    private final ConnectionRequestRepository connectionRequestRepository;

    public ConnectionRequestService(ConnectionRequestRepository connectionRequestRepository) {
        this.connectionRequestRepository = connectionRequestRepository;
    }

    public ConnectionRequest addConnectionRequest(ConnectionRequest connectionRequest){
        if(connectionRequest.getRequestReceiverId() == connectionRequest.getRequestSenderId())
            return null;

        return connectionRequestRepository.save(connectionRequest);
    }

}
