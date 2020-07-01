package io.github.encryptorcode.service;

import io.github.encryptorcode.entity.ASession;
import io.github.encryptorcode.entity.AUser;
import io.github.encryptorcode.handlers.AAuthenticationHandler;
import io.github.encryptorcode.handlers.ASecurityHandler;
import io.github.encryptorcode.handlers.ASessionHandler;
import io.github.encryptorcode.handlers.AUserHandler;
import io.github.encryptorcode.implementation.security.ZeroSecurityHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the main configuration file, All the core settings for setting up your authentication instance are provided here.
 *
 * @param <Session> Session template
 * @param <User>    User template
 */
public class AuthenticationConfiguration<Session extends ASession, User extends AUser> {

    /**
     * list of configured providers
     */
    Map<String, OauthProvider> oauthProviders;

    /**
     * handler for authentication details entity
     */
    AAuthenticationHandler authenticationHandler;

    /**
     * handler for sessions entity
     */
    ASessionHandler<Session, User> sessionHandler;

    /**
     * handler for users entity
     */
    AUserHandler<User> userHandler;

    /**
     * app home path
     */
    String homePath;

    /**
     * app login path
     */
    String loginPath;

    /**
     * cookie name to be set for authentication
     */
    String authenticationCookieName;

    /**
     * handler for security related operations
     */
    ASecurityHandler<User> securityHandler;

    AuthenticationConfiguration() {
        this.oauthProviders = new HashMap<>();
    }

    static AuthenticationConfiguration<?, ?> configuration;

    /**
     * Initialize the {@link AuthenticationConfiguration} using this initializer immediately after your server starts to initialise authentication layer
     *
     * @param <Session> Session template
     * @param <User>    User template
     */

    static <Session extends ASession, User extends AUser> AuthenticationConfiguration<Session, User> getConfiguration() {
        //noinspection unchecked
        return (AuthenticationConfiguration<Session, User>) AuthenticationConfiguration.configuration;
    }



}
