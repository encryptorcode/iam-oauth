package io.github.encryptorcode.iam.implementation;

import io.github.encryptorcode.iam.oauth.OauthToken;
import io.github.encryptorcode.iam.oauth.OauthUser;

public interface OauthStrategyHelper {
    OauthStrategyDetails getDetails();
    OauthUser readUser(String user);
    OauthToken readToken(String token);
}
