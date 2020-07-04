package io.github.encryptorcode.implementation.storage.file;

import io.github.encryptorcode.entity.ASession;
import io.github.encryptorcode.entity.AUser;
import io.github.encryptorcode.handlers.ASessionHandler;

import java.util.HashMap;

/**
 * A ready made implementation for handling storage for {@link Session} using files
 */
public class FileSessionHandler<Session extends ASession, User extends AUser> extends ASessionHandler<Session, User> {

    private final HashMap<String, Session> data;
    private final String filePath;

    public FileSessionHandler(String filePath) {
        this.filePath = filePath;
        //noinspection unchecked
        this.data = (HashMap<String, Session>) FileUtils.readFromFile(filePath).orElse(new HashMap<>());
    }

    @Override
    public Session getSession(String identifier) {
        return data.get(identifier);
    }

    @Override
    public Session createSession(Session session) {
        data.put(session.getIdentifier(), session);
        saveToStorage();
        //noinspection unchecked
        return (Session) session.clone();
    }

    @Override
    public void deleteSession(String identifier) {
        data.remove(identifier);
        saveToStorage();
    }

    private void saveToStorage() {
        FileUtils.writeToFile(filePath, data);
    }
}
