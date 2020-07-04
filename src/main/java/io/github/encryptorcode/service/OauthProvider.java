package io.github.encryptorcode.service;

import io.github.encryptorcode.entity.OauthToken;
import io.github.encryptorcode.entity.OauthUser;

public interface OauthProvider {
    String id();
    String getAuthenticationUrl(String state, boolean showConsent);
    OauthToken generateToken(String grantCode);
    OauthToken regenerateToken(String refreshToken);
    void revokeToken(String refreshToken);
    OauthUser getUser(String accessToken);
}
