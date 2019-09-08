package io.github.encryptorcode.iam.example.entities;

import io.github.encryptorcode.iam.oauth.OauthToken;
import io.github.encryptorcode.iam.session.AuthSession;

public class Session implements AuthSession<User> {

    private String identifier;
    private String oauthStrategy;
    private User user;
    private OauthToken token;

    @Override
    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String getOauthStrategy() {
        return oauthStrategy;
    }

    public void setOauthStrategy(String oauthStrategy) {
        this.oauthStrategy = oauthStrategy;
    }

    @Override
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public OauthToken getToken() {
        return token;
    }

    public void setToken(OauthToken token) {
        this.token = token;
    }
}
