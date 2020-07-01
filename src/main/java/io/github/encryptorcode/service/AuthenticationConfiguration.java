package io.github.encryptorcode.service;

import io.github.encryptorcode.entity.ASession;
import io.github.encryptorcode.entity.AUser;
import io.github.encryptorcode.handlers.AAuthenticationHandler;
import io.github.encryptorcode.handlers.ASecurityHandler;
import io.github.encryptorcode.handlers.ASessionHandler;
import io.github.encryptorcode.handlers.AUserHandler;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This is the main configuration file, All the core settings for setting up your authentication instance are provided here.
 *
 * @param <Session> Session template
 * @param <User> User template
 */
public class AuthenticationConfiguration<Session extends ASession, User extends AUser> {

    @SuppressWarnings("rawtypes")
    private static AuthenticationConfiguration configuration;

    /**
     * Call this method when immediately after your server starts to initialise authentication layer
     *
     * @param oauthProviderList        list of configured providers
     * @param authenticationHandler    handler for authentication details entity
     * @param sessionHandler           handler for sessions entity
     * @param userHandler              handler for users entity
     * @param homePath                 app home path
     * @param loginPath                app login path
     * @param authenticationCookieName cookie name to be set for authentication
     * @param <Session>                Session implementation
     * @param <User>                   User implementation
     */
    public static <Session extends ASession, User extends AUser> void init(
            List<OauthProvider> oauthProviderList,
            ASecurityHandler<User> securityHandler,
            AAuthenticationHandler authenticationHandler,
            ASessionHandler<Session, User> sessionHandler,
            AUserHandler<User> userHandler,
            String homePath,
            String loginPath,
            String authenticationCookieName
    ) {
        configuration = new AuthenticationConfiguration<>(
                oauthProviderList.stream().collect(Collectors.toMap(OauthProvider::id, provider -> provider)),
                securityHandler,
                authenticationHandler,
                sessionHandler,
                userHandler,
                homePath,
                loginPath,
                authenticationCookieName
        );
    }

    public static <Session extends ASession, User extends AUser> AuthenticationConfiguration<Session, User> getConfiguration() {
        //noinspection unchecked
        return (AuthenticationConfiguration<Session, User>) AuthenticationConfiguration.configuration;
    }

    final Map<String, OauthProvider> oauthProviders;
    final ASecurityHandler<User> securityHandler;
    final AAuthenticationHandler authenticationHandler;
    final ASessionHandler<Session, User> sessionHandler;
    final AUserHandler<User> userHandler;
    final String homePath;
    final String loginPath;
    final String authenticationCookieName;

    private AuthenticationConfiguration(
            Map<String, OauthProvider> oauthProviders,
            ASecurityHandler<User> securityHandler,
            AAuthenticationHandler authenticationHandler,
            ASessionHandler<Session, User> sessionHandler,
            AUserHandler<User> userHandler,
            String homePath,
            String loginPath,
            String authenticationCookieName
    ) {
        this.oauthProviders = oauthProviders;
        this.securityHandler = securityHandler;
        this.authenticationHandler = authenticationHandler;
        this.sessionHandler = sessionHandler;
        this.userHandler = userHandler;
        this.homePath = homePath;
        this.loginPath = loginPath;
        this.authenticationCookieName = authenticationCookieName;
    }
}
