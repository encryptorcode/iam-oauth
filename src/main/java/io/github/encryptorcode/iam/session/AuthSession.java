package io.github.encryptorcode.iam.session;

import io.github.encryptorcode.iam.oauth.OauthToken;
import io.github.encryptorcode.iam.user.AuthUser;

public interface AuthSession<U extends AuthUser> {
    String getIdentifier();
    String getOauthStrategy();
    U getUser();
    OauthToken getToken();
}
