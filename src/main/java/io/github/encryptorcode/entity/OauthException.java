package io.github.encryptorcode.entity;

public class OauthException extends Exception {
    public OauthException(String message) {
        super(message);
    }

    public OauthException(String message, Throwable cause) {
        super(message, cause);
    }
}
