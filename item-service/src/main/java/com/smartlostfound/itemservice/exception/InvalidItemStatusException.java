package com.smartlostfound.itemservice.exception;

public class InvalidItemStatusException extends RuntimeException {

    public InvalidItemStatusException(String message) {
        super(message);
    }
}