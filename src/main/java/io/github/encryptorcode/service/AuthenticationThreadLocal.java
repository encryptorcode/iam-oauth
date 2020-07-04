package io.github.encryptorcode.service;

import io.github.encryptorcode.entity.ASession;
import io.github.encryptorcode.entity.AUser;

import javax.servlet.ServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * ThreadLocal handler for Authentication, stores request specific details like current user and current session.
 * Do not use these methods unless you have a complete understanding of ThreadLocal object.
 */
public class AuthenticationThreadLocal {

    public static void clear() {
        THREAD_LOCAL.remove();
    }

    protected static ServletRequest getCurrentRequest() {
        return get("request");
    }

    protected static void setCurrentRequest(ServletRequest request) {
        set("request", request);
    }

    protected static <User extends AUser> User getCurrentUser() {
        return get("user");
    }

    protected static <User extends AUser> void setCurrentUser(User user) {
        set("user", user);
    }

    protected static <Session extends ASession> Session getCurrentSession() {
        return get("session");
    }

    protected static <Session extends ASession> void setCurrentSession(Session session) {
        set("session", session);
    }

    // <editor-fold desc="// Internal Thread local operations" defaultstate="collapsed">
    private static final ThreadLocal<Map<String, Object>> THREAD_LOCAL = new ThreadLocal<>();

    private static <T> T get(String key) {
        Map<String, Object> data = THREAD_LOCAL.get();
        if (data != null) {
            //noinspection unchecked
            return (T) data.get(key);
        } else {
            return null;
        }
    }

    private static <T> void set(String key, T value) {
        Map<String, Object> data = THREAD_LOCAL.get();
        if (data == null) {
            data = new HashMap<>();
            THREAD_LOCAL.set(data);
        }
        data.put(key, value);
    }
    // </editor-fold>
}
