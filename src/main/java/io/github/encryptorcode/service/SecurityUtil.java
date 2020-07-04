package io.github.encryptorcode.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class SecurityUtil {

    /**
     * used to generate a uniquely random number.
     */
    private static final SecureRandom SECURE_RANDOM;
    /**
     * used for converting unique bytes to a hash to
     * abstract away the source of the unique random number.
     */
    private static final MessageDigest MESSAGE_DIGEST;

    private final static char[] HEX_ARRAY = "0123456789abcdef".toCharArray();

    static {
        try {
            SECURE_RANDOM = SecureRandom.getInstanceStrong();
            MESSAGE_DIGEST = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to initialize Session service securely", e);
        }
    }

    public static byte[] generateRandomBytes(int size) {
        byte[] randomBytes = new byte[size];
        SECURE_RANDOM.nextBytes(randomBytes);
        return randomBytes;
    }

    public static byte[] generateMD5Digest(byte[] bytes) {
        return MESSAGE_DIGEST.digest(bytes);
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] hexToBytes(String hex) {
        try {
            byte[] bytes = new byte[hex.length() / 2];
            for (int i = 0; i < bytes.length; i++) {
                int index = i * 2;
                int j = Integer.parseInt(hex.substring(index, index + 2), 16);
                bytes[i] = (byte) j;
            }
            return bytes;
        } catch (NumberFormatException e) {
            return new byte[0];
        }
    }

}
