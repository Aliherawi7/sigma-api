package com.herawi.sigma.service;

import com.herawi.sigma.model.Account;
import com.herawi.sigma.model.ProfileImage;
import com.herawi.sigma.repository.AccountRepository;
import com.herawi.sigma.repository.ProfileImageRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    public boolean addAccount(Account account, MultipartFile profileImg) throws Exception {
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
            ProfileImage profileImage = new ProfileImage(accountRepository.findByEmail(account.getEmail()).getId(), profileImg.getBytes());
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

    public








}
