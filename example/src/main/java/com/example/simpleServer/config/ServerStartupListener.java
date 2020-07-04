package com.example.simpleServer.config;

import com.example.simpleServer.entities.Session;
import com.example.simpleServer.entities.User;
import io.github.encryptorcode.service.AuthenticationInitializer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ServerStartupListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
        AuthenticationInitializer.newInstance(Session::new, User::new)
                .addOAuthProvider(new GoogleAuthenticationProvider())
                .initialize();
    }
}
