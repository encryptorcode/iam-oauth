# Customise storage for Authentication
We have in-built storage support for storing with the below
0. [JDBC/Database](#jdbcdatabase)
0. [Redis](#redis)
0. [Files](#files) (Default) (Not recommended for production)

Or you can also write your own implementation by extending [ASessionHandler](../src/main/java/io/github/encryptorcode/handlers/ASessionHandler.java), [AUserHandler](../src/main/java/io/github/encryptorcode/handlers/AUserHandler.java) and [AAuthenticationHandler](../src/main/java/io/github/encryptorcode/handlers/AAuthenticationHandler.java)

## JDBC/Database
We have a pre-written implementations for JDBC that uses [jOOQ library](https://github.com/jOOQ/jOOQ). 
To use these you may need to include the jOOQ library into your pom.xml file. 
So all you have to do is help me construct the Session and User objects as defined in the example below. 
First we'll make the configuration object like below

**MyJdbcConfiguration.java**
```java
import io.github.encryptorcode.implementation.storage.jdbc.JdbcConfiguration;
import java.sql.DriverManager;

class MyJdbcConfiguration extends JdbcConfiguration<Session, User>{
    @Override
    protected Connection getConnection() {
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/iam-db";
        String username = "root";
        String password = "root";
        return DriverManager.getConnection(url, username, password);
    }

    @Override
    protected ConstructionHelper<User> userConstructionHelper() {
        return User::new;
    }

    @Override
    protected ConstructionHelper<Session> sessionConstructionHelper() {
        return Session::new;
    }
}
```

**SessionHandler.java**
```java
import io.github.encryptorcode.implementation.storage.jdbc.JdbcConfiguration;import io.github.encryptorcode.implementation.storage.jdbc.JdbcSessionHandler;

class SessionHandler extends JdbcSessionHandler<Session, User>{
    public SessionHandler(){
        super(new MyJdbcConfiguration())
    }
    
    @Override
    public Session constructSession(){
        return new Session();
    }
}
```

**UserHandler.java**
```java
import io.github.encryptorcode.implementation.storage.jdbc.JdbcUserHandler;

class UserHandler extends JdbcUserHandler<User>{
    public SessionHandler(){
        super(new MyJdbcConfiguration())
    }
    
    @Override
    public User constructUser(){
        return new User();
    }
}
```

As AuthenticationHandler is not defined abstract you can simply create a new object of it in AuthenticationInitializer.init() 
or if you require you may also extend it to provide your custom implementation.

## Redis
As Redis is most widely used as a cache-storage, We have implemented redis to act as so.
In all the redis you will have to pass another handler for actual storage. 
You need to implement RedisConfiguration first, like below.

**MyRedisConfiguration.java**
```java
import io.github.encryptorcode.implementation.storage.redis.RedisConfiguration;
import redis.clients.jedis.Jedis;

class MyRedisConfiguration extends RedisConfiguration {
    
    @Override
    public Jedis getJedis() {
        return new Jedis("localhost", 6379);
    }
}
```

Once configuration is done, you can wrap your actual handler using the constructors to create an instance for redis handlers like below
```java
ASessionHandler<Session, User> = new RedisSessionHandler<>(new MyRedisConfiguration(), Session.class, new SessionHandler());
AUserHandler<User> userHandler = new RedisUserHandler<>(new MyRedisConfiguration(), User.class, new UserHandler());
AAuthenticationDetailHandler authenticationHandler = new RedisAuthenticationHandler(new MyRedisConfiguration(), new AuthenticationHandler());
```
In the above example SessionHandler, UserHandler and AuthenticationHandler are your custom implementations that redis handler wraps, to provide faster responses.

## Files
This handler stores all the data as files. 
This is the default handler that [AuthenticationInitializer](../src/main/java/io/github/encryptorcode/service/AuthenticationInitializer.java) uses for quickly starting up with this library.  
Anyways using this handler is **NOT RECOMMENDED** for production use-cases. A simple implementation for creating the file handler as anonymous classes are given below. 

```java
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
```

---

Prev: [Create your User and Session implementations](user-and-session-implementation.md) 
Next: [Setup your security for your users](security-handler.md)