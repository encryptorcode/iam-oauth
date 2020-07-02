package io.github.encryptorcode.implementation.security;

import io.github.encryptorcode.entity.AUser;
import io.github.encryptorcode.handlers.ASecurityHandler;
import io.github.encryptorcode.service.SecurityUtil;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class BaseSecurityHandler<User extends AUser> extends ASecurityHandler<User> {

    private static final Logger LOGGER = Logger.getLogger(BaseSecurityHandler.class.getName());
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String KEY_TYPE = "AES";

    protected abstract String getEncryptionKey();

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
            byte[] ivBytes = SecurityUtil.generateRandomBytes(16);
            SecretKeySpec key = new SecretKeySpec(getEncryptionKey().getBytes(), KEY_TYPE);
            IvParameterSpec iv = new IvParameterSpec(ivBytes);
            Cipher encrypt = Cipher.getInstance(ALGORITHM);
            encrypt.init(Cipher.ENCRYPT_MODE, key, iv);
            byte[] rawBytes = SecurityUtil.hexToBytes(cookieValue);
            byte[] encrypted = encrypt.doFinal(rawBytes);
            return SecurityUtil.bytesToHex(encrypted) + "-" + SecurityUtil.bytesToHex(ivBytes);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e) {
            LOGGER.log(Level.SEVERE, "Failed to encrypt the cookie value. So returning null", e);
            return null;
        }
    }

    @Override
    public String decryptCookieValue(String encryptedValue) {
        try {
            String[] split = encryptedValue.split("-");
            byte[] encrypted = SecurityUtil.hexToBytes(split[0]);
            byte[] ivBytes = SecurityUtil.hexToBytes(split[1]);
            SecretKey key = new SecretKeySpec(getEncryptionKey().getBytes(), KEY_TYPE);
            IvParameterSpec iv = new IvParameterSpec(ivBytes);
            Cipher decrypt = Cipher.getInstance(ALGORITHM);
            decrypt.init(Cipher.DECRYPT_MODE, key, iv);
            byte[] rawBytes = decrypt.doFinal(encrypted);
            return SecurityUtil.bytesToHex(rawBytes);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException | ArrayIndexOutOfBoundsException e) {
            LOGGER.log(Level.SEVERE, "Failed to decrypt the cookie value. So returning null", e);
            return null;
        }
    }
}
