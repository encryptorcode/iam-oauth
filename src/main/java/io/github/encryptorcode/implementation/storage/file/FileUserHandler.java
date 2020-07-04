package io.github.encryptorcode.implementation.storage.file;

import io.github.encryptorcode.entity.AUser;
import io.github.encryptorcode.handlers.AUserHandler;

import java.util.ArrayList;
import java.util.Optional;

/**
 * A ready made implementation for handling storage for {@link User} using files
 */
public class FileUserHandler<User extends AUser> extends AUserHandler<User> {

    private final ArrayList<User> data;
    private final String filePath;

    public FileUserHandler(String filePath) {
        this.filePath = filePath;
        //noinspection unchecked
        this.data = (ArrayList<User>) FileUtils.readFromFile(filePath).orElse(new ArrayList<>());
    }

    @Override
    public User getUser(String id) {
        Optional<User> user = data.stream()
                .filter((u) -> id.equals(u.getUserId()))
                .findFirst();
        return user.orElse(null);
    }

    @Override
    public User getUserByEmail(String email) {
        Optional<User> user = data.stream()
                .filter((u) -> email.equals(u.getEmail()))
                .findFirst();
        return user.orElse(null);
    }

    @Override
    public User createUser(User user) {
        data.add(user);
        saveToStorage();
        //noinspection unchecked
        return (User) user.clone();
    }

    private void saveToStorage() {
        FileUtils.writeToFile(filePath, data);
    }
}
