package io.github.encryptorcode.implementation.security;

import io.github.encryptorcode.entity.AUser;
import io.github.encryptorcode.handlers.ASecurityHandler;
import io.github.encryptorcode.service.SecurityUtil;

import java.util.Random;

public class ZeroSecurityHandler<User extends AUser> extends ASecurityHandler<User> {

    private final Random random = new Random();

    @Override
    public String generateIdentifier(User user) {
        byte[] bytes = new byte[16];
        random.nextBytes(bytes);
        return SecurityUtil.bytesToHex(bytes);
    }

    @Override
    public int getSessionExpiration(User user) {
        return 60 * 60 * 24 * 10; // default is 10 days
    }

    @Override
    public String encryptCookieValue(String cookieValue) {
        return cookieValue;
    }

    @Override
    public String decryptCookieValue(String encryptedValue) {
        return encryptedValue;
    }
}
