package io.github.encryptorcode.handlers;

import io.github.encryptorcode.entity.AUser;

public abstract class ASecurityHandler<User extends AUser> {

    /**
     * Generates a uniquely random identifier for the session.
     *
     * @param user user details
     * @return unique string for using as identifier for session
     */
    public abstract String generateIdentifier(User user);

    /**
     * Used to customize session expiry time.
     *
     * @param user user details
     * @return time in seconds
     */
    public abstract int getSessionExpiration(User user);


    /**
     * Encrypts the cookie value
     *
     * @param cookieValue value of the cookie
     * @return encrypted string
     */
    public abstract String encryptCookieValue(String cookieValue);

    /**
     * Decrypts the cookie value
     *
     * @param encryptedValue encrypted value
     * @return decrypted string
     */
    public abstract String decryptCookieValue(String encryptedValue);
}
