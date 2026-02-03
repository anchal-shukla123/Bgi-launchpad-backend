package com.bgi.launchpad.exception;

/**
 * Exception thrown when request data is invalid.
 * Returns HTTP 400 Bad Request
 */
public class BadRequestException extends RuntimeException {
    
    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}