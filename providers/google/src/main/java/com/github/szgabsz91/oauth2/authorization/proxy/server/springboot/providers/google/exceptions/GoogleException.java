package com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.google.exceptions;

/**
 * Exception for internal Google errors.
 *
 * @author szgabsz91
 */
public class GoogleException extends Exception {

    /**
     * Constructor that sets the error message.
     * @param message the error message
     */
    public GoogleException(String message) {
        super(message);
    }

    /**
     * Constructor that sets the error message and the root cause.
     * @param message the error message
     * @param cause the root cause
     */
    public GoogleException(String message, Throwable cause) {
        super(message, cause);
    }

}
