package io.github.encryptorcode.implementation.storage.jdbc.converters;

import io.github.encryptorcode.implementation.storage.DateTimeHandler;
import org.jooq.Converter;

import java.time.ZonedDateTime;

/**
 * A JOOQ convertor used to convert {@link ZonedDateTime} to {@link Long} for storing time as long in database
 */
public class ZonedDateTimeConvertor implements Converter<Long, ZonedDateTime> {

    private ZonedDateTimeConvertor() {
    }

    private static ZonedDateTimeConvertor convertor;

    public static ZonedDateTimeConvertor instance() {
        if (convertor == null) {
            convertor = new ZonedDateTimeConvertor();
        }
        return convertor;
    }

    @Override
    public ZonedDateTime from(Long databaseObject) {
        return DateTimeHandler.toZonedDateTime(databaseObject);
    }

    @Override
    public Long to(ZonedDateTime userObject) {
        return DateTimeHandler.toMillis(userObject);
    }

    @Override
    public Class<Long> fromType() {
        return Long.class;
    }

    @Override
    public Class<ZonedDateTime> toType() {
        return ZonedDateTime.class;
    }
}
