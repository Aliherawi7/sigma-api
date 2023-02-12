package com.herawi.sigma.dto;

public class LoginInformationDTO {
    private String accessToken;
    private String refreshToken;
    private AccountDTO accountDTO;

    public LoginInformationDTO() {
    }

    public LoginInformationDTO(String accessToken, String refreshToken, AccountDTO accountDTO) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accountDTO = accountDTO;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String token) {
        this.accessToken = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public AccountDTO getAccountDTO() {
        return accountDTO;
    }

    public void setAccountDTO(AccountDTO accountDTO) {
        this.accountDTO = accountDTO;
    }
}
