package com.crowd.exception;

public class AccessForbiddenException extends RuntimeException{
    public AccessForbiddenException(String message) {
        super(message);
    }

}
