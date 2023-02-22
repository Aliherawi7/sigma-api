package com.herawi.sigma.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.herawi.sigma.dto.AccountDTO;
import com.herawi.sigma.dto.AccountRegistrationRequest;
import com.herawi.sigma.dto.RegistrationResponse;
import com.herawi.sigma.filter.AccountRegistrationRequestFilter;
import com.herawi.sigma.filter.FilterResponse;
import com.herawi.sigma.model.Account;
import com.herawi.sigma.model.Role;
import com.herawi.sigma.repository.AccountRepository;
import com.herawi.sigma.tools.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class AccountService implements UserDetailsService {
    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final FileStorageService fileStorageService;

    @Autowired
    public AccountService(AccountRepository accountRepository,
                          BCryptPasswordEncoder bCryptPasswordEncoder,
                          FileStorageService fileStorageService) {
        this.accountRepository = accountRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.fileStorageService = fileStorageService;
    }

    /*
    * this method implemented for for UserDetailsService for authentication process
    * in Spring security
    * */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(email);
        if(account == null){
            throw new UsernameNotFoundException("account not found");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        account.getRoles()
                .forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return new org.springframework.security.core.userdetails.User(account.getEmail(), account.getPassword(), authorities);
    }
    /*
    * this method add the new account in the database with provided information
    * and then after saving successfully in the database it returns the account information
    * with JWT token for authorization the account in the subsequence requests
    * */
    public ResponseEntity<?> addAccount(AccountRegistrationRequest accountRegistrationRequest) throws Exception {
        if (accountRegistrationRequest != null) {
            FilterResponse filterResponse = AccountRegistrationRequestFilter.filter(accountRegistrationRequest);
            if (!filterResponse.isOk()) {
                return new ResponseEntity<>(filterResponse, HttpStatus.BAD_REQUEST);
            }
            if (accountRepository.existsAccountByEmail(accountRegistrationRequest.getEmail())) {
                System.out.println("account already exist");
                Map<String, String> response = new HashMap<>();
                response.put("errorMessage", "This email already has taken");
                response.put("status", HttpStatus.BAD_REQUEST.name());
                response.put("statusCode", HttpStatus.BAD_REQUEST.value()+"");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            Account account = new Account();
            account.setName(accountRegistrationRequest.getName());
            account.setLastName(accountRegistrationRequest.getLastName());
            account.setDob(accountRegistrationRequest.getDob());
            account.setEmail(accountRegistrationRequest.getEmail());
            account.setGender(accountRegistrationRequest.getGender());

            // encode password before saving in database
            account.setPassword(bCryptPasswordEncoder.encode(accountRegistrationRequest.getPassword()));
            account = accountRepository.save(account);
            if (accountRegistrationRequest.getImg() != null && !accountRegistrationRequest.getImg().isEmpty()) {
                fileStorageService.storeFile(accountRegistrationRequest.getImg(),account.getId()+"");
            }
            Algorithm algorithm = Algorithm.HMAC256("Bearer".getBytes());
            String accessToken = JWT.create()
                    .withSubject(account.getEmail())
                    .withExpiresAt(new Date(System.currentTimeMillis() + (1000*60*60*24*10)))
                    .withClaim("roles", account.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                    .sign(algorithm);
            RegistrationResponse registrationResponse =  new RegistrationResponse(
                    accessToken,
                    AccountDTOMapper.apply(account, accountRegistrationRequest.getImg() != null ? accountRegistrationRequest.getImg().getBytes() : null)
            );
            return new ResponseEntity<>(registrationResponse, HttpStatus.CREATED);
        }
        return ResponseEntity.badRequest().build();
    }

    /*
     * update the current logged in account with the provided information
     * this method should return the account information with the updated information
     */
    public boolean updateAccount(HttpServletRequest request, AccountRegistrationRequest accountRegistrationRequest) throws Exception {
        String accountEmail = JWTTools.getUserEmailByJWT(request);
        if (accountRegistrationRequest != null) {
            if (!accountRepository.existsAccountByEmail(accountEmail)) {
                throw new Exception("This user is not found in database");
            }
            Account account = accountRepository.findByEmail(accountRegistrationRequest.getEmail());
            if (accountRegistrationRequest.getDob() != null) {
                if (AccountRegistrationRequestFilter.filterDOB(accountRegistrationRequest.getDob())) {
                    account.setDob(accountRegistrationRequest.getDob());
                }
            }
            if (accountRegistrationRequest.getEmail() != null) {
                if (AccountRegistrationRequestFilter.filterEmail(accountRegistrationRequest.getEmail())
                        && !accountRepository.existsAccountByEmail(accountRegistrationRequest.getEmail())) {
                    account.setEmail(accountRegistrationRequest.getEmail());
                }
            }
            if (accountRegistrationRequest.getName() != null) {
                if (AccountRegistrationRequestFilter.filterNameOrLastName(accountRegistrationRequest.getName())) {
                    account.setName(accountRegistrationRequest.getName());
                }
            }
            if (accountRegistrationRequest.getLastName() != null) {
                if (AccountRegistrationRequestFilter.filterNameOrLastName(accountRegistrationRequest.getLastName())) {
                    account.setLastName(accountRegistrationRequest.getLastName());
                }
            }
            if (accountRegistrationRequest.getPassword() != null) {
                if (AccountRegistrationRequestFilter.filterPassword(accountRegistrationRequest.getPassword())) {
                    // encode password before saving in database
                    account.setPassword(bCryptPasswordEncoder.encode(accountRegistrationRequest.getPassword()));
                }
            }
            accountRepository.save(account);
            return true;
        }
        return false;
    }

    public boolean deleteAccount(String userName, String email, String password) throws Exception {
        if (accountRepository.existsAccountByEmail(email)) {
            Account p = accountRepository.findByEmail(email);
            boolean arePasswordsMatched = bCryptPasswordEncoder.matches(password, p.getPassword());
            if (arePasswordsMatched) {
                accountRepository.delete(p);
                return true;
            } else {
                throw new Exception("Wrong password! account did not remove");
            }
        }
        return false;
    }

    /* gives the specific account detail with request header authorization */
    public AccountDTO getAccount(HttpServletRequest request) {
        String email = JWTTools.getUserEmailByJWT(request);
        return getAccount(email);
    }

    /* give all the accounts which are saved in the database*/
    public Collection<AccountDTO> getAllAccount(){
        return accountRepository
                .findAll()
                .stream()
                .map(item -> AccountDTOMapper.apply(item,
                        fileStorageService.getProfileImage(item.getId()+""))).collect(Collectors.toList());
    }

    /*
     * find account by email and return its information by accountInfo dto
     * */
    public AccountDTO getAccount(String email) {
        Account account = accountRepository.findByEmail(email);
        System.out.println(accountRepository.findByEmail(email));
        if (account == null) {
            return null;
        }
        byte[] profileImage = fileStorageService.getProfileImage(account.getId()+"");
        return new AccountDTO(
                account.getName(),
                account.getLastName(),
                profileImage,
                account.getEmail(),
                account.getConnections().size(),
                account.getGender()
        );
    }

    /* return all account information by email of the account*/
    public Account getAccountWithDetails(String email){
        if (email != null){
            email = email.toLowerCase().trim();
            return accountRepository.findByEmail(email);
        }
        return null;
    }
    /* check whether there is account with the provided email address*/
    public boolean isAccountExistByEmail(String email){
        return accountRepository.existsAccountByEmail(email);
    }

    /*
     * find all connections of this account
     * */
    public Collection<AccountDTO> getAllConnections(HttpServletRequest request) {
        String email = JWTTools.getUserEmailByJWT(request);
        Account currentAccount = accountRepository.findByEmail(email);
        Collection<AccountDTO> connections = new ArrayList<>();
        currentAccount.getConnections().forEach(account -> {
            byte[] profileImage = fileStorageService.getProfileImage(account.getId()+"");
            connections.add(AccountDTOMapper.apply(currentAccount, profileImage));
        });
        return connections;
    }

    /*
     * add someone as connection to current logged in account
     * */
    public boolean addAsConnection(HttpServletRequest request, String targetEmail) {
        String email = JWTTools.getUserEmailByJWT(request);
        Account account = accountRepository.findByEmail(email);
        Account targetAccount = accountRepository.findByEmail(targetEmail);
        if (account == null || targetAccount == null)
            return false;

        account.addAccountToConnections(targetAccount);
        accountRepository.save(account);
        return true;
    }
}
