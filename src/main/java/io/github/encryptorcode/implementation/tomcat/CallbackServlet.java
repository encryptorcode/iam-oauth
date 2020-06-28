package io.github.encryptorcode.implementation.tomcat;

import io.github.encryptorcode.exceptions.UserNotAllowedException;
import io.github.encryptorcode.service.AuthenticationConfiguration;
import io.github.encryptorcode.service.AuthenticationService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Oauth callback url, You need to set this url as redirect_url in your oauth client
 * Or if you plan on writing your own servlet implementation, you need to make sure same url is provided to
 * oauth provider configuration
 *
 * Configure this servlet in web.xml like below
 * <code><pre>
 *     &lt;servlet&gt;
 *         &lt;servlet-name&gt;CallbackServlet&lt;/servlet-name&gt;
 *         &lt;servlet-class&gt;io.github.encryptorcode.implementation.tomcat.CallbackServlet&lt;/servlet-class&gt;
 *     &lt;/servlet&gt;
 *     &lt;servlet-mapping&gt;
 *         &lt;servlet-name&gt;CallbackServlet&lt;/servlet-name&gt;
 *         &lt;servlet-mapping&gt;/auth/callback&lt;/servlet-mapping&gt;
 *     &lt;/servlet-mapping&gt;
 * </pre></code>
 */
public class CallbackServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String code = req.getParameter("code");
        String state = req.getParameter("state");
        try {
            String redirectLocation = AuthenticationService.getInstance().login(req, resp, code, state);
            resp.sendRedirect(redirectLocation);
        } catch (UserNotAllowedException e) {
            resp.sendRedirect(AuthenticationConfiguration.getConfiguration().homePath);
        }
    }
}
