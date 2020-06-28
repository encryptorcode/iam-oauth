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
 * <code><pre>
 *     &lt;servlet&gt;
 *         &lt;servlet-name&gt;LogoutServlet&lt;/servlet-name&gt;
 *         &lt;servlet-class&gt;io.github.encryptorcode.implementation.tomcat.LogoutServlet&lt;/servlet-class&gt;
 *     &lt;/servlet&gt;
 *     &lt;servlet-mapping&gt;
 *         &lt;servlet-name&gt;CallbackServlet&lt;/servlet-name&gt;
 *         &lt;servlet-mapping&gt;/logout&lt;/servlet-mapping&gt;
 *     &lt;/servlet-mapping&gt;
 * </pre></code>
 */
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String redirect = req.getParameter("redirect");
        String redirectLocation = AuthenticationService.getInstance().logout(req, resp, redirect);
        resp.sendRedirect(redirectLocation);
    }
}
