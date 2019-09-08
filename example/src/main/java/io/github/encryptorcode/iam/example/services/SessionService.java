package io.github.encryptorcode.iam.example.services;

import io.github.encryptorcode.iam.example.entities.Session;
import io.github.encryptorcode.iam.example.entities.User;
import io.github.encryptorcode.iam.oauth.OauthToken;
import io.github.encryptorcode.iam.session.AuthSessionStorage;

import java.util.HashMap;
import java.util.Map;

public class SessionService implements AuthSessionStorage<User, Session> {

    private static final Map<String, Session> IDENTIFIER_VS_SESSION_MAP = new HashMap<>();

    @Override
    public Session getSession(String identifier) {
        return IDENTIFIER_VS_SESSION_MAP.get(identifier);
    }

    @Override
    public void createSession(String sessionIdentifier, String strategyName, OauthToken token, User authUser) {
        Session session = new Session();
        session.setIdentifier(sessionIdentifier);
        session.setOauthStrategy(strategyName);
        session.setToken(token);
        session.setUser(authUser);
        IDENTIFIER_VS_SESSION_MAP.put(sessionIdentifier, session);
    }

    @Override
    public void updateSessionToken(String identifier, OauthToken token) {
        Session session = IDENTIFIER_VS_SESSION_MAP.get(identifier);
        if(session != null){
            session.setToken(token);
        }
    }

    @Override
    public void updateSessionAccessed(String identifier) {

    }

    @Override
    public void deleteSession(String identifier) {
        IDENTIFIER_VS_SESSION_MAP.remove(identifier);
    }
}
