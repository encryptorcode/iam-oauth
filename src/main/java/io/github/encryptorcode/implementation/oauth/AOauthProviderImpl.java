package io.github.encryptorcode.implementation.oauth;

import io.github.encryptorcode.httpclient.HTTPRequest;
import io.github.encryptorcode.httpclient.HTTPResponse;
import io.github.encryptorcode.service.OauthProvider;
import io.github.encryptorcode.entity.OauthToken;
import io.github.encryptorcode.entity.OauthUser;

import java.io.IOException;

/**
 * This is a common implementation that works for multiple providers like Google, Facebook...
 * All you have to do is extend this call the constructor with the required details.
 * Since it's only for authentication please make sure you add the scope required to get user email.
 * We have implementation based on the assumption that an email belongs to a single user.
 * In other words we allow a user to sign in using multiple providers and use a single email.
 */
public abstract class AOauthProviderImpl implements OauthProvider {

    private final String id;
    private final String loginUrl;
    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;
    private final String tokenUrl;
    private final String revokeUrl;
    private final String userUrl;
    private final String scope;

    public AOauthProviderImpl(
            String id,
            String loginUrl,
            String clientId,
            String clientSecret,
            String redirectUri,
            String tokenUrl,
            String revokeUrl,
            String userUrl,
            String scope
    ) {
        this.id = id;
        this.loginUrl = loginUrl;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.tokenUrl = tokenUrl;
        this.revokeUrl = revokeUrl;
        this.userUrl = userUrl;
        this.scope = scope;
    }

    /**
     * This method will be invoked with the response of token API.
     * You need to handle both responses
     * <ol>
     *     <li>When token is generated using grand code, or</li>
     *     <li>Regenerated using refresh token</li>
     * </ol>
     *
     * @param data response as a string
     * @return token object
     */
    public abstract OauthToken readToken(String data);

    /**
     * This method will be invoked with the response of user.
     * You need to make sure that email is set.
     *
     * @param data response as a string
     * @return user object
     */
    public abstract OauthUser readUser(String data);

    /**
     * A unique id for this provider. Once this id is set, you cannot update as this id is used in 2 places
     * <ol>
     *     <li>used in login url</li>
     *     <li>stored for reference for authentication and session</li>
     * </ol>
     * @return permanent unique id
     */
    @Override
    public String id() {
        return id;
    }

    /**
     * Generates a url that takes the user to oauth consent page
     * @param state a string to be set to state param
     * @param showConsent if true, we need to force the user to accept consent to refresh token is (re)generated
     * @return authentication url
     */
    @Override
    public String getAuthenticationUrl(String state, boolean showConsent) {
        String url = this.loginUrl +
                "?client_id=" + this.clientId +
                "&redirect_uri=" + this.redirectUri +
                "&scope=" + this.scope +
                "&access_type=offline" +
                "&response_type=code" +
                "&state=" + state;
        if (showConsent) {
            url += "&prompt=consent";
        }
        return url;
    }

    /**
     * Generates the token using the grant code
     * @param grantCode grant code from generated from redirect url
     * @return token object
     */
    @Override
    public OauthToken generateToken(String grantCode) {
        if (grantCode.equals("invalid_code")) {
            return OauthToken.error("INVALID_CODE");
        }
        try {
            HTTPResponse response = new HTTPRequest(HTTPRequest.Method.POST, this.tokenUrl)
                    .formParam("code", grantCode)
                    .formParam("client_id", this.clientId)
                    .formParam("client_secret", this.clientSecret)
                    .formParam("redirect_uri", this.redirectUri)
                    .formParam("grant_type", "authorization_code")
                    .getResponse();

            return readToken(response.getData());
        } catch (IOException e) {
            return OauthToken.error(e);
        }
    }

    /**
     * Generated a new access token using existing refresh token
     * @param refreshToken existing refresh token
     * @return token object
     */
    @Override
    public OauthToken regenerateToken(String refreshToken) {
        try {
            HTTPResponse response = new HTTPRequest(HTTPRequest.Method.POST, this.tokenUrl)
                    .formParam("refresh_token", refreshToken)
                    .formParam("client_id", this.clientId)
                    .formParam("client_secret", this.clientSecret)
                    .formParam("redirect_uri", this.redirectUri)
                    .formParam("grant_type", "refresh_token")
                    .getResponse();

            return readToken(response.getData());
        } catch (IOException e) {
            return OauthToken.error(e);
        }
    }

    /**
     * Used to revoke an existing token
     * Mostly used with a user is deleted
     * @param refreshToken refresh token of the user
     */
    @Override
    public void revokeToken(String refreshToken) {
        try {
            new HTTPRequest(HTTPRequest.Method.POST, this.revokeUrl)
                    .formParam("token", refreshToken)
                    .getResponse();
        } catch (IOException ignored) {
        }
    }

    /**
     * Helps fetching the user details from the provider
     * @param accessToken access token
     * @return user object
     */
    @Override
    public OauthUser getUser(String accessToken) {
        try {
            HTTPResponse response = new HTTPRequest(HTTPRequest.Method.GET, this.userUrl)
                    .header("Authorization", "Bearer " + accessToken)
                    .getResponse();

            return readUser(response.getData());
        } catch (IOException e) {
            return null;
        }
    }
}
