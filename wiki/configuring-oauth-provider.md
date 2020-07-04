# Configuring oauth provider(s)
There are multiple products that provide oauth. For example: [Google](https://developers.google.com/identity/sign-in/web/sign-in), [Facebook](https://developers.facebook.com/docs/facebook-login/manually-build-a-login-flow/), [GitHub](https://docs.github.com/en/rest/guides/basics-of-authentication) and many more...

To configure logging in your users using oauth you can either manually implement [OauthProvider](../src/main/java/io/github/encryptorcode/service/OauthProvider.java) or extend a ready-made [AOuthProviderImpl](../src/main/java/io/github/encryptorcode/implementation/oauth/AOauthProviderImpl.java).
Below given is an example for implementation for GoogleAuthenticationProvider.
```java
package com.example.simpleServer.config;

import io.github.encryptorcode.entity.OauthToken;
import io.github.encryptorcode.entity.OauthUser;
import io.github.encryptorcode.implementation.oauth.AOauthProviderImpl;
import org.json.JSONObject;

import java.time.ZonedDateTime;

public class GoogleAuthenticationProvider extends AOauthProviderImpl {

    private static final String GOOGLE_CLIENT_ID = System.getProperty("google.client.id");
    private static final String GOOGLE_CLIENT_SECRET = System.getProperty("google.client.secret");
    private static final String SERVER_DOMAIN = "http://localhost:8888";

    public GoogleAuthenticationProvider() {
        super(
                "google",
                "https://accounts.google.com/o/oauth2/v2/auth",
                GOOGLE_CLIENT_ID,
                GOOGLE_CLIENT_SECRET,
                SERVER_DOMAIN + "/auth/callback",
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
        if (userJson.has("id")) {
            oauthId = userJson.getString("id");
        }
        if (userJson.has("email")) {
            email = userJson.getString("email");
        }
        if (userJson.has("name")) {
            fullName = userJson.getString("name");
        }
        if (userJson.has("given_name")) {
            name = userJson.getString("given_name");
        }
        if (userJson.has("picture")) {
            profileImage = userJson.getString("picture");
        }

        return new OauthUser(oauthId, email, fullName, name, profileImage);
    }

    @Override
    public OauthToken readToken(String token) {
        JSONObject object = new JSONObject(token);
        if (object.has("access_token") && object.has("expires_in")) {
            if (object.has("refresh_token")) {
                return OauthToken.create(
                        object.getString("access_token"),
                        object.getString("refresh_token"),
                        ZonedDateTime.now().plusSeconds(object.getLong("expires_in"))
                );
            } else {
                return OauthToken.create(
                        object.getString("access_token"),
                        ZonedDateTime.now().plusSeconds(object.getLong("expires_in"))
                );
            }
        }
        return OauthToken.error("MISSING_KEYS");
    }
}
```

---

Next: [Create your User and Session implementations](user-and-session-implementation.md)