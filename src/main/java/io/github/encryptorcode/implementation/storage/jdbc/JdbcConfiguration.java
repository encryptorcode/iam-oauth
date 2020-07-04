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
 * @param <User>    User template
 */
public abstract class JdbcConfiguration<Session extends ASession, User extends AUser> {

    /**
     * Connection object required for performing db operations
     *
     * @return Connection object
     */
    protected abstract Connection getConnection();

    /**
     * {@link SQLDialect} type or database type
     *
     * @return database type
     */
    protected SQLDialect getDialect() {
        return SQLDialect.MYSQL;
    }

    /**
     * JOOQ helper implementation for {@link io.github.encryptorcode.entity.AuthenticationDetail}
     *
     * @return Authentication detail mapper object
     */
    protected AuthenticationDetailsMapper getAuthenticationDetailsMapper() {
        return authenticationDetailsMapper;
    }

    /**
     * JOOQ helper implementation for {@link Session}
     *
     * @return Session mapper object
     */
    protected SessionsMapper<Session> getSessionsMapper() {
        return sessionsMapper;
    }

    /**
     * JOOQ helper implementation for {@link User}
     *
     * @return User mapper object
     */
    protected UsersMapper<User> getUsersMapper() {
        return usersMapper;
    }

    // <editor-fold desc="Internal implementation" defaultstate="collapsed">
    private final AuthenticationDetailsMapper authenticationDetailsMapper = new AuthenticationDetailsMapper();
    private final SessionsMapper<Session> sessionsMapper = new SessionsMapper<>();
    private final UsersMapper<User> usersMapper = new UsersMapper<>();
    private final DSLContext context = DSL.using(getConnection(), getDialect());

    DSLContext context() {
        return context;
    }
    // </editor-fold>
}
