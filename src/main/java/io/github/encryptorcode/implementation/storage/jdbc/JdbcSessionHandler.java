package io.github.encryptorcode.implementation.storage.jdbc;

import io.github.encryptorcode.entity.ASession;
import io.github.encryptorcode.entity.AUser;
import io.github.encryptorcode.handlers.ASessionHandler;
import org.jooq.DSLContext;

import static io.github.encryptorcode.implementation.storage.jdbc.tables.SESSIONS.SESSIONS;

/**
 * JDBC implementation of {@link ASessionHandler} using JOOQ
 *
 * @param <Session> session instance
 * @param <User>    user instance
 */
public abstract class JdbcSessionHandler<Session extends ASession, User extends AUser> extends ASessionHandler<Session, User> {

    private final JdbcConfiguration<Session, User> configuration;
    private final DSLContext context;

    public JdbcSessionHandler(JdbcConfiguration<Session, User> configuration) {
        this.configuration = configuration;
        this.context = configuration.context();
    }

    @Override
    public Session getSession(String identifier) {
        return context.select()
                .from(SESSIONS)
                .where(SESSIONS.IDENTIFIER.eq(identifier))
                .fetchOne(configuration.getSessionsMapper());
    }

    @Override
    public Session createSession(Session session) {
        context.insertInto(SESSIONS,
                SESSIONS.IDENTIFIER,
                SESSIONS.USER_ID,
                SESSIONS.PROVIDER_ID,
                SESSIONS.CREATION_TIME,
                SESSIONS.EXPIRY_TIME)
                .values(
                        session.getIdentifier(),
                        session.getUserId(),
                        session.getProviderId(),
                        session.getCreationTime(),
                        session.getExpiryTime()
                ).execute();
        //noinspection unchecked
        return (Session) session.clone();
    }

    @Override
    public void deleteSession(String identifier) {
        context.deleteFrom(SESSIONS).where(SESSIONS.IDENTIFIER.eq(identifier)).execute();
    }
}
