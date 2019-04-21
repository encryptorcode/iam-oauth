package io.github.encryptorcode.iam.implementation;

import io.github.encryptorcode.iam.oauth.OAuthException;
import io.github.encryptorcode.iam.oauth.OauthStrategy;
import io.github.encryptorcode.iam.oauth.OauthToken;
import io.github.encryptorcode.iam.oauth.OauthUser;
import io.github.encryptorcode.httpclient.HTTPRequest;
import io.github.encryptorcode.httpclient.HTTPResponse;

import java.io.IOException;

public class DefaultOauthStrategy implements OauthStrategy {

    private OauthStrategyHelper helper;
    private OauthStrategyDetails details;

    public DefaultOauthStrategy(OauthStrategyHelper helper) {
        this.helper = helper;
        this.details = helper.getDetails();
    }

    @Override
    public String getStrategyName() {
        return this.details.getStrategyName();
    }

    @Override
    public String getAuthenticationUrl(String state) {
        return this.details.getLoginUrl() +
                "?client_id=" + this.details.getClientId() +
                "&redirect_uri=" + this.details.getRedirectUri() +
                "&scope=" + this.details.getScope() +
                "&access_type=offline" +
                "&response_type=code" +
                "&prompt=consent" +
                "&state=" + state;
    }

    @Override
    public OauthToken generateToken(String grantCode) throws OAuthException {
        try {
            HTTPResponse response = new HTTPRequest(HTTPRequest.Method.POST, this.details.getTokenUrl())
                    .formParam("code", grantCode)
                    .formParam("client_id", this.details.getClientId())
                    .formParam("client_secret", this.details.getClientSecret())
                    .formParam("redirect_uri", this.details.getRedirectUri())
                    .formParam("grant_type", "authorization_code")
                    .getResponse();

            return this.helper.readToken(response.getData());
        } catch (IOException e) {
            throw new OAuthException("Failed to retrieve access token", e);
        }
    }

    @Override
    public OauthToken regenerateToken(String refreshToken) throws OAuthException {
        try {
            HTTPResponse response = new HTTPRequest(HTTPRequest.Method.POST, this.details.getTokenUrl())
                    .formParam("refresh_token", refreshToken)
                    .formParam("client_id", this.details.getClientId())
                    .formParam("client_secret", this.details.getClientSecret())
                    .formParam("redirect_uri", this.details.getRedirectUri())
                    .formParam("grant_type", "refresh_token")
                    .getResponse();

            return this.helper.readToken(response.getData());
        } catch (IOException e) {
            throw new OAuthException("Failed to regenerate access token", e);
        }
    }

    @Override
    public void revokeToken(String refreshToken) throws OAuthException {
        try {
            new HTTPRequest(HTTPRequest.Method.POST, this.details.getRevokeUrl())
                    .formParam("token", refreshToken)
                    .getResponse();
        } catch (IOException e) {
            throw new OAuthException("Failed to revoke refresh token", e);
        }
    }

    @Override
    public OauthUser getUser(String accessToken) throws OAuthException {
        try {
            HTTPResponse response = new HTTPRequest(HTTPRequest.Method.GET, this.details.getUserUrl())
                    .header("Authorization", "Bearer " + accessToken)
                    .getResponse();

            return this.helper.readUser(response.getData());
        } catch (IOException e) {
            throw new OAuthException("Failed to get user data", e);
        }
    }
}
