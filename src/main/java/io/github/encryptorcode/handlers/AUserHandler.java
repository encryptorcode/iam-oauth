package io.github.encryptorcode.handlers;

import io.github.encryptorcode.entity.AUser;
import io.github.encryptorcode.exceptions.UserNotAllowedException;

import java.util.UUID;

/**
 * Abstract user handler implementation for handling user operations
 * @param <User> {@link AUser} template
 */
public abstract class AUserHandler<User extends AUser> {

    /**
     * Generates an id for the user based on email.
     *
     * @param user user implementation
     * @return generated user id
     */
    public String generateUserId(User user) {
        return UUID.randomUUID().toString();
    }

    /**
     * Get user based on user id.
     *
     * @param id user id
     * @return user object with possibly all details or
     * null if user doesn't exists
     */
    public abstract User getUser(String id);

    /**
     * Get user based on user email.
     *
     * @param email user email
     * @return user object with possibly all details or
     * null if user doesn't exists
     */
    public abstract User getUserByEmail(String email);

    /**
     * Creates a user when user is signing up for the first time
     *
     * @param user user object with basic details required for sign up
     * @return created user object with id
     * @throws UserNotAllowedException if the user shouldn't be allowed to sign up with a reason
     */
    public abstract User createUser(User user) throws UserNotAllowedException;

}
