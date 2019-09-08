package io.github.encryptorcode.iam.example.services;

import io.github.encryptorcode.iam.example.entities.User;
import io.github.encryptorcode.iam.user.AuthUserService;

import java.util.HashMap;
import java.util.Map;

public class UserService implements AuthUserService<User> {

    private static final Map<String, User> EMAIL_VS_USER_MAP = new HashMap<>();

    @Override
    public User getUserByEmail(String email) {
        return EMAIL_VS_USER_MAP.get(email);
    }

    @Override
    public User createUser(String email, String name, String fullName, Map<String, String> strategyVsIdMap, String profileImage) {
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setFullName(fullName);
        user.setStrategyVsIdMap(strategyVsIdMap);
        user.setProfileImage(profileImage);
        EMAIL_VS_USER_MAP.put(email, user);
        return user;
    }

    @Override
    public void updateUser(User user) {
        EMAIL_VS_USER_MAP.put(user.getEmail(), user);
    }
}
