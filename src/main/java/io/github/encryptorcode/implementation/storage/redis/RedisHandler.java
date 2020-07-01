package io.github.encryptorcode.implementation.storage.redis;

import com.google.gson.Gson;
import redis.clients.jedis.Jedis;

/**
 * Implementation to store Java objects as JSON in a HashMap in redis
 *
 * @param <T> Template
 */
public class RedisHandler<T> {

    private final Jedis jedis;
    private final Gson gson;
    private final String prefix;
    private final Integer expiry;
    private final Class<T> clazz;
    private final String rootKey;

    public RedisHandler(RedisConfiguration configuration, Class<T> clazz, String rootKey) {
        this.jedis = configuration.getJedis();
        this.gson = configuration.getGson();
        this.prefix = configuration.getKeyPrefix();
        this.expiry = configuration.expiryTime();
        this.clazz = clazz;
        this.rootKey = rootKey;
    }

    public T get(String id) {
        String key = key(id);
        String jsonData = jedis.get(key);
        if (jsonData == null) {
            return null;
        }
        return gson.fromJson(jsonData, clazz);
    }

    public void set(String id, T data) {
        String jsonData = gson.toJson(data);
        String key = key(id);
        jedis.set(key, jsonData);
        if(expiry != null) {
            jedis.expire(key, expiry);
        }
    }

    public void delete(String id) {
        String key = key(id);
        jedis.hdel(rootKey, key);
    }

    private String key(String id) {
        return String.format("%s_%s_%s", prefix, rootKey, id);
    }
}
