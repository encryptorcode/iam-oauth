package io.github.encryptorcode.iam.example.services;

import io.github.encryptorcode.iam.example.entities.Session;
import io.github.encryptorcode.iam.example.entities.User;
import io.github.encryptorcode.iam.implementation.DefaultOauthStrategy;
import io.github.encryptorcode.iam.oauth.OauthStrategy;
import io.github.encryptorcode.iam.service.AuthenticationHelper;
import io.github.encryptorcode.iam.service.AuthenticationService;

import java.util.ArrayList;
import java.util.List;

public class AuthenticationManager extends AuthenticationHelper<User, Session> {

    public static final List<String> strategyNames = new ArrayList<>();
    private static final List<OauthStrategy> strategies = new ArrayList<>();
    private static AuthenticationService<User, Session> authenticationService;

    static {
        OauthStrategy googleStrategy = new DefaultOauthStrategy(new GoogleAuthentication());
        strategies.add(googleStrategy);
        strategyNames.add(googleStrategy.getStrategyName());
    }

    public static AuthenticationService<User, Session> getAuthenticationService() {
        if (authenticationService == null) {
            authenticationService = new AuthenticationService<>(new AuthenticationManager());
        }
        return authenticationService;
    }

    @Override
    public String getLoginPagePath() {
        return "/login";
    }

    @Override
    public List<OauthStrategy> getOauthStrategies() {
        return strategies;
    }

    @Override
    public UserService getUserService() {
        return new UserService();
    }

    @Override
    public SessionService getSessionStorage() {
        return new SessionService();
    }
}
