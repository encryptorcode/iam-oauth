package io.github.encryptorcode.implementation.storage.redis;

import com.google.gson.*;
import io.github.encryptorcode.implementation.storage.DateTimeHandler;
import redis.clients.jedis.Jedis;

import java.time.ZonedDateTime;

/**
 * Configuration class to be extended for setting Redis configurations
 */
public abstract class RedisConfiguration {
    public abstract Jedis getJedis();

    /**
     * A prefix to be added to all keys stored to redis by {@link RedisHandler}
     *
     * @return prefix string
     */
    public String getKeyPrefix() {
        return "auth";
    }

    /**
     * Expiry time to for each key to expire.
     *
     * @return expiry time in seconds, or null for no expiry
     */
    public Integer expiryTime() {
        return null;
    }

    /**
     * Gson instance used to convert POJO's to string to store in redis
     *
     * @return {@link Gson} object
     */
    public Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(ZonedDateTime.class, (JsonSerializer<ZonedDateTime>) (src, typeOfSrc, context) -> new JsonPrimitive(DateTimeHandler.toMillis(src)))
                .registerTypeAdapter(ZonedDateTime.class, (JsonDeserializer<ZonedDateTime>) (json, typeOfT, context) -> DateTimeHandler.toZonedDateTime(json.getAsLong()))
                .create();
    }
}
