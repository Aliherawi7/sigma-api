package com.herawi.sigma.services;

import com.herawi.sigma.dto.AccountDTO;
import com.herawi.sigma.models.Account;


public class AccountDTOMapper {
    public static AccountDTO apply(Account account, byte[] profileImage) {
        return  new AccountDTO(
                account.getName(),
                account.getLastName(),
                account.getUserName(),
                profileImage,
                account.getEmail(),
                account.getFriends().size(),
                account.getGender()
        );
    }
}
