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
 * <code><pre>
 *     &lt;servlet&gt;
 *         &lt;servlet-name&gt;LoginServlet&lt;/servlet-name&gt;
 *         &lt;servlet-class&gt;io.github.encryptorcode.implementation.tomcat.LoginServlet&lt;/servlet-class&gt;
 *     &lt;/servlet&gt;
 *     &lt;servlet-mapping&gt;
 *         &lt;servlet-name&gt;LoginServlet&lt;/servlet-name&gt;
 *         &lt;servlet-mapping&gt;/login&lt;/servlet-mapping&gt;
 *     &lt;/servlet-mapping&gt;
 * </pre></code>
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
