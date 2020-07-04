package io.github.encryptorcode.service;

import io.github.encryptorcode.entity.*;
import io.github.encryptorcode.exceptions.UserNotAllowedException;
import io.github.encryptorcode.handlers.AAuthenticationHandler;
import io.github.encryptorcode.handlers.ASecurityHandler;
import io.github.encryptorcode.handlers.ASessionHandler;
import io.github.encryptorcode.handlers.AUserHandler;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the core logic used for handling authentication of a user
 *
 * @param <Session> Session template
 * @param <User> User template
 */
public class AuthenticationService<Session extends ASession, User extends AUser> {

    private static final String PACKAGE_NAME = AuthenticationService.class.getPackage().getName();
    private static final String SESSION_REDIRECT_KEY = PACKAGE_NAME + ".redirect";
    private static final Logger LOGGER = Logger.getLogger(AuthenticationService.class.getName());

    private AuthenticationService() {
    }

    @SuppressWarnings("rawtypes")
    private static AuthenticationService authenticationService;

    public static <Session extends ASession, User extends AUser> AuthenticationService<Session, User> getInstance() {
        if (authenticationService == null) {
            authenticationService = new AuthenticationService<>();
        }
        //noinspection unchecked
        return authenticationService;
    }

    @SuppressWarnings("unchecked")
    private final AuthenticationConfiguration<Session, User> configuration = (AuthenticationConfiguration<Session, User>) AuthenticationConfiguration.configuration;

    /**
     * A method to get the current user at any given http request context
     *
     * @return User object
     */
    public User getCurrentUser() {
        return AuthenticationThreadLocal.getCurrentUser();
    }

    /**
     * A method to get the current session at any given http request context
     *
     * @return Session object
     */
    public Session getCurrentSession() {
        return AuthenticationThreadLocal.getCurrentSession();
    }

    /**
     * Use this method to get a generated login path for the given provider
     *
     * @param request      request object
     * @param providerId   provider id
     * @param redirectPath redirection path to redirect the user to after login. If null is set app home will be used
     * @return url to redirect the user to for authentication
     */
    public String getLoginRedirectPath(HttpServletRequest request, String providerId, String redirectPath) {

        // user is already logged in
        User user = getCurrentUser();
        if (user != null) {
            LOGGER.log(Level.FINE, "Login page requested when the user is already logged in: {0}", new String[]{user.getUserId()});
            if (redirectPath != null) {
                return redirectPath;
            } else {
                return configuration.homePath;
            }
        }

        // user trying to login with a provider
        if (providerId == null) {
            LOGGER.log(Level.SEVERE, "PROVIDER_MISSING :: Redirecting the user to login page as provider is missing");
            return configuration.loginPath;
        }

        Map<String, OauthProvider> providers = configuration.oauthProviders;
        if (!providers.containsKey(providerId)) {
            LOGGER.log(Level.SEVERE, "INVALID_PROVIDER :: Redirecting the user to home page as given provider id is invalid: {0}", new String[]{providerId});
            return configuration.homePath;
        }

        HttpSession session = request.getSession();
        if (redirectPath != null) {
            session.setAttribute(SESSION_REDIRECT_KEY, redirectPath);
        }
        OauthProvider provider = providers.get(providerId);
        return provider.getAuthenticationUrl(providerId, false);

    }

    /**
     * Use this method to log the user in when the grant code is generated
     *
     * @param request   request object
     * @param response  response object to update cookies
     * @param state     state param
     * @param grantCode grant code
     * @return url to redirect the user to after login is successful
     * @throws UserNotAllowedException thrown when the user is not allowed to signup
     */
    public String login(HttpServletRequest request, HttpServletResponse response, String state, String grantCode) throws UserNotAllowedException {
        Map<String, OauthProvider> providers = configuration.oauthProviders;
        if (!providers.containsKey(state)) {
            LOGGER.log(Level.SEVERE, "INVALID_PROVIDER :: Redirecting the user to login page as given provider id is invalid: {0}", new String[]{state});
            return configuration.loginPath;
        }

        OauthProvider provider = providers.get(state);
        OauthToken token = provider.generateToken(grantCode);
        if (token.getStatus() == OauthToken.Status.INVALID_CODE) {
            LOGGER.log(Level.WARNING, "ACCESS_GENERATION_FAILED :: Access token was not generated failed with invalid code", token.getThrowable());
            return provider.getAuthenticationUrl(state, false);
        }

        AUserHandler<User> userHandler = configuration.userHandler;
        AAuthenticationHandler authenticationHandler = configuration.authenticationHandler;
        OauthUser oauthUser = provider.getUser(token.getAccessToken());
        User user = userHandler.getUserByEmail(oauthUser.getEmail());

        if (user == null) {
            if (token.getStatus() != OauthToken.Status.ACCESS_AND_REFRESH) {
                return provider.getAuthenticationUrl(state, true);
            }

            user = createUser(oauthUser);
            createAuthenticationDetail(provider.id(), user, oauthUser, token);
        } else {
            AuthenticationDetail authenticationDetail = authenticationHandler.getAuthenticationDetail(user.getUserId(), provider.id());
            if (authenticationDetail == null) {
                if (token.getStatus() != OauthToken.Status.ACCESS_AND_REFRESH) {
                    return provider.getAuthenticationUrl(state, true);
                }

                createAuthenticationDetail(provider.id(), user, oauthUser, token);
            }
        }

        ASecurityHandler<User> securityHandler = configuration.securityHandler;
        ASessionHandler<Session, User> sessionHandler = configuration.sessionHandler;
        String sessionIdentifier = securityHandler.generateIdentifier(user);
        String encryptedCookieValue = securityHandler.encryptCookieValue(sessionIdentifier);
        int expiryInSeconds = securityHandler.getSessionExpiration(user);

        ZonedDateTime now = ZonedDateTime.now();
        Session session = configuration.sessionConstructor.construct();
        session.setIdentifier(sessionIdentifier);
        session.setUserId(user.getUserId());
        session.setProviderId(provider.id());
        session.setCreationTime(now);
        session.setExpiryTime(now.plusSeconds(expiryInSeconds));
        sessionHandler.createSession(session);

        Cookie cookie = new Cookie(configuration.authenticationCookieName, encryptedCookieValue);
        cookie.setPath("/");
        cookie.setMaxAge(expiryInSeconds);
        response.addCookie(cookie);

        HttpSession httpSession = request.getSession();
        if (httpSession.getAttribute(SESSION_REDIRECT_KEY) != null) {
            return (String) httpSession.getAttribute(SESSION_REDIRECT_KEY);
        }
        return configuration.homePath;
    }

    /**
     * Use this method to log out a user. This method will help clean up context, like session and cookies
     *
     * @param request      request object
     * @param response     response object
     * @param redirectPath redirect path
     * @return url to redirect the user to after successful log out
     */
    public String logout(HttpServletRequest request, HttpServletResponse response, String redirectPath) {
        clearSession(request, response);
        if (redirectPath != null) {
            return redirectPath;
        }
        return configuration.homePath;
    }

    /**
     * Call this method for every request. This method validates and sets details like current user and session in thread local
     *
     * @param request  request object
     * @param response response object
     */
    public void preProcessRequest(HttpServletRequest request, HttpServletResponse response) {
        AuthenticationThreadLocal.clear();
        AuthenticationThreadLocal.setCurrentRequest(request);

        String sessionIdentifier = getAuthCookieValue(request);
        if (sessionIdentifier == null) {
            clearSession(request, response);
            return;
        }

        ASessionHandler<Session, User> sessionHandler = configuration.sessionHandler;
        AUserHandler<User> userHandler = configuration.userHandler;
        AAuthenticationHandler authenticationStorage = configuration.authenticationHandler;

        Session session = sessionHandler.getSession(sessionIdentifier);
        if (session == null) {
            clearSession(request, response);
            return;
        }

        if (isTimePassed(session.getExpiryTime())) {
            clearSession(request, response);
            return;
        }

        User user = userHandler.getUser(session.getUserId());
        AuthenticationDetail detail = authenticationStorage.getAuthenticationDetail(user.getUserId(), session.getProviderId());
        if (isTimePassed(detail.getExpiryTime())) {
            OauthProvider provider = configuration.oauthProviders.get(session.getProviderId());
            OauthToken token = provider.regenerateToken(detail.getRefreshToken());
            if (token.getStatus() == OauthToken.Status.INVALID_CODE) {
                clearSession(request, response);
                return;
            }

            detail.setAccessToken(token.getAccessToken());
            detail.setExpiryTime(token.getExpiryTime());
            authenticationStorage.update(detail);
        }

        AuthenticationThreadLocal.setCurrentSession(session);
        AuthenticationThreadLocal.setCurrentUser(user);
        LOGGER.log(Level.FINE, "Current user is set to: {0}", new String[]{user.getUserId()});
        LOGGER.log(Level.FINE, "Current session id is: {0}", new String[]{sessionIdentifier});
    }

    /**
     * call this method after completion of each request
     *
     * @param request  request object
     * @param response response object
     */
    public void postProcessRequest(HttpServletRequest request, HttpServletResponse response) {
        AuthenticationThreadLocal.clear();
    }

    private User createUser(OauthUser oauthUser) throws UserNotAllowedException {
        AUserHandler<User> userHandler = configuration.userHandler;
        User user = configuration.userConstructor.construct();
        user.setEmail(oauthUser.getEmail());
        user.setName(oauthUser.getName());
        user.setFullName(oauthUser.getFullName());
        user.setProfileImage(oauthUser.getProfileImage());

        String userId = userHandler.generateUserId(user);
        user.setUserId(userId);
        return userHandler.createUser(user);
    }

    private void createAuthenticationDetail(String providerId, User user, OauthUser oauthUser, OauthToken token) {
        AuthenticationDetail authenticationDetail = new AuthenticationDetail();
        authenticationDetail.setUserId(user.getUserId());
        authenticationDetail.setProvider(providerId);
        authenticationDetail.setProvidedUserId(oauthUser.getId());
        authenticationDetail.setAccessToken(token.getAccessToken());
        authenticationDetail.setExpiryTime(token.getExpiryTime());
        authenticationDetail.setRefreshToken(token.getRefreshToken());
        configuration.authenticationHandler.create(authenticationDetail);
    }

    private void clearSession(HttpServletRequest request, HttpServletResponse response) {
        Cookie authCookie = getAuthCookie(request);

        if (authCookie == null) {
            return;
        }

        authCookie.setMaxAge(0);
        response.addCookie(authCookie);

        String sessionIdentifier = authCookie.getValue();
        configuration.sessionHandler.deleteSession(sessionIdentifier);
    }

    private String getAuthCookieValue(HttpServletRequest request) {
        Cookie authCookie = getAuthCookie(request);
        if (authCookie == null) {
            return null;
        }

        ASecurityHandler<User> securityHandler = configuration.securityHandler;
        return securityHandler.decryptCookieValue(authCookie.getValue());
    }

    private Cookie getAuthCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Cookie authCookie = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(configuration.authenticationCookieName)) {
                    authCookie = cookie;
                }
            }
        }
        return authCookie;
    }

    private boolean isTimePassed(ZonedDateTime time) {
        return time.compareTo(ZonedDateTime.now()) <= 0;
    }

}
