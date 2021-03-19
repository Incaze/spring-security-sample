package com.incaze.springsecuritysample.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserNotFoundException extends Exception {
    private String errorMessage;

    public UserNotFoundException(String msg) {
        super("invalid login or password: " + msg);
    }
}