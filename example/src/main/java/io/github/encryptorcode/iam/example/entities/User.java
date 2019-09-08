package io.github.encryptorcode.iam.example.entities;

import io.github.encryptorcode.iam.user.AuthUser;

import java.util.Map;

public class User implements AuthUser {
    private String name;
    private String fullName;
    private String email;
    private Map<String, String> strategyVsIdMap;
    private String profileImage;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getFullName() {
        return fullName;
    }

    @Override
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public Map<String, String> getStrategyVsIdMap() {
        return strategyVsIdMap;
    }

    @Override
    public void setStrategyVsIdMap(Map<String, String> strategyVsIdMap) {
        this.strategyVsIdMap = strategyVsIdMap;
    }

    @Override
    public String getProfileImage() {
        return profileImage;
    }

    @Override
    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", strategyVsIdMap=" + strategyVsIdMap +
                ", profileImage='" + profileImage + '\'' +
                '}';
    }
}
