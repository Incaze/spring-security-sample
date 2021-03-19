package com.incaze.springsecuritysample.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserAlreadyExistException extends Exception {
    private String errorMessage;

    public UserAlreadyExistException(String msg) {
        super("user already exist: " + msg);
    }
}
