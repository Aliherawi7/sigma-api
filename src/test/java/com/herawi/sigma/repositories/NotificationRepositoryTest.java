package com.herawi.sigma.repositories;

import com.herawi.sigma.models.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class NotificationRepositoryTest {

    @Autowired
    private NotificationRepository underTest;
    private Notification notification;

    @BeforeEach
    void setUp() {
        notification = new Notification();
        notification.setUserName("alex_parker");
        notification.setMessage("You have a new friend request");
        notification.setTitle("friend Request");
    }

    @Test
    void findAllByUserName() {
        //given
        underTest.save(notification);

        //when
        String userName = notification.getUserName();

        //then
        assertTrue(underTest.findAllByUserName(userName)
                .stream()
                .allMatch(item -> item.getUserName().equalsIgnoreCase(userName))
        );
    }

    @Test
    void findAllBySeenFalseAndUserName() {
        // given
        notification.setSeen(false);
        underTest.save(notification);

        //when

        String userName = notification.getUserName();
        boolean isSeen = notification.isSeen();

        //then
        assertTrue(underTest.findAllBySeenFalseAndUserName(userName)
                .stream()
                .allMatch(item ->
                        item.isSeen() == isSeen && item.getUserName().equalsIgnoreCase(userName))
        );
    }
}