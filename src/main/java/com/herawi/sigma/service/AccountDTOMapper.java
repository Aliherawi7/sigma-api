package com.herawi.sigma.service;

import com.herawi.sigma.dto.AccountDTO;
import com.herawi.sigma.model.Account;


public class AccountDTOMapper {
    public static AccountDTO apply(Account account, byte[] profileImage) {
        return  new AccountDTO(
                account.getName(),
                account.getLastName(),
                profileImage,
                account.getEmail(),
                account.getConnections().size(),
                account.getGender()
        );
    }
}
