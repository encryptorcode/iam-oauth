package io.github.encryptorcode.implementation.storage.jdbc.mappers;

import io.github.encryptorcode.entity.ASession;
import io.github.encryptorcode.implementation.storage.jdbc.JdbcConfiguration;
import org.jooq.Record;
import org.jooq.RecordMapper;

import static io.github.encryptorcode.implementation.storage.jdbc.tables.SESSIONS.SESSIONS;

/**
 * A mapper implementation that helps JOOQ to convert {@link Record} to a proper POJO of {@link Session}
 */
public class SessionsMapper<Session extends ASession> implements RecordMapper<Record, Session> {

    private final JdbcConfiguration.ConstructionHelper<Session> constructionHelper;

    public SessionsMapper(JdbcConfiguration.ConstructionHelper<Session> constructionHelper) {
        this.constructionHelper = constructionHelper;
    }

    @Override
    public Session map(Record record) {
        if (record == null) {
            return null;
        }
        Session session = constructionHelper.construct();
        session.setIdentifier(record.get(SESSIONS.IDENTIFIER));
        session.setUserId(record.get(SESSIONS.USER_ID));
        session.setProviderId(record.get(SESSIONS.PROVIDER_ID));
        session.setCreationTime(record.get(SESSIONS.CREATION_TIME));
        session.setExpiryTime(record.get(SESSIONS.EXPIRY_TIME));
        return session;
    }
}
