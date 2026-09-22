package com.smartlostfound.claimservice.exception;

public class InvalidClaimException extends RuntimeException {

    public InvalidClaimException(String message) {
        super(message);
    }
}