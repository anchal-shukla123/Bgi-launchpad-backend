package com.bgi.launchpad.exception;

/**
 * Exception thrown when user lacks permission for an action.
 * Returns HTTP 403 Forbidden
 */
public class ForbiddenException extends RuntimeException {
    
    public ForbiddenException(String message) {
        super(message);
    }
}