package io.github.encryptorcode.implementation.security;

import io.github.encryptorcode.entity.AUser;
import io.github.encryptorcode.handlers.ASecurityHandler;
import io.github.encryptorcode.service.SecurityUtil;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public abstract class BaseSecurityHandler<User extends AUser> extends ASecurityHandler<User> {

    private Cipher encrypt;
    private Cipher decrypt;

    public BaseSecurityHandler() {
        initEncryption();
    }

    protected abstract String getEncryptionKey();

    protected void initEncryption() {
        try {
            SecretKey key = new SecretKeySpec(getEncryptionKey().getBytes(), "AES");
            this.encrypt = Cipher.getInstance("AES");
            this.encrypt.init(Cipher.ENCRYPT_MODE, key);
            this.decrypt = Cipher.getInstance("AES");
            this.decrypt.init(Cipher.DECRYPT_MODE, key);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to initialise ciphers", e);
        }
    }

    @Override
    public String generateIdentifier(User user) {
        String userId = user.getUserId();
        String currentTime = String.valueOf(System.currentTimeMillis());
        byte[] sessionIdTemplate = String.format("%s_%s_", userId, currentTime).getBytes();
        byte[] randomBytes = SecurityUtil.generateRandomBytes(16);

        byte[] rawSessionIdWithRandom = new byte[sessionIdTemplate.length + randomBytes.length];
        System.arraycopy(sessionIdTemplate, 0, rawSessionIdWithRandom, 0, sessionIdTemplate.length);
        System.arraycopy(randomBytes, 0, rawSessionIdWithRandom, sessionIdTemplate.length, randomBytes.length);

        byte[] sessionDigest = SecurityUtil.generateMD5Digest(rawSessionIdWithRandom);
        return SecurityUtil.bytesToHex(sessionDigest);
    }

    @Override
    public int getSessionExpiration(User user) {
        return 60 * 60 * 24 * 10; // default is 10 days
    }

    @Override
    public String encryptCookieValue(String cookieValue) {
        try {
            byte[] rawBytes = cookieValue.getBytes();
            byte[] encrypted = this.encrypt.doFinal(rawBytes);
            return SecurityUtil.bytesToHex(encrypted);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException("Failed to encrypt cookie value.");
        }
    }

    @Override
    public String decryptCookieValue(String encryptedValue) {
        try {
            byte[] encrypted = SecurityUtil.hexToBytes(encryptedValue);
            byte[] rawBytes = this.decrypt.doFinal(encrypted);
            return new String(rawBytes);
        } catch (BadPaddingException | IllegalBlockSizeException e) {
            throw new RuntimeException("Failed to decrypt cookie value.");
        }
    }
}
