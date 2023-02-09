package com.herawi.sigma.service;

import com.herawi.sigma.dto.AccountDTO;
import com.herawi.sigma.model.Account;
import com.herawi.sigma.model.ProfileImage;

import java.util.function.Function;

public class AccountDTOMapper {
    public static AccountDTO apply(Account account, ProfileImage profileImage) {
        return  new AccountDTO(
                account.getName(),
                account.getLastName(),
                profileImage.getImage(),
                account.getEmail(),
                account.getConnections().size()
        );
    }
}
