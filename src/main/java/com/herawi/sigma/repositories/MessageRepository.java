package com.herawi.sigma.repositories;

import com.herawi.sigma.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Collection;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Collection<Message> findAllBySenderUsername(String senderUsername);
    Collection<Message> findAllByReceiverUsername(String receiverUsername);
    Collection<Message> findAllBySenderUsernameAndReceiverUsername(String senderUsername, String receiverUsername);
}
