package com.herawi.sigma.repositories;

import com.herawi.sigma.dto.ConnectionRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface ConnectionRequestRepository extends JpaRepository<ConnectionRequest, Long> {
    Collection<ConnectionRequest> findAllByRequestReceiverId(long receiverId);

}
