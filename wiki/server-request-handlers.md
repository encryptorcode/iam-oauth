# Server request  handlers
Now we have configured all the handlers, We only have to register it to our server and AuthenticationService to be able to use them.
We have the below things to configure. 
0. [Initializer](#initializer)
0. [Interceptor/Filter](#interceptorfilter)
0. [Controllers/Servlets](#controllersservlets)

## Initializer
Initializer helps you initialize the all your settings and helps this library function properly. 
Also, note that you need to call this when the server starts. 
An example with all the configurations possible.
```java
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ServerStartupListener implements ServerContextListener{
    @Override
    public void contextInitialized(ServletContextEvent event) {
        AuthenticationInitializer.newInstance(Session::new, User::new)
                .addOAuthProvider(new GoogleAuthenticationProvider())
                .setAuthenticationHandler(new AuthenticationHandler())
                .setSessionHandler(new SessionHandler())
                .setUserHandler(new UserHandler())
                .setSessionHandler(new SecurityHandler())
                .setHomePath("/")
                .setLoginPath("/login")
                .setAuthenticationCookieName("auth-cookie")
                .initialize();
    }
}
```
Please refer [AuthenticationInitializer](../src/main/java/io/github/encryptorcode/service/AuthenticationInitializer.java) for defaults and mandatory fields

Since this had to be triggered before the server starts, In case of tomcat we can configure a server context listener in web.xml
```xml
<listener>
    <listener-class>ServerStartupListener</listener-class>
</listener>
```

## Interceptor/Filter
Now next we need to configure calls to preProcess and postProcess of [AuthenticationService](../src/main/java/io/github/encryptorcode/service/AuthenticationService.java) to be invoked for every request.

### For Spring Users
We have an in-built interceptor that you can configure in spring, or you can even write your own implementation of interceptor and invoke preProcess and postProcess methods
To use the in-built interceptor on spring you simply have to pass it to WebMvcConfigurer of your spring implementation. 
```java
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getAuthenticationInterceptor());
    }

    @Bean
    public HandlerInterceptor getAuthenticationInterceptor() {
        return new AuthenticationInterceptor();
    }
```

If you have configured spring using xml you need to add the following
```xml
<beans:beans>
    <mvc:interceptors>
        <bean class="io.github.encryptorcode.implementation.spring.AuthenticationInterceptor"/>
    </mvc:interceptors>
</beans:beans>
```

### For Tomcat Users
We have an in-built filter for setting up on the tomcat server, Or you can even write your own implementation that calls the preProcess and postProcess methods.
To use the in-built filter you need to add the following in your web.xml file.
```xml
<filter>
    <filter-name>AuthenticationInterceptor</filter-name>
    <filter-class>io.github.encryptorcode.implementation.tomcat.AuthenticationFilter</filter-class>
</filter>
```

## Controllers/Servlets
Now we need to configure urls for the login, logout and receiving callback from oauth provider.
A few things to note.
- The in-built implementations will only set up /login, /logout and /auth/callback urls
- You will have to set up the home page and login page on your own.
- Your custom login page should contain links to redirect to /login?provider=`provider_id`
- Make sure you configure your provider to redirect to /auth/callback if you are using in-built implementations.
 

### For Spring Users
We have an in-built controller for setting up, Or as usual you can write one on your own and call the relevant methods.
To use the in-built controller you can add the package name to your component scan annotation over WebMvcConfigurer implementation.
```java
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@ComponentScan(basePackages = {
        "your.own.controllers.package", 
        "io.github.encryptorcode.implementation.spring"
})
class WebMvcConfig extends WebMvcConfigurer{

}
```

Or if you are using spring xml configuration you can set it up like below.
```xml
<beans:beans>
    <context:component-scan base-package="your.own.controllers.package, io.github.encryptorcode.implementation.spring" />
</beans:beans>
```

### For Tomcat Users
We have in-built servlets to invoke the relevant methods, Or as usual you can write the servlets on your own and invoke the methods. 
To use the in-built servlets you can configure them in web.xml as given below.
```xml
<servlet>
    <servlet-name>LoginServlet</servlet-name>
    <servlet-class>io.github.encryptorcode.implementation.tomcat.LoginServlet</servlet-class>
</servlet>
<servlet-mapping>
    <servlet-name>LoginServlet</servlet-name>
    <url-pattern>/login</url-pattern>
</servlet-mapping>

<servlet>
    <servlet-name>LogoutServlet</servlet-name>
    <servlet-class>io.github.encryptorcode.implementation.tomcat.LogoutServlet</servlet-class>
</servlet>
<servlet-mapping>
    <servlet-name>LogoutServlet</servlet-name>
    <url-pattern>/logout</url-pattern>
</servlet-mapping>

<servlet>
    <servlet-name>CallbackServlet</servlet-name>
    <servlet-class>io.github.encryptorcode.implementation.tomcat.CallbackServlet</servlet-class>
</servlet>
<servlet-mapping>
    <servlet-name>CallbackServlet</servlet-name>
    <url-pattern>/auth/callback</url-pattern>
</servlet-mapping>
```

---

Prev: [Setup your security for your users](security-handler.md)