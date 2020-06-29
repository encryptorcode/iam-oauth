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
 * <pre>
 * {@code
 *     <servlet>
 *         <servlet-name>CallbackServlet</servlet-name>
 *         <servlet-class>io.github.encryptorcode.implementation.tomcat.CallbackServlet</servlet-class>
 *     </servlet>
 *     <servlet-mapping>
 *         <servlet-name>CallbackServlet</servlet-name>
 *         <servlet-mapping>/auth/callback</servlet-mapping>
 *     </servlet-mapping>
 * }
 * </pre>
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
