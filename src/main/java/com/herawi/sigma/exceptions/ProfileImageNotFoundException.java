package com.herawi.sigma.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//@ResponseStatus(code = HttpStatus.NOT_FOUND, value = HttpStatus.NOT_FOUND)
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProfileImageNotFoundException extends RuntimeException{
    public ProfileImageNotFoundException(String message) {
        super(message);
    }

}
