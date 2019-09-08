package io.github.encryptorcode.iam.service;

import io.github.encryptorcode.iam.oauth.OAuthException;
import io.github.encryptorcode.iam.oauth.OauthStrategy;
import io.github.encryptorcode.iam.oauth.OauthToken;
import io.github.encryptorcode.iam.oauth.OauthUser;
import io.github.encryptorcode.iam.session.AuthSession;
import io.github.encryptorcode.iam.session.AuthSessionStorage;
import io.github.encryptorcode.iam.user.AuthUser;
import io.github.encryptorcode.iam.user.AuthUserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class AuthenticationService<U extends AuthUser, S extends AuthSession<U>> {

    private static final String PACKAGE_NAME = AuthenticationService.class.getPackage().getName();
    private static final String REQUEST_USER_KEY = PACKAGE_NAME+".user";
    private static final String REQUEST_SESSION_KEY = PACKAGE_NAME+".session";
    private static final String SESSION_REDIRECT_KEY = PACKAGE_NAME+".redirect";
    private static final String AUTH_COOKIE_NAME = "framework-auth";
    private static final String INDEX_PATH = "/";

    private AuthenticationHelper<U,S> helper;
    private AuthUserService<U> authUserService;
    private AuthSessionStorage<U,S> authSessionStorage;
    private Map<String, OauthStrategy> oauthStrategies;

    public AuthenticationService(AuthenticationHelper<U,S> helper){
        this.helper = helper;
        this.authUserService = helper.getUserService();
        this.authSessionStorage = helper.getSessionStorage();
        this.oauthStrategies = new HashMap<>();
        for (OauthStrategy oauthStrategy : helper.getOauthStrategies()) {
            this.oauthStrategies.put(oauthStrategy.getStrategyName(),oauthStrategy);
        }
    }

    public U getCurrentUser(HttpServletRequest request){
        if(request == null){
            return null;
        }

        return (U) request.getAttribute(REQUEST_USER_KEY);
    }

    public S getCurrentSession(HttpServletRequest request){
        if(request == null){
            return null;
        }

        return (S) request.getAttribute(REQUEST_SESSION_KEY);
    }

    public String getLoginRedirectPath(HttpServletRequest request, String loginStrategy, String redirectPath) {

        // user is already logged in
        U user = getCurrentUser(request);
        if (user != null) {
            if(redirectPath != null){
                return redirectPath;
            } else {
                return INDEX_PATH;
            }
        }

        // user trying to login using an oauth strategy
        if(loginStrategy != null){
            if(!oauthStrategies.containsKey(loginStrategy)){
                throw new NullPointerException("Invalid login strategy specified "+loginStrategy);
            }
            OauthStrategy strategy = oauthStrategies.get(loginStrategy);
            HttpSession httpSession = request.getSession();
            if(redirectPath != null) {
                httpSession.setAttribute(SESSION_REDIRECT_KEY, redirectPath);
            }
            return strategy.getAuthenticationUrl(strategy.getStrategyName());
        }

        // user trying to access the default login page of the app
        String loginPath = helper.getLoginPagePath();
        if (loginPath == null) {
            throw new NullPointerException("Login view is not provided by helper " + helper.getClass());
        } else {
            if (loginPath.contains("?")) {
                loginPath = loginPath + "&redirect=" + URLEncoder.encode(redirectPath);
            } else {
                loginPath = loginPath + "?redirect=" + URLEncoder.encode(redirectPath);
            }
            return loginPath;
        }
    }

    public String login(HttpServletRequest request, HttpServletResponse response, String strategyName, String grantCode) throws OAuthException {
        if(!oauthStrategies.containsKey(strategyName)){
            throw new NullPointerException("Invalid login strategy name specified "+strategyName);
        }

        OauthStrategy oauthStrategy = oauthStrategies.get(strategyName);
        if(request == null){
            throw new NullPointerException("You call login only when the user sends a request");
        }

        OauthToken token = oauthStrategy.generateToken(grantCode);
        OauthUser oauthUser = oauthStrategy.getUser(token.getAccessToken());
        U user = authUserService.getUserByEmail(oauthUser.getEmail());

        if(user == null){
            if(!helper.isUserAllowedSignUp(oauthUser)){
                throw new OAuthException("User was not allowed to signUp.");
            }

            Map<String,String> strategyVsIdMap = new HashMap<>();
            strategyVsIdMap.put(strategyName,oauthUser.getOauthId());
            user = authUserService.createUser(oauthUser.getEmail(),oauthUser.getName(),oauthUser.getFullName(),strategyVsIdMap,oauthUser.getProfileImage());

        } else {
            if(!helper.isUserAllowedLogin(user)) {
                throw new OAuthException("User was not allowed to login.");
            }

            if(user.getFullName() == null) {
                user.setFullName(oauthUser.getFullName());
            }

            if(user.getName() == null) {
                user.setName(oauthUser.getName());
            }

            if(user.getProfileImage() == null){
                user.setProfileImage(oauthUser.getProfileImage());
            }

            if(user.getStrategyVsIdMap() == null){
                user.setStrategyVsIdMap(new HashMap<>());
            }

            user.getStrategyVsIdMap().put(strategyName,oauthUser.getOauthId());

            authUserService.updateUser(user);

        }

        String sessionIdentifier = generateSessionIdentifier();
        authSessionStorage.createSession(sessionIdentifier,strategyName,token,user);

        Cookie authCookie = new Cookie(AUTH_COOKIE_NAME,sessionIdentifier);
        authCookie.setPath("/");
        authCookie.setMaxAge(60 * 60 * 24 * 30); // FIXME:: setting 1 month span for cookie expiry. until a way to revoke unused tokens is found
        response.addCookie(authCookie);

        HttpSession httpSession = request.getSession();
        if(httpSession.getAttribute(SESSION_REDIRECT_KEY) != null) {
            return (String) httpSession.getAttribute(SESSION_REDIRECT_KEY);
        }
        return INDEX_PATH;
    }

    public String logout(HttpServletRequest request, HttpServletResponse response, String redirectPath) throws OAuthException {
        if(request == null){
            throw new NullPointerException("You can logout only when the user sends a request");
        }

        Cookie authCookie = getAuthCookie(request);
        if(authCookie != null) {

            authCookie.setMaxAge(0);
            response.addCookie(authCookie);

            String sessionIdentifier = authCookie.getValue();
            S currentSession = getCurrentSession(request);

            OauthStrategy strategy = oauthStrategies.get(currentSession.getOauthStrategy());
            if (strategy != null) {
                strategy.revokeToken(currentSession.getToken().getRefreshToken());
            }

            authSessionStorage.deleteSession(sessionIdentifier);
        }

        if(redirectPath != null){
            return redirectPath;
        }
        return INDEX_PATH;

    }

    public void processSession(HttpServletRequest request) throws OAuthException {
        if(request ==  null){
            throw new NullPointerException("You process session only when the user sends a request");
        }

        Cookie authCookie = getAuthCookie(request);
        if(authCookie == null){
            return;
        }

        String sessionIdentifier = authCookie.getValue();
        S session = authSessionStorage.getSession(sessionIdentifier);
        if(session == null){
            return;
        }

        if(!oauthStrategies.containsKey(session.getOauthStrategy())){
            throw new NullPointerException("Missing Oauth Strategy "+session.getOauthStrategy());
        }
        OauthStrategy strategy = oauthStrategies.get(session.getOauthStrategy());

        authSessionStorage.updateSessionAccessed(sessionIdentifier);
        if(isTimePassed(session.getToken().getExpiryTime())){
            OauthToken token = strategy.regenerateToken(session.getToken().getRefreshToken());

            if(token.getRefreshToken() == null || token.getRefreshToken().isEmpty()){
                authSessionStorage.deleteSession(sessionIdentifier);
                return;
            } else {
                token.setRefreshToken(session.getToken().getRefreshToken());
                authSessionStorage.updateSessionToken(sessionIdentifier, token);
            }
        }

        U user = session.getUser();
        request.setAttribute(REQUEST_USER_KEY,user);
        request.setAttribute(REQUEST_SESSION_KEY,session);
    }

    private String generateSessionIdentifier(){
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[16];
        secureRandom.nextBytes(bytes);
        Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
        return encoder.encodeToString(bytes);
    }


    private Cookie getAuthCookie(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        Cookie authCookie = null;
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(AUTH_COOKIE_NAME)) {
                    authCookie = cookie;
                }
            }
        }
        return authCookie;
    }

    private boolean isTimePassed(ZonedDateTime time){
        return time.compareTo(ZonedDateTime.now(ZoneId.of("GMT"))) <= 0;
    }

    public void deleteSession(S session) throws OAuthException {
        if(!oauthStrategies.containsKey(session.getOauthStrategy())){
            throw new NullPointerException("Invalid login strategy name specified "+session.getOauthStrategy());
        }

        OauthStrategy strategy = oauthStrategies.get(session.getOauthStrategy());
        strategy.revokeToken(session.getToken().getRefreshToken());
    }
}
