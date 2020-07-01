package io.github.encryptorcode.implementation.storage.redis;

import io.github.encryptorcode.entity.AUser;
import io.github.encryptorcode.exceptions.UserNotAllowedException;
import io.github.encryptorcode.handlers.AUserHandler;

/**
 * Redis implementation of {@link AUserHandler} using Decorator pattern
 *
 * @param <User> User template
 * @see <a href="https://en.wikipedia.org/wiki/Decorator_pattern">Decorator pattern in Wikipedia</a>
 */
public class RedisUserHandler<User extends AUser> extends AUserHandler<User> {

    private final RedisHandler<User> redisHandlerByEmail;
    private final RedisHandler<User> redisHandlerById;
    private final AUserHandler<User> handler;

    public RedisUserHandler(RedisConfiguration configuration, Class<User> clazz, AUserHandler<User> handler) {
        this(configuration, clazz, handler, "users_cache_by_email", "users_cache_by_id");
    }

    public RedisUserHandler(RedisConfiguration configuration, Class<User> clazz, AUserHandler<User> handler, String keyForEmailBasedStorage, String keyForIdBasedStorage) {
        this.handler = handler;
        this.redisHandlerByEmail = new RedisHandler<>(configuration, clazz, keyForEmailBasedStorage);
        this.redisHandlerById = new RedisHandler<>(configuration, clazz, keyForIdBasedStorage);
    }

    @Override
    public User constructUser() {
        return handler.constructUser();
    }

    @Override
    public User getUser(String id) {
        User user = redisHandlerById.get(id);
        if (user == null) {
            user = handler.getUser(id);
            if (user != null) {
                redisHandlerById.set(id, user);
                redisHandlerByEmail.set(user.getEmail(), user);
            }
        }
        return user;
    }

    @Override
    public User getUserByEmail(String email) {
        User user = redisHandlerById.get(email);
        if (user == null) {
            user = handler.getUserByEmail(email);
            if (user != null) {
                redisHandlerById.set(user.getUserId(), user);
                redisHandlerByEmail.set(email, user);
            }
        }
        return user;
    }

    @Override
    public User createUser(User user) throws UserNotAllowedException {
        User createdUser = handler.createUser(user);
        redisHandlerById.set(createdUser.getUserId(), createdUser);
        redisHandlerByEmail.set(createdUser.getEmail(), createdUser);
        return createdUser;
    }
}
