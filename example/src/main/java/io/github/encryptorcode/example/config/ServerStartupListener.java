package io.github.encryptorcode.example.config;

import io.github.encryptorcode.example.entities.Session;
import io.github.encryptorcode.example.entities.User;
import io.github.encryptorcode.handlers.AAuthenticationHandler;
import io.github.encryptorcode.handlers.ASessionHandler;
import io.github.encryptorcode.handlers.AUserHandler;
import io.github.encryptorcode.implementation.security.BaseSecurityHandler;
import io.github.encryptorcode.implementation.storage.file.FileAuthenticationHandler;
import io.github.encryptorcode.implementation.storage.file.FileSessionHandler;
import io.github.encryptorcode.implementation.storage.file.FileUserHandler;
import io.github.encryptorcode.service.AuthenticationInitializer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ServerStartupListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        AUserHandler<User> userHandler = new FileUserHandler<User>("users.bin") {
            @Override
            public User constructUser() {
                return new User();
            }
        };
        ASessionHandler<Session, User> sessionHandler = new FileSessionHandler<Session, User>("sessions.bin") {
            @Override
            public Session constructSession() {
                return new Session();
            }
        };
        AAuthenticationHandler authenticationHandler = new FileAuthenticationHandler("authentication.bin");

        AuthenticationInitializer.newInstance(Session.class, User.class)
                .addOAuthProvider(new GoogleAuthenticationProvider())
                .setAuthenticationHandler(authenticationHandler)
                .setSessionHandler(sessionHandler)
                .setUserHandler(userHandler)
                .initialize();
    }
}
