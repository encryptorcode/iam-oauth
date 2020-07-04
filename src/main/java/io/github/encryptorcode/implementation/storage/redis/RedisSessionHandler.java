package io.github.encryptorcode.implementation.storage.redis;

import io.github.encryptorcode.entity.ASession;
import io.github.encryptorcode.entity.AUser;
import io.github.encryptorcode.handlers.ASessionHandler;

/**
 * Redis implementation of {@link ASessionHandler} using Decorator pattern
 *
 * @param <Session> Session template
 * @param <User> User template
 * @see <a href="https://en.wikipedia.org/wiki/Decorator_pattern">Decorator pattern in Wikipedia</a>
 */
public class RedisSessionHandler<Session extends ASession, User extends AUser> extends ASessionHandler<Session, User> {

    private final RedisHandler<Session> redisHandler;
    private final ASessionHandler<Session, User> handler;

    public RedisSessionHandler(RedisConfiguration configuration, Class<Session> clazz, ASessionHandler<Session, User> handler) {
        this(configuration, clazz, handler, "sessions_cache");
    }

    public RedisSessionHandler(RedisConfiguration configuration, Class<Session> clazz, ASessionHandler<Session, User> handler, String key) {
        this.redisHandler = new RedisHandler<>(configuration, clazz, key);
        this.handler = handler;
    }

    @Override
    public Session getSession(String identifier) {
        Session session = redisHandler.get(identifier);
        if (session == null) {
            session = handler.getSession(identifier);
            if (session != null) {
                redisHandler.set(identifier, session);
            }
        }
        return session;
    }

    @Override
    public Session createSession(Session session) {
        Session createdSession = handler.createSession(session);
        redisHandler.set(session.getIdentifier(), session);
        return createdSession;
    }

    @Override
    public void deleteSession(String identifier) {
        handler.deleteSession(identifier);
        redisHandler.delete(identifier);
    }
}
