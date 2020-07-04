# Creating your user and session implementations
First things first. Let's create a simple implementation for user and session. 
We have most of the required fields already, Say for a user you would also like to store the user role. The implementations will be as below.

**Session.java**
```java
import io.github.encryptorcode.entity.ASession;

public class Session extends ASession {
}
```

**User.java**
```java
import io.github.encryptorcode.entity.AUser;

public class User extends AUser {
    private Role role;
    
    public enum Role{
        ADMIN,
        USER    
    }
    
    // getter and setters for Role
}
```

As we already have added annotations to the pre-defined fields, if your project uses JPA and/or Gson.

> Note: If you are using JPA or Hibernate, You might need additional columns for storing id and making it the primary key

---  

Prev: [Configure your oauth provider(s)](configuring-oauth-provider.md)
Next: [Customise your way to store of Users, Session and AuthenticationDetails](customise-storage.md)  