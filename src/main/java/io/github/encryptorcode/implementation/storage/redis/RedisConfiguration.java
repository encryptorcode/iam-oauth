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

    public Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(ZonedDateTime.class, (JsonSerializer<ZonedDateTime>) (src, typeOfSrc, context) -> new JsonPrimitive(DateTimeHandler.toMillis(src)))
                .registerTypeAdapter(ZonedDateTime.class, (JsonDeserializer<ZonedDateTime>) (json, typeOfT, context) -> DateTimeHandler.toZonedDateTime(json.getAsLong()))
                .create();
    }
}
