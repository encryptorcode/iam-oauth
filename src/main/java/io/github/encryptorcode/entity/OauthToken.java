package io.github.encryptorcode.entity;

import java.time.ZonedDateTime;

public class OauthToken {
    private Status status;
    private String accessToken;
    private ZonedDateTime expiryTime;
    private String refreshToken;
    private OauthException throwable;

    public enum Status {
        ACCESS_ONLY,
        ACCESS_AND_REFRESH,
        INVALID_CODE
    }

    private OauthToken() {
    }

    public static OauthToken create(String accessToken, ZonedDateTime expiryTime) {
        return create(accessToken, null, expiryTime);
    }

    public static OauthToken create(String accessToken, String refreshToken, ZonedDateTime expiryTime) {
        OauthToken token = new OauthToken();
        token.accessToken = accessToken;
        token.expiryTime = expiryTime;
        if (refreshToken != null) {
            token.refreshToken = refreshToken;
            token.status = Status.ACCESS_AND_REFRESH;
        } else {
            token.status = Status.ACCESS_ONLY;
        }
        return token;
    }

    public static OauthToken error(String message) {
        return error(new OauthException("Failed because of '" + message + "'"));
    }

    public static OauthToken error(Throwable exception) {
        OauthToken token = new OauthToken();
        token.status = Status.INVALID_CODE;
        if (exception instanceof OauthException) {
            token.throwable = (OauthException) exception;
        } else {
            token.throwable = new OauthException("Failed because of an unknown exception", exception);
        }
        return token;
    }

    public Status getStatus() {
        return status;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public ZonedDateTime getExpiryTime() {
        return expiryTime;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public OauthException getThrowable() {
        return throwable;
    }
}
