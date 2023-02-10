package com.herawi.sigma.service;

import com.herawi.sigma.dto.AccountDTO;
import com.herawi.sigma.dto.AccountRegistrationRequest;
import com.herawi.sigma.filter.AccountRegistrationRequestFilter;
import com.herawi.sigma.model.Account;
import com.herawi.sigma.model.ProfileImage;
import com.herawi.sigma.repository.AccountRepository;
import com.herawi.sigma.repository.ProfileImageRepository;
import com.herawi.sigma.tools.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;


@Service
public class AccountService implements UserDetailsService {
    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ProfileImageRepository profileImageRepository;
    private final FileStorageService fileStorageService;

    @Autowired
    public AccountService(AccountRepository accountRepository,
                          BCryptPasswordEncoder bCryptPasswordEncoder,
                          ProfileImageRepository profileImageRepository,
                          FileStorageService fileStorageService) {
        this.accountRepository = accountRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.profileImageRepository = profileImageRepository;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    public boolean addAccount(AccountRegistrationRequest accountRegistrationRequest) throws Exception {
        if (accountRegistrationRequest != null) {
            if (!AccountRegistrationRequestFilter.filter(accountRegistrationRequest)) {
                return false;
            }
            if (accountRepository.existsByEmail(accountRegistrationRequest.getEmail())) {
                throw new Exception("Email already has taken");
            }
            Account account = new Account();
            account.setName(accountRegistrationRequest.getName());
            account.setLastName(accountRegistrationRequest.getLastName());
            account.setDob(accountRegistrationRequest.getDob());
            account.setEmail(accountRegistrationRequest.getEmail());

            // encode password before saving in database
            account.setPassword(bCryptPasswordEncoder.encode(accountRegistrationRequest.getPassword()));
            account = accountRepository.save(account);
            if (!accountRegistrationRequest.getImg().isEmpty()) {
                fileStorageService.storeFile(accountRegistrationRequest.getImg(),account.getId()+"");
            }
            return true;
        }
        return false;
    }

    public boolean updateAccount(HttpServletRequest request, AccountRegistrationRequest accountRegistrationRequest) throws Exception {
        String accountEmail = JWTTools.getUserEmailByJWT(request);
        if (accountRegistrationRequest != null) {
            if (!accountRepository.existsByEmail(accountEmail)) {
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
                        && !accountRepository.existsByEmail(accountRegistrationRequest.getEmail())) {
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
        if (accountRepository.existsByEmail(email)) {
            Account p = accountRepository.findByEmail(email);
            boolean arePasswordsMatched = bCryptPasswordEncoder.matches(password, p.getPassword());
            if (arePasswordsMatched) {
                accountRepository.delete(p);
                return true;
            } else {
                throw new Exception("Wrong password! user not removed");
            }
        }
        return false;
    }

    public AccountDTO getAccount(HttpServletRequest request) {
        String email = JWTTools.getUserEmailByJWT(request);
        return getAccount(email);
    }

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
                account.getConnections().size()
        );
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
     * add someone as connection
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
