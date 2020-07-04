package io.github.encryptorcode.implementation.storage.jdbc.mappers;

import io.github.encryptorcode.entity.ASession;
import io.github.encryptorcode.service.AuthenticationConfiguration;
import org.jooq.Record;
import org.jooq.RecordMapper;

import static io.github.encryptorcode.implementation.storage.jdbc.tables.SESSIONS.SESSIONS;

/**
 * A mapper implementation that helps JOOQ to convert {@link Record} to a proper POJO of {@link Session}
 */
public class SessionsMapper<Session extends ASession> implements RecordMapper<Record, Session> {

    @Override
    public Session map(Record record) {
        if (record == null) {
            return null;
        }

        @SuppressWarnings("unchecked")
        Session session = (Session) AuthenticationConfiguration.configuration.sessionConstructor.construct();
        session.setIdentifier(record.get(SESSIONS.IDENTIFIER));
        session.setUserId(record.get(SESSIONS.USER_ID));
        session.setProviderId(record.get(SESSIONS.PROVIDER_ID));
        session.setCreationTime(record.get(SESSIONS.CREATION_TIME));
        session.setExpiryTime(record.get(SESSIONS.EXPIRY_TIME));
        return session;
    }
}
