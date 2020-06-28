package io.github.encryptorcode.implementation.storage.redis;

import io.github.encryptorcode.entity.AuthenticationDetail;
import io.github.encryptorcode.storage.AAuthenticationHandler;

/**
 * Redis implementation of {@link AAuthenticationHandler} using Decorator pattern
 *
 * @see <a href="https://en.wikipedia.org/wiki/Decorator_pattern">Decorator pattern in Wikipedia</a>
 */
public class RedisAuthenticationHandler extends AAuthenticationHandler {

    private static final String REDIS_ROOT_KEY = "authentication_details_cache";
    private static final String REDIS_KEY_FORMAT = "%s_%s";
    private final RedisHandler<AuthenticationDetail> redisHandler;
    private final AAuthenticationHandler handler;

    public RedisAuthenticationHandler(RedisConfiguration configuration, AAuthenticationHandler handler) {
        this.redisHandler = new RedisHandler<>(configuration, AuthenticationDetail.class, REDIS_ROOT_KEY);
        this.handler = handler;
    }

    @Override
    public AuthenticationDetail getAuthenticationDetail(String userId, String providerId) {
        String key = String.format(REDIS_KEY_FORMAT, userId, providerId);
        AuthenticationDetail detail = redisHandler.get(key);
        if (detail == null) {
            detail = handler.getAuthenticationDetail(userId, providerId);
            if (detail != null) {
                redisHandler.set(key, detail);
            }
        }
        return detail;
    }

    @Override
    public AuthenticationDetail create(AuthenticationDetail detail) {
        AuthenticationDetail createdDetail = handler.create(detail);
        redisHandler.set(String.format(REDIS_KEY_FORMAT, detail.getUserId(), detail.getProvider()), detail);
        return createdDetail;
    }

    @Override
    public AuthenticationDetail update(AuthenticationDetail detail) {
        AuthenticationDetail createdDetail = handler.update(detail);
        redisHandler.set(String.format(REDIS_KEY_FORMAT, detail.getUserId(), detail.getProvider()), detail);
        return createdDetail;
    }

    @Override
    public void delete(AuthenticationDetail detail) {
        handler.delete(detail);
        redisHandler.delete(String.format(REDIS_KEY_FORMAT, detail.getUserId(), detail.getProvider()));
    }
}
