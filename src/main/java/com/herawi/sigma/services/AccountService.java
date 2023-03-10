package com.herawi.sigma.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.herawi.sigma.constants.APIEndpoints;
import com.herawi.sigma.dto.AccountDTO;
import com.herawi.sigma.dto.AccountRegistrationRequest;
import com.herawi.sigma.dto.RegistrationResponse;
import com.herawi.sigma.exceptions.AccountNotFoundException;
import com.herawi.sigma.filters.AccountRegistrationRequestFilter;
import com.herawi.sigma.filters.FilterResponse;
import com.herawi.sigma.models.Account;
import com.herawi.sigma.models.Role;
import com.herawi.sigma.repositories.AccountRepository;
import com.herawi.sigma.utils.JWTTools;
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
    private final AccountDTOMapper accountDTOMapper;

    @Autowired
    public AccountService(AccountRepository accountRepository,
                          BCryptPasswordEncoder bCryptPasswordEncoder,
                          FileStorageService fileStorageService, AccountDTOMapper accountDTOMapper) {
        this.accountRepository = accountRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.fileStorageService = fileStorageService;
        this.accountDTOMapper = accountDTOMapper;
    }

    /*
     * this method implemented for for UserDetailsService for authentication process
     * in Spring security
     * */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(email);
        if (account == null) {
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
     * with JWT token for authorizing the account in the subsequence requests
     * */
    public ResponseEntity<?> addAccount(AccountRegistrationRequest accountRegistrationRequest) throws Exception {
        if (accountRegistrationRequest != null) {
            FilterResponse filterResponse = AccountRegistrationRequestFilter.filter(accountRegistrationRequest);
            if (!filterResponse.isOk()) {
                return new ResponseEntity<>(filterResponse, HttpStatus.BAD_REQUEST);
            }
            if (accountRepository.existsAccountByEmail(accountRegistrationRequest.getEmail())) {
                Map<String, String> response = new HashMap<>();
                response.put("errorMessage", "This email already has taken");
                response.put("status", HttpStatus.BAD_REQUEST.name());
                response.put("statusCode", HttpStatus.BAD_REQUEST.value() + "");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            Account account = new Account();
            account.setUserName(accountRegistrationRequest.getEmail().substring(0, accountRegistrationRequest.getEmail().indexOf("@")));
            account.setName(accountRegistrationRequest.getName());
            account.setLastName(accountRegistrationRequest.getLastName());
            account.setDob(accountRegistrationRequest.getDob());
            account.setEmail(accountRegistrationRequest.getEmail());
            account.setGender(accountRegistrationRequest.getGender());

            // encode password before saving in database
            account.setPassword(bCryptPasswordEncoder.encode(accountRegistrationRequest.getPassword()));
            account = accountRepository.save(account);
            if (accountRegistrationRequest.getImg() != null && !accountRegistrationRequest.getImg().isEmpty()) {
                fileStorageService.storeFile(accountRegistrationRequest.getImg(), account.getId() + "");
            }
            Algorithm algorithm = Algorithm.HMAC256("Bearer".getBytes());
            String accessToken = JWT.create()
                    .withSubject(account.getEmail())
                    .withExpiresAt(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 10)))
                    .withClaim("roles", account.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                    .sign(algorithm);
            RegistrationResponse registrationResponse = new RegistrationResponse(
                    accessToken,
                    accountDTOMapper.apply(account)
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
                if (AccountRegistrationRequestFilter.filterEmail(accountRegistrationRequest.getEmail())) {
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

    public boolean deleteAccount(String email, String password) throws Exception {
        if (!accountRepository.existsAccountByEmail(email)) {
            throw new AccountNotFoundException("account not found with the provided email");
        }
        Account p = accountRepository.findByEmail(email);
        boolean arePasswordsMatched = bCryptPasswordEncoder.matches(password, p.getPassword());
        if (!arePasswordsMatched) {
            throw new Exception("Wrong password! account did not remove");
        }
        accountRepository.delete(p);
        return true;
    }

    /* gives the specific account detail with request header authorization or (jwt token) */
    public AccountDTO getAccount(HttpServletRequest request) {
        String email = JWTTools.getUserEmailByJWT(request);
        return getAccount(email);
    }

    /* give all the accounts which are saved in the database*/
    public Collection<AccountDTO> getAllAccount() {
        return accountRepository
                .findAll()
                .stream()
                .map(accountDTOMapper)
                .collect(Collectors.toList());
    }

    /*
     * find account by email and return its information by accountInfo dto
     * */
    public AccountDTO getAccount(String email) {
        if(accountRepository.existsAccountByEmail(email)){
            throw new AccountNotFoundException("account not found with the provided email");
        }
        Account account = accountRepository.findByEmail(email);
        return accountDTOMapper.apply(account);
    }

    /* return account by userName */
    public AccountDTO getAccountByUserName(String userName)  {
        if (userName == null || userName.isEmpty()) return null;
        Account account = accountRepository.findByUserName(userName);
        return accountDTOMapper.apply(account);
    }

    /* return all account information by email of the account*/
    public Account getAccountWithDetails(String email) {
        if (email != null) {
            email = email.toLowerCase().trim();
            return accountRepository.findByEmail(email);
        }
        return null;
    }


    /* check whether there is an account with the provided email address*/
    public boolean isAccountExistByEmail(String email) {
        return accountRepository.existsAccountByEmail(email);
    }

    /*
     * add someone as friend to current logged in account
     * */
    public void addAsFriend(HttpServletRequest request, String userName) {
        String email = JWTTools.getUserEmailByJWT(request);
        Account account = accountRepository.findByEmail(email);
        Account targetAccount = accountRepository.findByUserName(userName);
        if (account == null || targetAccount == null)
            return;
        account.addAccountToFriends(targetAccount);
        targetAccount.getFriends().add(account);

        accountRepository.save(account);
        accountRepository.save(targetAccount);
    }

    /*
     * check if someone is friend with target account
     * */
    public boolean isFriend(String accountUserName, String friendUserName) {
        return accountRepository.findByUserName(accountUserName)
                .getFriends()
                .stream()
                .anyMatch(friend -> friend.getUserName().equalsIgnoreCase(friendUserName));

    }

    /*
    * get all friends of the target account
    * */

    public Collection<AccountDTO> getAllFriends(String userName){
        return accountRepository
                .findByUserName(userName)
                .getFriends()
                .stream()
                .map(accountDTOMapper)
                .collect(Collectors.toList());
    }
}
