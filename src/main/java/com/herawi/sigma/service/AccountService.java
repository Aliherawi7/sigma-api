package com.herawi.heraynet.service;

import com.herawi.heraynet.model.Account;
import com.herawi.heraynet.repository.AccountRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService implements UserDetailsService {
    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AccountService(AccountRepository accountRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.accountRepository = accountRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    public boolean addAccount(Account account) throws Exception {
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
            Account p = accountRepository.getPersonByEmail(email);
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





}
