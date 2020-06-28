package io.github.encryptorcode.entity;

import com.google.gson.annotations.SerializedName;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * A User entity used for user details. We have made this abstract to maintain extensibility.
 */
@Entity
@Table(name = "USERS")
public abstract class AUser implements Cloneable, Serializable {

    @SerializedName("user_id")
    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "NAME")
    private String name;

    @SerializedName("full_name")
    @Column(name = "FULL_NAME")
    private String fullName;

    @Column(name = "EMAIL")
    private String email;

    @SerializedName("profile_image")
    @Column(name = "PROFILE_IMAGE")
    private String profileImage;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    @Override
    public AUser clone() {
        try {
            return (AUser) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
