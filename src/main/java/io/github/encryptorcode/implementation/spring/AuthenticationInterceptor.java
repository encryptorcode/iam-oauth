package io.github.encryptorcode.implementation.spring;

import io.github.encryptorcode.service.AuthenticationService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A simple Interceptor for all requests to validate and set user details in the current thread local <br>
 * Configure an instance of this in your implementation of {@link WebMvcConfigurer} <br><br>
 * We need to make sure {@link AuthenticationService#postProcessRequest(HttpServletRequest, HttpServletResponse)} is called without fail, to clear data stored in current thread before it's re-used for another request.
 */
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        AuthenticationService.getInstance().preProcessRequest(request, response);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        AuthenticationService.getInstance().postProcessRequest(request, response);
    }
}
