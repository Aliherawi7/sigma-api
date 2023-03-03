package com.herawi.sigma.services;

import com.herawi.sigma.constants.Gender;
import com.herawi.sigma.dto.AccountDTO;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    private String token;

    @BeforeEach
    void setUp() {
        underTest = new AccountService(accountRepository, bCryptPasswordEncoder, fileStorageService);
        request = new AccountRegistrationRequest(
                "ali",
                "herawi",
                "aliherawi@gmail.com",
                "123456",
                "2000-02-09",
                Gender.MALE,
                null
        );
        account = new Account();
        account.setName("ali");
        account.setLastName("herawi");
        account.setEmail("aliherawi@gmail.com");
        account.setUserName("aliherawi");
        account.setGender(Gender.MALE);
        account.setPassword("123456");
        token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9." +
                "eyJzdWIiOiJhbGloZXJhd2lAZ21haWwuY29tIiwicm9sZXMiOltdLCJleHAiOjE2NzgzMjAxNjB9." +
                "iKsyy0IhHva5DA2PFRxISjRtS2rKZd6eEEx6bli4zcQ";
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
        String email = account.getEmail();
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

        //when
        String email = account.getEmail();
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

    /* test delete account method while email and password are correct */
    @Test
    void deleteAccountIfEmailAndPasswordAreCorrect() throws Exception {
        //given
        String email = account.getEmail();
        String password = account.getPassword();
        //when
        when(accountRepository.existsAccountByEmail(email)).thenReturn(true);
        when(accountRepository.findByEmail(email)).thenReturn(account);
        when(bCryptPasswordEncoder.matches(password, account.getPassword())).thenReturn(true);

        //then
        assertTrue(underTest.deleteAccount(email, password));
        verify(accountRepository).existsAccountByEmail(email);
        verify(accountRepository).findByEmail(email);
        verify(accountRepository).delete(account);

    }

    /* test delete account method while email is correct but password is not correct */
    @Test
    void deleteAccountIfEmailAndPasswordAreNotCorrect() throws Exception {
        //given
        String email = account.getEmail();
        String password = account.getPassword();
        //when
        when(accountRepository.existsAccountByEmail(email)).thenReturn(true);
        when(accountRepository.findByEmail(email)).thenReturn(account);
        when(bCryptPasswordEncoder.matches(password, account.getPassword())).thenReturn(false);

        //then
        assertThrows(Exception.class, () -> underTest.deleteAccount(email, password));
        verify(accountRepository).existsAccountByEmail(email);
        verify(accountRepository).findByEmail(email);

    }

    /* test delete account method while email is not correct */
    @Test
    void deleteAccountIfEmailIsNotCorrect() throws Exception {
        //given
        String email = account.getEmail();
        String password = account.getPassword();
        //when
        when(accountRepository.existsAccountByEmail(email)).thenReturn(false);

        //then
        assertFalse(underTest.deleteAccount(email, password));
        verify(accountRepository).existsAccountByEmail(email);
    }

    /* test getAccount method which take HttpServletRequest as parameter */
    @Test
    void getAccountWhichTakesHttpServletRequestAsParameter() {
        // given
        String email = account.getEmail();

        when(httpServletRequest.getHeader("Authorization")).thenReturn(token);
        when(accountRepository.findByEmail(email)).thenReturn(account);

        //then
        assertTrue(underTest.getAccount(httpServletRequest).getEmail().equalsIgnoreCase(email));

    }

    /* test getAccount method which take String email as parameter if email is incorrect */
    @Test
    void getAccountIfEmailIsCorrect() {
        // given
        String email = account.getEmail();
        String userId = account.getId() + "";

        //when
        when(accountRepository.findByEmail(email)).thenReturn(account);
        when(fileStorageService.getProfileImage(userId)).thenReturn(new byte[10]);

        //then
        assertTrue(underTest.getAccount(email).getEmail().equalsIgnoreCase(email));
        verify(accountRepository).findByEmail(email);
        verify(fileStorageService).getProfileImage(userId);
    }

    /* test getAccount method which take String email as parameter if email is incorrect */
    @Test
    void getAccountIfEmailIsInCorrect() {
        // given
        String email = account.getEmail();

        //when
        when(accountRepository.findByEmail(email)).thenReturn(null);

        //then
        assertNull(underTest.getAccount(email));
        verify(accountRepository).findByEmail(email);
    }

    @Test
    void getAllAccount() {
        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        //when
        when(accountRepository.findAll()).thenReturn(accounts);
        when(fileStorageService.getProfileImage(account.getId() + "")).thenReturn(new byte[10]);
        underTest.getAllAccount();

        //then
        verify(accountRepository).findAll();
        verify(fileStorageService).getProfileImage(account.getId() + "");
    }


    @Test
    void getAccountByUserNameIfUserNameIsValid() {
        // given
        String userName = account.getUserName();

        //when
        when(accountRepository.findByUserName(userName)).thenReturn(account);
        when(fileStorageService.getProfileImage(account.getId() + "")).thenReturn(new byte[0]);

        //then
        assertEquals(userName, underTest.getAccountByUserName(userName).getUserName());
        verify(accountRepository).findByUserName(userName);
        verify(fileStorageService).getProfileImage(account.getId() + "");
    }

    /*test getAccountByUserName if username is invalid*/
    @Test
    void getAccountByUserNameIfUserNameIsInvalid() {
        //when
        String userName = account.getUserName();

        //then
        assertNull(underTest.getAccountByUserName(userName));

    }

    /*test getAccountByUserName if account is not available*/
    @Test
    void getAccountByUserNameIfAccountIsNotAvailable() {
        //given
        String userName = account.getUserName();

        //when
        when(accountRepository.findByUserName(userName)).thenReturn(null);

        //then
        assertNull(underTest.getAccountByUserName(userName));
        verify(accountRepository).findByUserName(userName);
    }


    @Test
    void getAccountWithDetails() {
        // given
        String email = account.getEmail();

        //when

        when(accountRepository.findByEmail(email)).thenReturn(account);

        //then
        assertEquals(email, underTest.getAccountWithDetails(email).getEmail());
        verify(accountRepository).findByEmail(email);

    }
    /* test account is exist by email */
    @Test
    void isAccountExistByEmailIfExist() {
        // given
        String email = account.getEmail();

        //when
        when(accountRepository.existsAccountByEmail(email)).thenReturn(true);

        //then
        assertTrue(accountRepository.existsAccountByEmail(email));
        verify(accountRepository).existsAccountByEmail(email);

    }

    /* test if account is not exist by email */
    @Test
    void isAccountExistByEmailIfNotExist() {
        // given
        String email = account.getEmail();

        //when
        when(accountRepository.existsAccountByEmail(email)).thenReturn(false);

        //then
        assertFalse(accountRepository.existsAccountByEmail(email));
        verify(accountRepository).existsAccountByEmail(email);

    }


    @Test
    void addAsFriend() {
        //given
        String email = account.getEmail();
        Account targetAccount = new Account();
        targetAccount.setName("alex");
        targetAccount.setUserName("alex_parker");
        targetAccount.setEmail("alex_parker@gmail.com");

        //when
        when(httpServletRequest.getHeader("Authorization")).thenReturn(token);
        when(accountRepository.findByEmail(email)).thenReturn(account);
        when(accountRepository.findByUserName(targetAccount.getUserName())).thenReturn(targetAccount);
        underTest.addAsFriend(httpServletRequest, targetAccount.getUserName());

        //then
        verify(accountRepository).findByEmail(email);
        verify(accountRepository).findByUserName(targetAccount.getUserName());
        verify(accountRepository).save(account);
        verify(accountRepository).save(targetAccount);
    }

    @Test
    void isFriend() {
        //given
        String accountUsername = account.getUserName();
        Account friend = new Account();
        friend.setUserName("alex_parker");
        account.addAccountToFriends(friend);

        //when
        when(accountRepository.findByUserName(accountUsername)).thenReturn(account);

        //then
        assertTrue(underTest.isFriend(accountUsername, friend.getUserName()));
        verify(accountRepository).findByUserName(accountUsername);
    }

    @Test
    void getAllFriends() {
        String username = account.getUserName();

        //when
        when(accountRepository.findByUserName(username)).thenReturn(account);

        //then
        underTest.getAllFriends(username);
        verify(accountRepository).findByUserName(username);
    }
}