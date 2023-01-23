package com.herawi.sigma.service;

import com.herawi.sigma.dto.AccountInfo;
import com.herawi.sigma.model.Account;
import com.herawi.sigma.model.ProfileImage;
import com.herawi.sigma.repository.AccountRepository;
import com.herawi.sigma.repository.ProfileImageRepository;
import com.herawi.sigma.tools.JWTTools;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;


@Service
public class AccountService implements UserDetailsService {
    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ProfileImageRepository profileImageRepository;

    public AccountService(AccountRepository accountRepository,
                          BCryptPasswordEncoder bCryptPasswordEncoder,
                          ProfileImageRepository profileImageRepository) {
        this.accountRepository = accountRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.profileImageRepository = profileImageRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    public boolean addAccount(Account account, byte[] imageBytes) throws Exception {
        if(account != null){
            if(account.getAge() < 18){
                throw new Exception("The user age must be above 18 years old!!!");
            }
            if(accountRepository.existsByEmailOrPhoneNumber(account.getEmail(), account.getPhoneNumber())){
                throw new Exception("Email or phone number already has taken");
            }
            // encode password before saving in database
            account.setPassword(bCryptPasswordEncoder.encode(account.getPassword()));
            accountRepository.save(account);
            ProfileImage profileImage = new ProfileImage(accountRepository.findByEmail(account.getEmail()).getId(), imageBytes);
            profileImageRepository.save(profileImage);
            return true;
        }
        return false;
    }
    public Account updateAccount(Account account) throws Exception {
        if(account != null){
            if(!accountRepository.existsByEmailOrPhoneNumber(account.getEmail(), account.getPhoneNumber())){
                throw new Exception("This is not found in database");
            }
            // encode password before saving in database
            account.setPassword(bCryptPasswordEncoder.encode(account.getPassword()));
            return accountRepository.save(account);
        }
        return null;
    }

    public boolean deleteAccount(String userName, String email, String password) throws Exception {
        if(accountRepository.existsByEmailOrPhoneNumber(email, userName)){
            Account p = accountRepository.findByEmail(email);
            boolean arePasswordsMatched = bCryptPasswordEncoder.matches(password, p.getPassword());
            if(arePasswordsMatched){
                accountRepository.delete(p);
                return true;
            }else {
                throw new Exception("Wrong password! user not removed");
            }
        }
        return false;
    }

    public AccountInfo getAccount(HttpServletRequest request){
        String email = JWTTools.getUserEmailByJWT(request);
        return getAccount(email);
    }

    /*
    * find account by email and return its information by accountInfo dto
    * */
    public AccountInfo getAccount(String email){
        Account account = accountRepository.findByEmail(email);
        if(account == null){
            return null;
        }
        ProfileImage profileImage = profileImageRepository.findById(account.getId()).orElse(new ProfileImage());

        return new AccountInfo(
                account.getName(),
                account.getLastName(),
                profileImage.getImage(),
                account.getEmail(),
                account.getConnections().size()
        );
    }
    /*
    * find all connections of this account
    * */
    public Collection<AccountInfo> getAllConnections(HttpServletRequest request){
        String email = JWTTools.getUserEmailByJWT(request);
        Account currentAccount = accountRepository.findByEmail(email);
        Collection<AccountInfo> connections = new ArrayList<>();
        currentAccount.getConnections().forEach(account -> {
            ProfileImage profileImage = profileImageRepository.findById(account.getId()).orElse(new ProfileImage());
            connections.add(new AccountInfo(
                    account.getName(),
                    account.getLastName(),
                    profileImage.getImage(),
                    account.getEmail(),
                    account.getConnections().size()
            ));
        });
        return connections;
    }

    /*
    * add someone as connection
    * */

    public boolean addAsConnection(HttpServletRequest request, String targetEmail){
        String email = JWTTools.getUserEmailByJWT(request);
        Account account = accountRepository.findByEmail(email);
        Account targetAccount = accountRepository.findByEmail(targetEmail);
        if(account == null || targetAccount == null)
            return false;

        account.addAccountToConnections(targetAccount);
        accountRepository.save(account);
        return true;
    }


    
}
