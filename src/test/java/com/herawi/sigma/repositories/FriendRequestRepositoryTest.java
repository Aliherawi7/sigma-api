package com.herawi.sigma.repositories;

import com.herawi.sigma.models.FriendRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class FriendRequestRepositoryTest {
    @Autowired
    private FriendRequestRepository underTest;
    private FriendRequest friendRequest;

    @BeforeEach
    void setUp() {
        friendRequest = new FriendRequest();
        friendRequest.setRequestReceiverUserName("alex_parker");
        friendRequest.setRequestSenderUserName("simon_rose");
    }

    @Test
    void findAllByRequestReceiverUserName() {
        //given
        underTest.save(friendRequest);

        //when
        String receiverUserName = friendRequest.getRequestReceiverUserName();

        //then
        assertTrue(underTest.findAllByRequestReceiverUserName(receiverUserName)
                .stream()
                .allMatch(item -> item.getRequestReceiverUserName()
                                .equalsIgnoreCase(receiverUserName))
                );
    }

    @Test
    void findAllByRequestSenderUserName() {
        //given
        underTest.save(friendRequest);

        //when
        String senderUserName = friendRequest.getRequestSenderUserName();

        //then
        assertTrue(underTest.findAllByRequestSenderUserName(senderUserName)
                .stream()
                .allMatch(item -> item.getRequestSenderUserName().equalsIgnoreCase(senderUserName))
        );
    }

    @Test
    void findByRequestReceiverUserNameAndRequestSenderUserName() {
        //given
        underTest.save(friendRequest);

        //when
        String senderUserName = friendRequest.getRequestSenderUserName();
        String receiverUserName = friendRequest.getRequestReceiverUserName();

        //then
        assertTrue(underTest
                .findByRequestReceiverUserNameAndRequestSenderUserName(receiverUserName, senderUserName)
                .getRequestSenderUserName().equalsIgnoreCase(senderUserName));

        assertTrue(underTest.findByRequestReceiverUserNameAndRequestSenderUserName(receiverUserName, senderUserName)
        .getRequestReceiverUserName().equalsIgnoreCase(receiverUserName));
    }

    @Test
    void deleteByRequestSenderUserNameAndRequestReceiverUserName() {
        //given
        underTest.save(friendRequest);

        //then
        String senderUserName = friendRequest.getRequestSenderUserName();
        String receiverUserName = friendRequest.getRequestReceiverUserName();

        //then
        underTest.deleteByRequestSenderUserNameAndRequestReceiverUserName(senderUserName, receiverUserName);
        assertNull(underTest.findByRequestReceiverUserNameAndRequestSenderUserName(receiverUserName, senderUserName));
    }
}