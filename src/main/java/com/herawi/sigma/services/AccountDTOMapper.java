package com.herawi.sigma.services;

import com.herawi.sigma.constants.APIEndpoints;
import com.herawi.sigma.dto.AccountDTO;
import com.herawi.sigma.models.Account;
import org.springframework.stereotype.Component;

import java.util.function.Function;


@Component
public class AccountDTOMapper implements Function<Account, AccountDTO> {
    public AccountDTO apply(Account account) {
        return  new AccountDTO(
                account.getName(),
                account.getLastName(),
                account.getUserName(),
                APIEndpoints.PROFILE_PICTURE.getValue()+account.getUserName(),
                account.getEmail(),
                account.getFriends().size(),
                account.getGender()
        );
    }
}
