package io.github.encryptorcode.implementation.tomcat;

import io.github.encryptorcode.service.AuthenticationService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Login page, where user is redirected to provider's consent page
 *
 * Configure this servlet in web.xml like below
 * <pre>
 * {@code
 *     <servlet>
 *         <servlet-name>LoginServlet</servlet-name>
 *         <servlet-class>io.github.encryptorcode.implementation.tomcat.LoginServlet</servlet-class>
 *     </servlet>
 *     <servlet-mapping>
 *         <servlet-name>LoginServlet</servlet-name>
 *         <servlet-mapping>/login</servlet-mapping>
 *     </servlet-mapping>
 * }
 * </pre>
 */
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String provider = req.getParameter("provider");
        String redirect = req.getParameter("redirect");
        String redirectLocation = AuthenticationService.getInstance().getLoginRedirectPath(req, provider, redirect);
        resp.sendRedirect(redirectLocation);
    }
}
