package com.fyaora.profilemanagement.profileservice.advice;

public class DuplicateWaitlistRequestException extends RuntimeException {
    public DuplicateWaitlistRequestException(String email) {
        super(String.format("Waitlist request already exists for email: %s",  email));
    }
    public DuplicateWaitlistRequestException(String email, Throwable cause) {
        super(String.format("Waitlist request already exists for email: %s",  email), cause);
    }
}
