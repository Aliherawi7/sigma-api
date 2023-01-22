package com.herawi.sigma.repository;

import com.herawi.sigma.model.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface AccountRepository extends CrudRepository<Account, Long> {
    Account getPersonByEmail(String email);
    Collection<Account> findAllByNameOrLastName(String name, String lastName);
    boolean existsByEmailOrPhoneNumber(String email, String phoneNumber);
    boolean existsByEmailOrUserName(String email, String userName);

}
