package io.github.encryptorcode.service;

import io.github.encryptorcode.entity.ASession;
import io.github.encryptorcode.entity.AUser;
import io.github.encryptorcode.handlers.AAuthenticationHandler;
import io.github.encryptorcode.handlers.ASecurityHandler;
import io.github.encryptorcode.handlers.ASessionHandler;
import io.github.encryptorcode.handlers.AUserHandler;
import io.github.encryptorcode.implementation.security.ZeroSecurityHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AuthenticationInitializer<Session extends ASession, User extends AUser> {

    private static final Logger LOGGER = Logger.getLogger(AuthenticationInitializer.class.getName());
    private final AuthenticationConfiguration<Session, User> configuration;

    private AuthenticationInitializer() {
        this.configuration = new AuthenticationConfiguration<>();
    }

    public static <Session extends ASession, User extends AUser> AuthenticationInitializer<Session, User> newInstance(Class<Session> sessionClass, Class<User> userClass) {
        return new AuthenticationInitializer<>();
    }

    public AuthenticationInitializer<Session, User> addOAuthProvider(OauthProvider provider) {
        this.configuration.oauthProviders.put(provider.id(), provider);
        return this;
    }

    public AuthenticationInitializer<Session, User> setAuthenticationHandler(AAuthenticationHandler authenticationHandler) {
        this.configuration.authenticationHandler = authenticationHandler;
        return this;
    }

    public AuthenticationInitializer<Session, User> setSessionHandler(ASessionHandler<Session, User> sessionHandler) {
        this.configuration.sessionHandler = sessionHandler;
        return this;
    }

    public AuthenticationInitializer<Session, User> setUserHandler(AUserHandler<User> userHandler) {
        this.configuration.userHandler = userHandler;
        return this;
    }

    public AuthenticationInitializer<Session, User> setSecurityHandler(ASecurityHandler<User> securityHandler) {
        this.configuration.securityHandler = securityHandler;
        return this;
    }

    public AuthenticationInitializer<Session, User> setHomePath(String homePath) {
        this.configuration.homePath = homePath;
        return this;
    }

    public AuthenticationInitializer<Session, User> setLoginPath(String loginPath) {
        this.configuration.loginPath = loginPath;
        return this;
    }

    public AuthenticationInitializer<Session, User> setAuthenticationCookieName(String cookieName) {
        this.configuration.authenticationCookieName = cookieName;
        return this;
    }

    public void initialize() {
        if (this.configuration.oauthProviders.size() == 0) {
            throw new RuntimeException("No oauth providers registered");
        }
        if (this.configuration.authenticationHandler == null) {
            throw new RuntimeException("Authentication handler is not set");
        }
        if (this.configuration.sessionHandler == null) {
            throw new RuntimeException("Session handler is not set");
        }
        if (this.configuration.userHandler == null) {
            throw new RuntimeException("User handler is not set");
        }
        if (this.configuration.securityHandler == null) {
            this.configuration.securityHandler = new ZeroSecurityHandler<>();

        }
        if (this.configuration.securityHandler instanceof ZeroSecurityHandler) {
            LOGGER.log(Level.SEVERE, "*** DO NOT USE ZERO SECURITY PROVIDER IN PRODUCTION MODE ***");
        }
        if (this.configuration.homePath == null) {
            this.configuration.homePath = "/";
            LOGGER.log(Level.INFO, "Home path was not set. Setting to default '/'");
        }
        if (this.configuration.loginPath == null) {
            this.configuration.loginPath = "/login";
            LOGGER.log(Level.INFO, "Login path was not set. Setting to default '/login'");
        }
        if (this.configuration.authenticationCookieName == null) {
            this.configuration.authenticationCookieName = "auth-cookie";
            LOGGER.log(Level.INFO, "Cookie name was not set. Setting to default 'auth-cookie'");
        }
        AuthenticationConfiguration.configuration = this.configuration;
    }
}