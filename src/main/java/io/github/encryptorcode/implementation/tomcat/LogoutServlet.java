package io.github.encryptorcode.implementation.tomcat;

import io.github.encryptorcode.service.AuthenticationService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Logout handling where user is logged out, so clearing cookies and sessions is handled
 *
 * Configure this servlet in web.xml like below
 * <pre>
 * {@code
 *     <servlet>
 *         <servlet-name>LogoutServlet</servlet-name>
 *         <servlet-class>io.github.encryptorcode.implementation.tomcat.LogoutServlet</servlet-class>
 *     </servlet>
 *     <servlet-mapping>
 *         <servlet-name>CallbackServlet</servlet-name>
 *         <servlet-mapping>/logout</servlet-mapping>
 *     </servlet-mapping>
 * }
 * </pre>
 */
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String redirect = req.getParameter("redirect");
        String redirectLocation = AuthenticationService.getInstance().logout(req, resp, redirect);
        resp.sendRedirect(redirectLocation);
    }
}
