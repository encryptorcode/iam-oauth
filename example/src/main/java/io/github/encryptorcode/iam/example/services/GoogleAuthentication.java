package io.github.encryptorcode.iam.example.services;

import io.github.encryptorcode.iam.implementation.OauthStrategyDetails;
import io.github.encryptorcode.iam.implementation.OauthStrategyHelper;
import io.github.encryptorcode.iam.oauth.OauthToken;
import io.github.encryptorcode.iam.oauth.OauthUser;
import org.json.JSONObject;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class GoogleAuthentication implements OauthStrategyHelper{

    private static final String GOOGLE_CLIENT_ID = "";
    private static final String GOOGLE_CLIENT_SECRET = "";
    private static final String SERVER_DOMAIN = "";

    @Override
    public OauthStrategyDetails getDetails() {
        return new OauthStrategyDetails(
                "Google",
                "https://accounts.google.com/o/oauth2/v2/auth",
                GOOGLE_CLIENT_ID,
                GOOGLE_CLIENT_SECRET,
                SERVER_DOMAIN+"/auth/callback",
                "https://www.googleapis.com/oauth2/v4/token",
                "https://accounts.google.com/o/oauth2/revoke",
                "https://www.googleapis.com/oauth2/v1/userinfo?alt=json",
                "https://www.googleapis.com/auth/userinfo.email%20https://www.googleapis.com/auth/userinfo.profile"
        );
    }

    @Override
    public OauthUser readUser(String user) {
        String oauthId = null,
                email = null,
                fullName = null,
                name = null,
                profileImage = null;

        JSONObject userJson = new JSONObject(user);
        if(userJson.has("id")){
            oauthId = userJson.getString("id");
        }
        if(userJson.has("email")){
            email = userJson.getString("email");
        }
        if(userJson.has("name")){
            fullName = userJson.getString("name");
        }
        if(userJson.has("given_name")){
            name = userJson.getString("given_name");
        }
        if(userJson.has("picture")){
            profileImage = userJson.getString("picture");
        }

        return new OauthUser(oauthId, email, fullName, name, profileImage);
    }

    @Override
    public OauthToken readToken(String token) {

        String accessToken = null,
                refreshToken = null;
        ZonedDateTime expiryTime = null;

        JSONObject tokenJson = new JSONObject(token);
        if(tokenJson.has("access_token")){
            accessToken = tokenJson.getString("access_token");
        }
        if(tokenJson.has("refresh_token")){
            refreshToken = tokenJson.getString("refresh_token");
        }
        if(tokenJson.has("expires_in")){
            long expiresInSeconds = tokenJson.getLong("expires_in");
            expiryTime = ZonedDateTime.now(ZoneId.of("GMT")).plusSeconds(expiresInSeconds);
        }

        return new OauthToken(accessToken, refreshToken, expiryTime);
    }
}
