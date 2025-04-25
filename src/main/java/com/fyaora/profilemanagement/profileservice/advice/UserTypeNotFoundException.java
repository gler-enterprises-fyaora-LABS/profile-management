package com.fyaora.profilemanagement.profileservice.advice;

public class UserTypeNotFoundException extends RuntimeException {
    public UserTypeNotFoundException(String msg) {
        super(msg);
    }
}
