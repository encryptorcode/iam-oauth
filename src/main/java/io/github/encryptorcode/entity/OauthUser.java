package io.github.encryptorcode.entity;

public class OauthUser {
    private final String id;
    private final String email;
    private final String name;
    private final String fullName;
    private final String profileImage;

    public OauthUser(String id, String email, String name, String fullName, String profileImage) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.fullName = fullName;
        this.profileImage = profileImage;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        return fullName;
    }

    public String getProfileImage() {
        return profileImage;
    }
}
