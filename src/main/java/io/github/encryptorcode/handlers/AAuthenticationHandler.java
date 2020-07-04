package io.github.encryptorcode.handlers;

import io.github.encryptorcode.entity.AuthenticationDetail;

/**
 * Abstract authentication handler implementation for handling authentication details operations
 */
public abstract class AAuthenticationHandler {
    /**
     * used to get authentication details based on userid and provider id
     *
     * @param userId     user id
     * @param providerId provider id
     * @return authentication detail object
     */
    public abstract AuthenticationDetail getAuthenticationDetail(String userId, String providerId);

    /**
     * Creates a new authentication detail for a given user
     * @param detail authentication details object to create
     * @return authentication details object, with updated details if any
     */
    public abstract AuthenticationDetail create(AuthenticationDetail detail);

    /**
     * Updates tokens and expiry of an existing authentication detail
     * @param detail authentication details object to update
     * @return authentication details object, with updated details if any
     */
    public abstract AuthenticationDetail update(AuthenticationDetail detail);

    /**
     * Deletes authentication details in case it invalidates
     * @param detail authentication details to delete
     */
    public abstract void delete(AuthenticationDetail detail);
}
