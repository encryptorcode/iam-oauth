package io.github.encryptorcode.implementation.tomcat;

import io.github.encryptorcode.service.AuthenticationService;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * A simple filter for all requests to validate and set user details in the current thread local
 * Add the following xml in your web.xml to configure this filter
 * <pre>
 * {@code
 *     <filter>
 *         <filter-name>AuthenticationFilter</filter-name>
 *         <filter-class>io.github.encryptorcode.implementation.tomcat.AuthenticationFilter</filter-class>
 *     </filter>
 * }
 * </pre>
 * We need to make sure {@link AuthenticationService#postProcessRequest(HttpServletRequest, HttpServletResponse)} is called without fail, to clear data stored in current thread before it's re-used for another request.
 */
public class AuthenticationFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        AuthenticationService.getInstance().preProcessRequest(req, res);
        chain.doFilter(req, res);
        AuthenticationService.getInstance().postProcessRequest(req, res);
    }
}
