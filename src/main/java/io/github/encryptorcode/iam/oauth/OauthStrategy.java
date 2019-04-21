package io.github.encryptorcode.iam.oauth;

public interface OauthStrategy {
    String getStrategyName();
    String getAuthenticationUrl(String state);
    OauthToken generateToken(String grantCode) throws OAuthException;
    OauthToken regenerateToken(String refreshToken) throws OAuthException;
    void revokeToken(String refreshToken) throws OAuthException;
    OauthUser getUser(String accessToken) throws OAuthException;
}
