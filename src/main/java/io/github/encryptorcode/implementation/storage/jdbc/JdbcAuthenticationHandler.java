package io.github.encryptorcode.implementation.storage.jdbc;

import io.github.encryptorcode.entity.ASession;
import io.github.encryptorcode.entity.AUser;
import io.github.encryptorcode.entity.AuthenticationDetail;
import io.github.encryptorcode.handlers.AAuthenticationHandler;
import org.jooq.DSLContext;

import static io.github.encryptorcode.implementation.storage.jdbc.tables.AUTHENTICATION_DETAILS.AUTHENTICATION_DETAILS;

/**
 * JDBC implementation of {@link AAuthenticationHandler} using JOOQ
 *
 * @param <Session> session instance
 * @param <User>    user instance
 */
public class JdbcAuthenticationHandler<Session extends ASession, User extends AUser> extends AAuthenticationHandler {

    private final JdbcConfiguration<Session, User> configuration;
    private final DSLContext context;

    public JdbcAuthenticationHandler(JdbcConfiguration<Session, User> configuration) {
        this.configuration = configuration;
        this.context = configuration.context();
    }

    @Override
    public AuthenticationDetail getAuthenticationDetail(String userId, String providerId) {
        return context.select()
                .from(AUTHENTICATION_DETAILS)
                .where(AUTHENTICATION_DETAILS.USER_ID.eq(userId))
                .and(AUTHENTICATION_DETAILS.PROVIDER.eq(providerId))
                .fetchOne(configuration.getAuthenticationDetailsMapper());
    }

    @Override
    public AuthenticationDetail create(AuthenticationDetail detail) {
        context.insertInto(AUTHENTICATION_DETAILS,
                AUTHENTICATION_DETAILS.USER_ID,
                AUTHENTICATION_DETAILS.PROVIDER,
                AUTHENTICATION_DETAILS.PROVIDED_USER_ID,
                AUTHENTICATION_DETAILS.ACCESS_TOKEN,
                AUTHENTICATION_DETAILS.EXPIRY_TIME,
                AUTHENTICATION_DETAILS.REFRESH_TOKEN)
                .values(
                        detail.getUserId(),
                        detail.getProvider(),
                        detail.getProvidedUserId(),
                        detail.getAccessToken(),
                        detail.getExpiryTime(),
                        detail.getRefreshToken()
                ).execute();
        return detail.clone();
    }

    @Override
    public AuthenticationDetail update(AuthenticationDetail detail) {
        context.update(AUTHENTICATION_DETAILS)
                .set(AUTHENTICATION_DETAILS.ACCESS_TOKEN, detail.getAccessToken())
                .set(AUTHENTICATION_DETAILS.EXPIRY_TIME, detail.getExpiryTime())
                .set(AUTHENTICATION_DETAILS.REFRESH_TOKEN, detail.getRefreshToken())
                .where(AUTHENTICATION_DETAILS.USER_ID.eq(detail.getUserId()))
                .and(AUTHENTICATION_DETAILS.PROVIDER.eq(detail.getProvider()))
                .execute();
        return detail.clone();
    }

    @Override
    public void delete(AuthenticationDetail detail) {
        context.delete(AUTHENTICATION_DETAILS)
                .where(AUTHENTICATION_DETAILS.USER_ID.eq(detail.getUserId()))
                .and(AUTHENTICATION_DETAILS.PROVIDER.eq(detail.getProvider()))
                .execute();
    }
}
