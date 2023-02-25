package com.herawi.sigma.repositories;

import com.herawi.sigma.models.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    Collection<FriendRequest> findAllByRequestReceiverId(long receiverId);
    Collection<FriendRequest> findAllByRequestSenderId(long senderId);

}
