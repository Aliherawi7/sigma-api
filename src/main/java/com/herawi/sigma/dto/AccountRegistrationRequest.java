package com.herawi.sigma.dto;

import java.time.LocalDate;

public class AccountRegistrationRequest {
    private String name;
    private String lastName;
    private String email;
    private String password;
    private LocalDate dob;

    public AccountRegistrationRequest() {
    }

    public AccountRegistrationRequest(String name, String lastName, String email, String password, LocalDate dob) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.dob = dob;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }
}
