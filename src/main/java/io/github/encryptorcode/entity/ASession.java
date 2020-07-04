package io.github.encryptorcode.entity;

import com.google.gson.annotations.SerializedName;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A Session entity used for storing user sessions. We made this abstract to maintain extensibility.
 */
@Entity
@Table(name = "SESSIONS")
public abstract class ASession implements Cloneable, Serializable {

    @Column(name = "IDENTIFIER")
    private String identifier;

    @SerializedName("user_id")
    @Column(name = "USER_ID")
    private String userId;

    @SerializedName("provider_id")
    @Column(name = "PROVIDER_ID")
    private String providerId;

    @SerializedName("creation_time")
    @Column(name = "CREATION_TIME")
    private ZonedDateTime creationTime;

    @SerializedName("expiry_time")
    @Column(name = "EXPIRY_TIME")
    private ZonedDateTime expiryTime;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public ZonedDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(ZonedDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public ZonedDateTime getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(ZonedDateTime expiryTime) {
        this.expiryTime = expiryTime;
    }

    @Override
    public ASession clone() {
        try {
            return (ASession) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
