package com.herawi.sigma.services;

import com.auth0.jwt.JWT;
import com.herawi.sigma.constants.Gender;
import com.herawi.sigma.dto.AccountDTO;
import com.herawi.sigma.dto.AccountRegistrationRequest;
import com.herawi.sigma.exceptions.AccountNotFoundException;
import com.herawi.sigma.models.Account;
import com.herawi.sigma.repositories.AccountRepository;
import com.herawi.sigma.utils.JWTTools;
import com.herawi.sigma.utils.PaginationUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.http.HttpServletRequest;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@Disabled
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
    @Mock
    private JWTTools jwtTools;
    @Mock
    private AccountDTOMapper accountDTOMapper;
    private AccountService underTest;
    private Account account;
    private AccountRegistrationRequest request;
    private String token;

    @BeforeEach
    void setUp() {
        underTest = new AccountService(
                accountRepository,
                bCryptPasswordEncoder,
                fileStorageService,
                accountDTOMapper
                );
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
        token = JWTTools.createToken(account.getEmail());
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
        String email = request.getEmail();
        //when
        when(accountRepository.existsAccountByEmail(email)).thenReturn(false);
        when(accountRepository.save(any(Account.class))).thenAnswer(i -> i.getArguments()[0]);
        underTest.addAccount(request);
        //then
        verify(accountRepository).existsAccountByEmail(email);
        verify(accountRepository).save(any(Account.class));
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
        String email = account.getEmail();

        //when
        when(httpServletRequest.getHeader("Authorization")).thenReturn(token);
        when(accountRepository.findByEmail(email)).thenReturn(account);

        //then
        underTest.updateAccount(httpServletRequest, request);
        verify(accountRepository).findByEmail(email);
        verify(accountRepository).save(account);
    }

    /* test if account is not already exist and update it*/
    @Test
    void updateAccountIfAccountIsNotAvailable(){
        //given
        String email = account.getEmail();
        // when
        //when()
        when(httpServletRequest.getHeader("Authorization")).thenReturn(token);
        when(accountRepository.findByEmail(account.getEmail())).thenReturn(null);

        //then
        assertThrows(AccountNotFoundException.class, () -> underTest.updateAccount(httpServletRequest, request));
        verify(accountRepository).findByEmail(email);

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
    void deleteAccountIfEmailIsNotCorrect(){
        //given
        String email = account.getEmail();
        String password = account.getPassword();
        //when
        when(accountRepository.existsAccountByEmail(email)).thenReturn(false);

        //then
        assertThrows(AccountNotFoundException.class, () -> underTest.deleteAccount(email, password));
        verify(accountRepository).existsAccountByEmail(email);
    }

    /* test getAccount method which take HttpServletRequest as parameter */
    @Test
    void getAccountByHttpServletRequestAsParameter() {
        // given
        String email = account.getEmail();
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setEmail(email);
        String headerParam = "Authorization";

        when(httpServletRequest.getHeader(headerParam)).thenReturn(token);
        when(accountRepository.findByEmail(email)).thenReturn(account);
        when(accountDTOMapper.apply(account)).thenReturn(accountDTO);
        //then
        assertTrue(underTest.getAccount(httpServletRequest).getEmail().equalsIgnoreCase(email));
        verify(accountDTOMapper).apply(account);
        verify(accountRepository).findByEmail(email);
        verify(httpServletRequest).getHeader(headerParam);

    }

    /* test getAccount method which take String email as parameter if email is incorrect */
    @Test
    void getAccountIfEmailIsCorrect() {
        // given
        String email = account.getEmail();
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setEmail(email);

        //when
        when(accountRepository.findByEmail(email)).thenReturn(account);
        when(accountDTOMapper.apply(account)).thenReturn(accountDTO);

        //then
        assertTrue(underTest.getAccount(email).getEmail().equalsIgnoreCase(email));
        verify(accountRepository).findByEmail(email);

    }

    /* test getAccount method which take String email as parameter if email is incorrect */
    @Test
    void getAccountIfEmailIsInCorrect() {
        // given
        String email = account.getEmail();

        //when
        when(accountRepository.findByEmail(email)).thenReturn(null);

        //then
        assertThrows(AccountNotFoundException.class, () -> underTest.getAccount(email));
        verify(accountRepository).findByEmail(email);
    }

    @Test
    void getAllAccount() throws FileNotFoundException {
        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        //when
        when(accountRepository.findAll()).thenReturn(accounts);
        underTest.getAllAccount();

        //then
        verify(accountRepository).findAll();

    }


    @Test
    void getAccountByUserNameIfUserNameIsValid(){
        // given
        String userName = account.getUserName().toLowerCase();
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setUserName(userName);

        //when
        when(accountRepository.findByUserName(userName)).thenReturn(account);
        when(accountDTOMapper.apply(account)).thenReturn(accountDTO);

        //then
        assertEquals(userName, underTest.getAccountByUserName(userName).getUserName());
        verify(accountRepository).findByUserName(userName);
    }

    /*test getAccountByUserName if username is invalid*/
    @Test
    void getAccountByUserNameIfUserNameIsInvalid() {
        //when
        String userName = account.getUserName();

        //then
        assertThrows(AccountNotFoundException.class, () -> underTest.getAccountByUserName(userName));

    }

    /*test getAccountByUserName if account is not available*/
    @Test
    void getAccountByUserNameIfAccountIsNotAvailable() {
        //given
        String userName = account.getUserName();

        //when
        when(accountRepository.findByUserName(userName)).thenReturn(null);

        //then
        assertThrows(AccountNotFoundException.class, () -> underTest.getAccountByUserName(userName));
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