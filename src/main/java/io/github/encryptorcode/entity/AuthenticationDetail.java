package io.github.encryptorcode.entity;

import com.google.gson.annotations.SerializedName;
import io.github.encryptorcode.extra.NoSerialization;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A Session entity used for storing user sessions. We made this abstract to maintain extensibility.
 */
@Entity
@Table(name = "AUTHENTICATION_DETAILS")
public class AuthenticationDetail implements Cloneable, Serializable {

    @Id
    @SerializedName("user_id")
    @Column(name = "USER_ID")
    private String userId;

    @Id
    @Column(name = "PROVIDER")
    private String provider;

    @NoSerialization
    @Column(name = "PROVIDED_USER_ID")
    private String providedUserId;

    @NoSerialization
    @Column(name = "ACCESS_TOKEN")
    private String accessToken;

    @Column(name = "EXPIRY_TIME")
    private ZonedDateTime expiryTime;

    @NoSerialization
    @Column(name = "REFRESH_TOKEN")
    private String refreshToken;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getProvidedUserId() {
        return providedUserId;
    }

    public void setProvidedUserId(String providedUserId) {
        this.providedUserId = providedUserId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public ZonedDateTime getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(ZonedDateTime expiryTime) {
        this.expiryTime = expiryTime;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public AuthenticationDetail clone() {
        try {
            return (AuthenticationDetail) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
