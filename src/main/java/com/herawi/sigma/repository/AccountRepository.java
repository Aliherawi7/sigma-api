package com.herawi.sigma.repository;

import com.herawi.sigma.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Collection;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByEmail(String email);
    Account findByUserName(String userName);
    Collection<Account> findAllByNameOrLastName(String name, String lastName);
    boolean existsAccountByEmail(String email);
    boolean existsByEmailOrUserName(String email, String userName);

}
