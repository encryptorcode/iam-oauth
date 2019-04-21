package io.github.encryptorcode.iam.oauth;

public class OauthUser{
    private String oauthId;
    private String email;
    private String fullName;
    private String name;
    private String profileImage;

    public OauthUser(String oauthId, String email, String fullName, String name, String profileImage) {
        this.oauthId = oauthId;
        this.email = email;
        this.fullName = fullName;
        this.name = name;
        this.profileImage = profileImage;
    }

    public String getOauthId() {
        return oauthId;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getName() {
        return name;
    }

    public String getProfileImage() {
        return profileImage;
    }
}
