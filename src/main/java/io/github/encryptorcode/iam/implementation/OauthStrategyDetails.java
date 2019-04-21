package io.github.encryptorcode.iam.implementation;

public class OauthStrategyDetails {
    private String strategyName;
    private String loginUrl;
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String tokenUrl;
    private String revokeUrl;
    private String userUrl;
    private String scope;

    public OauthStrategyDetails(String strategyName,
                                String loginUrl,
                                String clientId,
                                String clientSecret,
                                String redirectUri,
                                String tokenUrl,
                                String revokeUrl,
                                String userUrl,
                                String scope
    ) {
        this.strategyName = strategyName;
        this.loginUrl = loginUrl;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.tokenUrl = tokenUrl;
        this.revokeUrl = revokeUrl;
        this.userUrl = userUrl;
        this.scope = scope;
    }

    String getStrategyName() {
        return strategyName;
    }

    String getLoginUrl() {
        return loginUrl;
    }

    String getClientId() {
        return clientId;
    }

    String getClientSecret() {
        return clientSecret;
    }

    String getRedirectUri() {
        return redirectUri;
    }

    String getTokenUrl() {
        return tokenUrl;
    }

    String getRevokeUrl() {
        return revokeUrl;
    }

    String getUserUrl() {
        return userUrl;
    }

    String getScope() {
        return scope;
    }
}
