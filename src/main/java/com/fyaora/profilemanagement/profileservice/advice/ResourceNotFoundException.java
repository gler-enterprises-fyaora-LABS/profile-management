package com.fyaora.profilemanagement.profileservice.advice;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String msg) {
        super(msg);
    }

    public ResourceNotFoundException(String msg, Throwable th) {
        super(msg, th);
    }
}
