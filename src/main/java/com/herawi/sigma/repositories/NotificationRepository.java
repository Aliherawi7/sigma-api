package com.herawi.sigma.repositories;

import com.herawi.sigma.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Collection;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Collection<Notification> findAllByUserName(String userName);
    Collection<Notification> findAllBySeenFalseAndUserName(String userName);
}
