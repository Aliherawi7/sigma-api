package com.herawi.heraynet.repository;

import com.herawi.heraynet.model.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface PersonRepository extends CrudRepository<Account, Long> {
    Account getPersonByEmail(String email);
    Collection<Account> findAllByNameOrLastName(String nameOrLastName);
    boolean existsByEmailOrPhoneNumber(String email, String phoneNumber);
    boolean existsByEmailOrUserName(String email, String userName);

}
