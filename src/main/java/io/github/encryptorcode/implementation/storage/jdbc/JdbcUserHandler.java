package io.github.encryptorcode.implementation.storage.jdbc;

import io.github.encryptorcode.entity.ASession;
import io.github.encryptorcode.entity.AUser;
import io.github.encryptorcode.storage.AUserHandler;
import org.jooq.DSLContext;

import static io.github.encryptorcode.implementation.storage.jdbc.tables.USERS.USERS;

/**
 * JDBC implementation of {@link AUserHandler} using JOOQ
 *
 * @param <Session> session instance
 * @param <User>    user instance
 */
public abstract class JdbcUserHandler<Session extends ASession, User extends AUser> extends AUserHandler<User> {

    private final JdbcConfiguration<Session, User> configuration;
    private final DSLContext context;

    public JdbcUserHandler(JdbcConfiguration<Session, User> configuration) {
        this.configuration = configuration;
        this.context = configuration.context();
    }

    @Override
    public User getUser(String id) {
        return context.select()
                .from(USERS)
                .where(USERS.USER_ID.eq(id))
                .fetchOne(configuration.getUsersMapper());
    }

    @Override
    public User getUserByEmail(String email) {
        return context.select()
                .from(USERS)
                .where(USERS.EMAIL.eq(email))
                .fetchOne(configuration.getUsersMapper());
    }

    @Override
    public User createUser(User user) {
        context.insertInto(USERS,
                USERS.USER_ID,
                USERS.NAME,
                USERS.FULL_NAME,
                USERS.EMAIL,
                USERS.PROFILE_IMAGE)
                .values(
                        user.getUserId(),
                        user.getName(),
                        user.getFullName(),
                        user.getEmail(),
                        user.getProfileImage()
                ).execute();

        //noinspection unchecked
        return (User) user.clone();
    }
}
