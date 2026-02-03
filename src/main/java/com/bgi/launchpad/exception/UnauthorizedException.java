package com.bgi.launchpad.exception;

/**
 * Exception thrown when user is not authenticated.
 * Returns HTTP 401 Unauthorized
 */
public class UnauthorizedException extends RuntimeException {
    
    public UnauthorizedException(String message) {
        super(message);
    }
}