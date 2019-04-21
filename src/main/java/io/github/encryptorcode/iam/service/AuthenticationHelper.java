package io.github.encryptorcode.iam.service;

import io.github.encryptorcode.iam.oauth.OauthStrategy;
import io.github.encryptorcode.iam.oauth.OauthUser;
import io.github.encryptorcode.iam.session.AuthSession;
import io.github.encryptorcode.iam.session.AuthSessionStorage;
import io.github.encryptorcode.iam.user.AuthUser;
import io.github.encryptorcode.iam.user.AuthUserService;

import java.util.List;

public abstract class AuthenticationHelper<U extends AuthUser, S extends AuthSession<U>> {
    public abstract String getLoginPagePath();
    public abstract List<OauthStrategy> getOauthStrategies();
    public abstract AuthUserService<U> getUserService();
    public abstract AuthSessionStorage<U,S> getSessionStorage();

    public boolean isUserAllowedSignUp(OauthUser user){
        return true;
    }

    public boolean isUserAllowedLogin(U user){
        return true;
    }
}
