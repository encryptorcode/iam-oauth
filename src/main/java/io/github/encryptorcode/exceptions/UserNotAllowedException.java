package io.github.encryptorcode.exceptions;

import io.github.encryptorcode.entity.AUser;

/**
 * This exception is used to restrict a particular user from signing up.
 * You can throw this exception from {@link io.github.encryptorcode.handlers.AUserHandler#createUser(AUser)}
 */
public class UserNotAllowedException extends Exception {
    public UserNotAllowedException(String message) {
        super(message);
    }
}