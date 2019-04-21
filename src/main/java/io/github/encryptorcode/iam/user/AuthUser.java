package io.github.encryptorcode.iam.user;

import java.util.Map;

public interface AuthUser {
    String getName();
    String getFullName();
    String getEmail();
    Map<String,String> getStrategyVsIdMap();
    String getProfileImage();

    void setName(String name);
    void setFullName(String fullName);
    void setEmail(String email);
    void setStrategyVsIdMap(Map<String, String> signUpStrategyMap);
    void setProfileImage(String profileImage);

}
