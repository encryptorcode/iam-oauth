package io.github.encryptorcode.iam.user;

import java.util.Map;

public interface AuthUserService<U extends AuthUser> {
    U getUserByEmail(String email);
    void createUser(String email, String name, String fullName, Map<String, String> strategyVsIdMap, String profileImage);
    void updateUser(U user);
}
