package com.herawi.sigma.dto;

public class RegistrationResponse {
    private String accessToken;
    private AccountDTO accountDTO;

    public RegistrationResponse(String accessToken, AccountDTO accountDTO) {
        this.accessToken = accessToken;
        this.accountDTO = accountDTO;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public AccountDTO getAccountDTO() {
        return accountDTO;
    }

    public void setAccountDTO(AccountDTO accountDTO) {
        this.accountDTO = accountDTO;
    }
}
