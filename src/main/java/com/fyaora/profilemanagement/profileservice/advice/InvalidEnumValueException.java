package com.fyaora.profilemanagement.profileservice.advice;

public class InvalidEnumValueException extends IllegalArgumentException {
    public InvalidEnumValueException(String message) {
        super(message);
    }
}