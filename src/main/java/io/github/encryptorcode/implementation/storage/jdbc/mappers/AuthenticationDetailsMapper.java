package io.github.encryptorcode.implementation.storage.jdbc.mappers;

import io.github.encryptorcode.entity.AuthenticationDetail;
import org.jooq.Record;
import org.jooq.RecordMapper;

import static io.github.encryptorcode.implementation.storage.jdbc.tables.AUTHENTICATION_DETAILS.AUTHENTICATION_DETAILS;

/**
 * A mapper implementation that helps JOOQ to convert {@link Record} to a proper POJO of {@link AuthenticationDetail}
 */
public class AuthenticationDetailsMapper implements RecordMapper<Record, AuthenticationDetail> {
    @Override
    public AuthenticationDetail map(Record record) {
        if (record == null) {
            return null;
        }
        AuthenticationDetail authenticationDetail = new AuthenticationDetail();
        authenticationDetail.setUserId(record.get(AUTHENTICATION_DETAILS.USER_ID));
        authenticationDetail.setProvider(record.get(AUTHENTICATION_DETAILS.PROVIDER));
        authenticationDetail.setProvidedUserId(record.get(AUTHENTICATION_DETAILS.PROVIDED_USER_ID));
        authenticationDetail.setAccessToken(record.get(AUTHENTICATION_DETAILS.ACCESS_TOKEN));
        authenticationDetail.setExpiryTime(record.get(AUTHENTICATION_DETAILS.EXPIRY_TIME));
        authenticationDetail.setRefreshToken(record.get(AUTHENTICATION_DETAILS.REFRESH_TOKEN));
        return authenticationDetail;
    }
}
