package io.github.encryptorcode.implementation.storage;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DateTimeHandler {
    public static ZonedDateTime toZonedDateTime(long millis){
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault());
    }

    public static long toMillis(ZonedDateTime time){
        return time.toInstant().toEpochMilli();
    }
}
