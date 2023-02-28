package com.herawi.sigma.repositories;

import com.herawi.sigma.models.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AccountRepositoryTest {

    @Autowired
    private AccountRepository underTest;
    private Account account;

    @BeforeEach
    void setUp(){
        account = new Account();
        account.setName("alex");
        account.setLastName("parker");
        account.setEmail("alexparker@gmail.com");
        account.setUserName("alexparker");
    }

    @Test
    void findByEmail() {
        //given
        underTest.save(account);

        //when
        String email = "alexparker@gmail.com";

        //then
        assertEquals(email, underTest.findByEmail(email).getEmail());
    }


    @Test
    void findByUserName() {
        //given
        underTest.save(account);

        //when
        String userName = account.getUserName();

        //then
        assertEquals(userName, underTest.findByUserName(userName).getUserName());
    }

    @Test
    void findAllByNameOrLastName() {
        //given
        underTest.save(account);

        //when
        String name = account.getName();
        String lastName = account.getLastName();

        //then
        assertEquals(name, underTest.findAllByNameOrLastName(name, lastName).stream().findFirst().get().getName());
    }

    @Test
    void existsAccountByEmail() {
        //given
        String email = account.getEmail();
        underTest.save(account);

        //when
        boolean isExist = underTest.existsAccountByEmail(email);

        //then
        assertTrue(isExist);
    }

    @Test
    void existsByEmailOrUserName() {
        //given
        String userName = account.getUserName();
        String email = account.getEmail();
        underTest.save(account);

        //when
        boolean isExist = underTest.existsByEmailOrUserName(email, userName);

        //then
        assertTrue(isExist);
    }
}