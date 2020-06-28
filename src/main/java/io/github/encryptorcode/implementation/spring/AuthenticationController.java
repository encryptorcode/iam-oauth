package io.github.encryptorcode.implementation.spring;

import io.github.encryptorcode.exceptions.UserNotAllowedException;
import io.github.encryptorcode.service.AuthenticationConfiguration;
import io.github.encryptorcode.service.AuthenticationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A simple controller implementation using spring annotations for handling all the authentication urls
 * You still have to handle showing the login page to the user and redirect the user to /login/{provider}
 * In case you plan on using a single provider, You can set the default login url to /login/{provider} in your {@link AuthenticationConfiguration}
 */
@Controller
@SuppressWarnings("rawtypes")
public class AuthenticationController {

    private static final AuthenticationService AUTHENTICATION_SERVICE = AuthenticationService.getInstance();
    private static final AuthenticationConfiguration AUTHENTICATION_CONFIGURATION = AuthenticationConfiguration.getConfiguration();

    /**
     * Login page, where user is redirected to provider's consent page
     *
     * @param request    request object
     * @param providerId provider id from url
     * @param redirect   redirect url if any
     * @return redirects to authentication page
     */
    @RequestMapping(value = "/login", params = "provider")
    @ResponseBody
    public ModelAndView login(
            HttpServletRequest request,
            @RequestParam(value = "provider") String providerId,
            @RequestParam(value = "redirect", required = false) String redirect
    ) {
        return new ModelAndView("redirect:" + AUTHENTICATION_SERVICE.getLoginRedirectPath(request, providerId, redirect));
    }

    /**
     * Logout handling where user is logged out, so clearing cookies and sessions is handled
     *
     * @param request  request object
     * @param response response object
     * @param redirect redirect url if any
     * @return redirection after logout
     */
    @RequestMapping("/logout")
    @ResponseBody
    public ModelAndView logout(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(value = "redirect", required = false) String redirect
    ) {
        return new ModelAndView("redirect:" + AUTHENTICATION_SERVICE.logout(request, response, redirect));
    }

    /**
     * Oauth callback url, You need to set this url as redirect_url in your oauth client
     * Or if you plan on writing your own controller implementation, you need to make sure same url is provided to
     * oauth provider configuration
     *
     * @param request  request object
     * @param response response object
     * @param code     grant code
     * @param state    state token
     * @return redirection to app home or redirection path set when logging in
     */
    @RequestMapping("/auth/callback")
    @ResponseBody
    public ModelAndView callback(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam("code") String code,
            @RequestParam("state") String state
    ) {
        try {
            return new ModelAndView("redirect:" + AUTHENTICATION_SERVICE.login(request, response, state, code));
        } catch (UserNotAllowedException e) {
            return new ModelAndView("redirect: " + AUTHENTICATION_CONFIGURATION.homePath);
        }
    }
}