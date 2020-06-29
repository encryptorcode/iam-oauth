package io.github.encryptorcode.implementation.storage.redis;

import com.google.gson.Gson;
import redis.clients.jedis.Jedis;

import java.util.Collections;
import java.util.List;

/**
 * Implementation to store Java objects as JSON in a HashMap in redis
 * @param <T> Template
 */
public class RedisHandler<T> {

    private final Jedis jedis;
    private final Gson gson;
    private final Class<T> clazz;
    private final String rootKey;

    public RedisHandler(RedisConfiguration configuration, Class<T> clazz, String rootKey) {
        this.jedis = configuration.getJedis();
        this.gson = configuration.getGson();
        this.clazz = clazz;
        this.rootKey = rootKey;
    }

    public T get(String id) {
        List<String> results = jedis.hmget(rootKey, id);
        if (results.size() != 1) {
            return null;
        }
        return gson.fromJson(results.get(0), clazz);
    }

    public void set(String id, T data) {
        String jsonData = gson.toJson(data);
        jedis.hmset(rootKey, Collections.singletonMap(id, jsonData));
    }

    public void delete(String id){
        jedis.hdel(rootKey, id);
    }
}
