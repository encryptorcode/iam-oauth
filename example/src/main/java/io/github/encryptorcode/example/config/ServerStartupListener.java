package io.github.encryptorcode.example.config;

import io.github.encryptorcode.example.entities.Session;
import io.github.encryptorcode.example.entities.User;
import io.github.encryptorcode.example.services.GoogleAuthenticationProvider;
import io.github.encryptorcode.implementation.storage.jdbc.JdbcAuthenticationHandler;
import io.github.encryptorcode.implementation.storage.jdbc.JdbcConfiguration;
import io.github.encryptorcode.implementation.storage.jdbc.JdbcSessionHandler;
import io.github.encryptorcode.implementation.storage.jdbc.JdbcUserHandler;
import io.github.encryptorcode.implementation.storage.redis.RedisAuthenticationHandler;
import io.github.encryptorcode.implementation.storage.redis.RedisConfiguration;
import io.github.encryptorcode.implementation.storage.redis.RedisSessionHandler;
import io.github.encryptorcode.implementation.storage.redis.RedisUserHandler;
import io.github.encryptorcode.service.AuthenticationConfiguration;
import io.github.encryptorcode.storage.AAuthenticationHandler;
import io.github.encryptorcode.storage.ASessionHandler;
import io.github.encryptorcode.storage.AUserHandler;
import org.jooq.SQLDialect;
import redis.clients.jedis.Jedis;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;

public class ServerStartupListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {

        JdbcConfiguration<Session, User> jdbcConfiguration = new JdbcConfiguration<Session, User>() {
            @Override
            protected Connection getConnection() {
                String url = "jdbc:postgresql://localhost:5432/example";
                try {
                    Class.forName("org.postgresql.Driver");
                    return DriverManager.getConnection(url, "postgres", "postgres");
                } catch (SQLException | ClassNotFoundException throwable) {
                    return null;
                }
            }

            @Override
            protected SQLDialect getDialect() {
                return SQLDialect.POSTGRES;
            }

            @Override
            protected ConstructionHelper<Session> sessionConstructionHelper() {
                return Session::new;
            }

            @Override
            protected ConstructionHelper<User> userConstructionHelper() {
                return User::new;
            }
        };

        RedisConfiguration redisConfiguration = new RedisConfiguration() {
            private final Jedis jedis = new Jedis("localhost");

            @Override
            public Jedis getJedis() {
                return jedis;
            }
        };

        AUserHandler<User> userHandler = new RedisUserHandler<>(redisConfiguration, User.class, new JdbcUserHandler<Session, User>(jdbcConfiguration) {
            @Override
            public User constructUser() {
                return new User();
            }
        });
        ASessionHandler<Session, User> sessionHandler = new RedisSessionHandler<>(redisConfiguration, Session.class, new JdbcSessionHandler<Session, User>(jdbcConfiguration) {
            @Override
            public Session constructSession() {
                return new Session();
            }
        });
        AAuthenticationHandler authenticationHandler = new RedisAuthenticationHandler(redisConfiguration, new JdbcAuthenticationHandler<>(jdbcConfiguration));

        AuthenticationConfiguration.init(
                Collections.singletonList(
                        new GoogleAuthenticationProvider()
                ),
                authenticationHandler,
                sessionHandler,
                userHandler,
                "/",
                "/login",
                "example-auth"
        );
    }
}
