package io.github.encryptorcode.handlers;

import io.github.encryptorcode.entity.ASession;
import io.github.encryptorcode.entity.AUser;

/**
 * Abstract session handler implementation for handling session operations
 * @param <Session> {@link ASession} template
 * @param <User> {@link AUser} template
 */
public abstract class ASessionHandler<Session extends ASession, User extends AUser> {

    /**
     * Since framework uses an abstract {@link ASession} class,
     * this method will help this framework to construct a session object.
     *
     * @return an empty(default) session object
     */
    public abstract Session constructSession();

    /**
     * Get session details based on session identifier
     *
     * @param identifier identifier of the session
     * @return session object with possibly all details or
     * null if session doesn't exists or is expired
     */
    public abstract Session getSession(String identifier);

    /**
     * Creates a session for the user
     *
     * @param session session with details of the user
     * @return session object, with updated details if any
     */
    public abstract Session createSession(Session session);

    /**
     * Delete session using session identifier
     *
     * @param identifier identifier of the session
     */
    public abstract void deleteSession(String identifier);
}
