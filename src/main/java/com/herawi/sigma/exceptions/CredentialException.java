package com.herawi.sigma.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CredentialException extends RuntimeException {
    public CredentialException(String message) {
        super(message);
    }
}
