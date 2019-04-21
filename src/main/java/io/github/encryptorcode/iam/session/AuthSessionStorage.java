package io.github.encryptorcode.iam.session;

import io.github.encryptorcode.iam.oauth.OauthToken;
import io.github.encryptorcode.iam.user.AuthUser;

public interface AuthSessionStorage<U extends AuthUser, S extends AuthSession> {
    S getSession(String identifier);
    void createSession(String sessionIdentifier, String strategyName, OauthToken token, U authUser);
    void updateSessionToken(String identifier, OauthToken token);
    void updateSessionAccessed(String identifier);
    void deleteSession(String identifier);
}
