package io.github.encryptorcode.implementation.storage.jdbc.mappers;

import io.github.encryptorcode.entity.AUser;
import io.github.encryptorcode.service.AuthenticationConfiguration;
import org.jooq.Record;
import org.jooq.RecordMapper;

import static io.github.encryptorcode.implementation.storage.jdbc.tables.USERS.USERS;

/**
 * A mapper implementation that helps JOOQ to convert {@link Record} to a proper POJO of {@link User}
 */
public class UsersMapper<User extends AUser> implements RecordMapper<Record, User> {

    @Override
    public User map(Record record) {
        if (record == null) {
            return null;
        }

        @SuppressWarnings("unchecked")
        User user = (User) AuthenticationConfiguration.configuration.userConstructor.construct();
        user.setUserId(record.get(USERS.USER_ID));
        user.setName(record.get(USERS.NAME));
        user.setFullName(record.get(USERS.FULL_NAME));
        user.setEmail(record.get(USERS.EMAIL));
        user.setProfileImage(record.get(USERS.PROFILE_IMAGE));
        return user;
    }
}
