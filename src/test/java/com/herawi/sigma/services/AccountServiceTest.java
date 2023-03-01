package com.herawi.sigma.services;

import com.herawi.sigma.constants.Gender;
import com.herawi.sigma.dto.AccountRegistrationRequest;
import com.herawi.sigma.filters.AccountRegistrationRequestFilter;
import com.herawi.sigma.models.Account;
import com.herawi.sigma.repositories.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private FileStorageService fileStorageService;
    @Mock
    HttpServletRequest httpServletRequest;
    private AccountService underTest;
    private Account account;
    private AccountRegistrationRequest request;

    @BeforeEach
    void setUp() {
        underTest = new AccountService(accountRepository, bCryptPasswordEncoder, fileStorageService);
        ArgumentCaptor<MultipartFile> captor = ArgumentCaptor.forClass(MultipartFile.class);
        request = new AccountRegistrationRequest(
                "ali",
                "herawi",
                "aliherawi@gmail.com",
                "123456",
                "2000-02-09",
                Gender.MALE,
                captor.capture()
        );
        account = new Account();
        account.setName("ali");
        account.setLastName("herawi");
        account.setEmail("aliherawi@gmail.com");
        account.setUserName("aliherawi");
        account.setGender(Gender.MALE);
        account.setPassword("123456");
    }

    /* test if there is an account with the provided email*/
    @Test
    void loadUserByUsernameIfTheAccountIsAvailable() {
        //given
        String email = account.getEmail();

        //when
        when(accountRepository.findByEmail(email)).thenReturn(account);

        //then
        assertEquals(underTest.loadUserByUsername(email).getUsername(), email);
        verify(accountRepository).findByEmail(email);

    }
    /* test if there is an account with the provided email*/
    @Test
    void loadUserByUsernameIfTheAccountIsNotAvailable() {
        //given
        String email = account.getEmail();

        //when
        when(accountRepository.findByEmail(email)).thenReturn(null);

        //then
        assertThrows(UsernameNotFoundException.class, () -> underTest.loadUserByUsername(email));
        verify(accountRepository).findByEmail(email);

    }
    /* test if account created and saved successfully and email has not taken already*/
    @Test
    void addAccountIfEmailHasNotTakenAlready() throws Exception {
        //when
        ArgumentCaptor<Account> argumentCaptor = ArgumentCaptor.forClass(Account.class);
        when(accountRepository.save(account)).thenReturn(account);
        underTest.addAccount(request);
        //then
        verify(accountRepository).existsAccountByEmail(request.getEmail());
        verify(accountRepository).save(argumentCaptor.capture());
    }
    /* test if account created and saved successfully and email has already taken */
    @Test
    void addAccountIfEmailHasTakenAlready() throws Exception {
        //when
        when(accountRepository.existsAccountByEmail(account.getEmail())).thenReturn(true);
        underTest.addAccount(request);
        //then
        verify(accountRepository).existsAccountByEmail(request.getEmail());
    }
    /* test if account is already exist and update it*/
    @Test
    void updateAccountIfAccountIsAvailable() throws Exception {
        //given
       ArgumentCaptor<Account> argumentCaptor =
                ArgumentCaptor.forClass(Account.class);
        //when
        String email =  account.getEmail();
        when(accountRepository.existsAccountByEmail(email)).thenReturn(true);
        when(accountRepository.findByEmail(email)).thenReturn(account);
        underTest.updateAccount(httpServletRequest, request);

        //then
        verify(accountRepository).existsAccountByEmail(email);
        verify(accountRepository).findByEmail(email);
        verify(accountRepository).save(argumentCaptor.capture());
    }

    /* test if account is not already exist and update it*/
    @Test
    void updateAccountIfAccountIsNotAvailable() throws Exception {
        //given
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9." +
                "eyJzdWIiOiJhbGloZXJhd2lAZ21haWwuY29tIiwicm9sZXMiOltdLCJleHAiOjE2NzgzMjAxNjB9." +
                "iKsyy0IhHva5DA2PFRxISjRtS2rKZd6eEEx6bli4zcQ";
        //when
        String email =  account.getEmail();
        when(accountRepository.existsAccountByEmail(email)).thenReturn(false);
        when(httpServletRequest.getHeader("Authorization")).thenReturn(token);

        //then
        assertThrows(Exception.class,
                () -> underTest.updateAccount(httpServletRequest, request));
        verify(accountRepository).existsAccountByEmail(email);
    }
    /* test if accountRegistrationRequest is null or empty (the request body is empty) */
    @Test
    void updateAccountIfAccountRegistrationRequestIsNull() throws Exception {

        //when
        request = null;

        //then
        assertFalse(underTest.updateAccount(httpServletRequest, null));
    }

    @Test
    void deleteAccount() {
    }

    @Test
    void getAccount() {
    }

    @Test
    void getAllAccount() {
    }

    @Test
    void testGetAccount() {
    }

    @Test
    void getAccountByUserName() {
    }

    @Test
    void getAccountWithDetails() {
    }

    @Test
    void isAccountExistByEmail() {
    }

    @Test
    void getAllConnections() {
    }

    @Test
    void addAsConnection() {
    }

    @Test
    void isFriend() {
    }

    @Test
    void getAllFriends() {
    }
}