package io.github.encryptorcode.implementation.storage.jdbc;

import io.github.encryptorcode.entity.ASession;
import io.github.encryptorcode.entity.AUser;
import io.github.encryptorcode.implementation.storage.jdbc.mappers.AuthenticationDetailsMapper;
import io.github.encryptorcode.implementation.storage.jdbc.mappers.SessionsMapper;
import io.github.encryptorcode.implementation.storage.jdbc.mappers.UsersMapper;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;

/**
 * Configuration class to be extended for setting JDBC configurations
 *
 * @param <Session> Session template
 * @param <User> User template
 */
public abstract class JdbcConfiguration<Session extends ASession, User extends AUser> {

    // database
    protected abstract Connection getConnection();

    protected SQLDialect getDialect() {
        return SQLDialect.MYSQL;
    }

    // construction helpers
    protected abstract ConstructionHelper<User> userConstructionHelper();

    protected abstract ConstructionHelper<Session> sessionConstructionHelper();

    // jooq mapper helpers
    protected AuthenticationDetailsMapper getAuthenticationDetailsMapper() {
        return authenticationDetailsMapper;
    }

    protected SessionsMapper<Session> getSessionsMapper() {
        return sessionsMapper;
    }

    protected UsersMapper<User> getUsersMapper() {
        return usersMapper;
    }

    // <editor-fold desc="Internal implementation" defaultstate="collapsed">
    private final AuthenticationDetailsMapper authenticationDetailsMapper = new AuthenticationDetailsMapper();
    private final SessionsMapper<Session> sessionsMapper = new SessionsMapper<>(sessionConstructionHelper());
    private final UsersMapper<User> usersMapper = new UsersMapper<>(userConstructionHelper());
    private final DSLContext context = DSL.using(getConnection(), getDialect());

    DSLContext context() {
        return context;
    }

    public interface ConstructionHelper<T> {
        T construct();
    }
    // </editor-fold>
}
