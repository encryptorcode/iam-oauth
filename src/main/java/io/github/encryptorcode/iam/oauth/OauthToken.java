package io.github.encryptorcode.iam.oauth;

import java.time.ZonedDateTime;

public class OauthToken{
    private String accessToken;
    private String refreshToken;
    private ZonedDateTime expiryTime;

    public OauthToken(String accessToken, String refreshToken, ZonedDateTime expiryTime) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiryTime = expiryTime;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public ZonedDateTime getExpiryTime() {
        return expiryTime;
    }

    public OauthToken setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public OauthToken setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public OauthToken setExpiryTime(ZonedDateTime expiryTime) {
        this.expiryTime = expiryTime;
        return this;
    }
}
