package io.github.encryptorcode.service;

import io.github.encryptorcode.entity.ASession;
import io.github.encryptorcode.entity.AUser;
import io.github.encryptorcode.handlers.AAuthenticationHandler;
import io.github.encryptorcode.handlers.ASecurityHandler;
import io.github.encryptorcode.handlers.ASessionHandler;
import io.github.encryptorcode.handlers.AUserHandler;
import io.github.encryptorcode.implementation.security.ZeroSecurityHandler;
import io.github.encryptorcode.implementation.storage.file.FileAuthenticationHandler;
import io.github.encryptorcode.implementation.storage.file.FileSessionHandler;
import io.github.encryptorcode.implementation.storage.file.FileUserHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AuthenticationInitializer<Session extends ASession, User extends AUser> {

    private static final Logger LOGGER = Logger.getLogger(AuthenticationInitializer.class.getName());

    public static <Session extends ASession, User extends AUser> AuthenticationInitializer<Session, User> newInstance(ConstructionHelper<Session> sessionConstructionHelper, ConstructionHelper<User> userConstructionHelper) {
        AuthenticationInitializer<Session, User> initializer = new AuthenticationInitializer<>();
        initializer.configuration.sessionConstructor = sessionConstructionHelper;
        initializer.configuration.userConstructor = userConstructionHelper;
        return initializer;
    }

    private final AuthenticationConfiguration<Session, User> configuration;

    private AuthenticationInitializer() {
        this.configuration = new AuthenticationConfiguration<>();
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
        // Mandatory fields not set
        if (this.configuration.oauthProviders.size() == 0) {
            throw new RuntimeException("No oauth providers registered");
        }

        // Setting defaults if not already set
        if (this.configuration.authenticationHandler == null) {
            this.configuration.authenticationHandler = new FileAuthenticationHandler("authentication_details.bin");
        }
        if (this.configuration.sessionHandler == null) {
            this.configuration.sessionHandler = new FileSessionHandler<>("sessions.bin");
        }
        if (this.configuration.userHandler == null) {
            this.configuration.userHandler = new FileUserHandler<>("users.bin");
        }
        if (this.configuration.securityHandler == null) {
            this.configuration.securityHandler = new ZeroSecurityHandler<>();
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

        // Warning messages for settings not recommended for production
        if (this.configuration.authenticationHandler instanceof FileAuthenticationHandler) {
            logNotRecommendedClass(this.configuration.authenticationHandler.getClass());
        }
        if (this.configuration.sessionHandler instanceof FileSessionHandler) {
            logNotRecommendedClass(this.configuration.sessionHandler.getClass());
        }
        if (this.configuration.userHandler instanceof FileUserHandler) {
            logNotRecommendedClass(this.configuration.userHandler.getClass());
        }
        if (this.configuration.securityHandler instanceof ZeroSecurityHandler) {
            logNotRecommendedClass(this.configuration.securityHandler.getClass());
        }
        AuthenticationConfiguration.configuration = this.configuration;
    }

    private void logNotRecommendedClass(Class<?> clazz) {
        LOGGER.log(Level.SEVERE, "*** THIS IS AN IMPORTANT WARNING. PLEASE DO NOT IGNORE THE BELOW LOG ***");
        LOGGER.log(Level.SEVERE, "You are currently using {0}. It is not recommended to use this in production. If this is a production environment, Please consider alternatives.", new String[]{clazz.getSimpleName()});
    }
}