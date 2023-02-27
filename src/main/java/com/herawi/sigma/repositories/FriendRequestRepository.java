package com.herawi.sigma.repositories;

import com.herawi.sigma.models.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    Collection<FriendRequest> findAllByRequestReceiverUserName(String receiverUserName);
    Collection<FriendRequest> findAllByRequestSenderUserName(String senderUserName);
    FriendRequest findByRequestReceiverUserNameAndRequestSenderUserName(String receiverUserName, String senderUserName);
    void deleteByRequestSenderUserNameAndRequestReceiverUserName(String requestSender, String requestReceiver);
}
