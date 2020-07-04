# Setup Security Handler
Security handler is responsible for making your cookies secure.
By default [AuthenticationInitializer](../src/main/java/io/github/encryptorcode/service/AuthenticationInitializer.java) uses the [ZeroSecurityHandler](../src/main/java/io/github/encryptorcode/implementation/security/ZeroSecurityHandler.java) which doesn't provide any authentication and works with basic Random Logic.

You can also make use of the [BaseSecurityHandler](../src/main/java/io/github/encryptorcode/implementation/security/BaseSecurityHandler.java) by extending it and setting a encryption key and the rest will be handled by itself.

```java
import io.github.encryptorcode.implementation.security.BaseSecurityHandler;

class MySecurityHandler extends BaseSecurityHandler<User>{   
    @Override
    protected String getEncryptionKey() {
        return System.getProperty("secret.key");
    }
} 
```

This implementation uses AES/CBC/PKCS#5 for encryption and decryption with is fast and secure for validating cookies in each request.

---

Prev: [Customise your way to store of Users, Session and AuthenticationDetails](customise-storage.md) <br/>
Next: [Create handlers for server requests](server-request-handlers.md)