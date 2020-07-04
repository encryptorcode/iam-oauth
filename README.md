# I AM open oauth
<!-- Git badges -->
[![Maven Central](https://img.shields.io/maven-central/v/io.github.encryptorcode/iam-oauth.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.encryptorcode%22%20AND%20a:%22iam-oauth%22)
[![License](https://img.shields.io/github/license/encryptorcode/iam-oauth)](https://github.com/encryptorcode/iam-oauth/blob/master/LICENSE)
[![Code Climate](https://api.codeclimate.com/v1/badges/43de378ed28020dcca28/maintainability)](https://codeclimate.com/github/encryptorcode/iam-oauth/maintainability)
[![Dependabot](https://badgen.net/dependabot/encryptorcode/iam-oauth/182090705?icon=dependabot)](https://github.com/encryptorcode/iam-oauth)
[![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/io.github.encryptorcode/iam-oauth?server=https%3A%2F%2Foss.sonatype.org)](https://oss.sonatype.org/content/repositories/snapshots/io/github/encryptorcode/iam-oauth/)
> The Quickest way to support authentication on your app.

## Installation
#### Maven
```xml
<dependency>
  <groupId>io.github.encryptorcode</groupId>
  <artifactId>iam-oauth</artifactId>
  <version>2.0.0</version>
</dependency>
```
#### Groovy
```groovy
implementation 'io.github.encryptorcode:iam-oauth:2.0.0'
```
#### Kotlin DSL
```groovy
implementation("io.github.encryptorcode:iam-oauth:2.0.0")
```

## What?
This is a library for all your authentication needs. 
From the scratch it's made to give you full flexibility for advanced implementation, 
also making sure it's easy to implement for starters.

## Usage
You need to write bare minimal to set up your authentication.
```java
AuthenticationInitializer.newInstance(Session::new, User::new)
        .addOAuthProvider(new GoogleAuthenticationProvider())
        .initialize();
```
* `GoogleAuthenticationProvider` An implementation for supporting Oauth 2.0 authentication using Google.
* We also have ready-made implementations for supporting storage with files, db and redis.

For complete example, check [example folder](example/src/main) given in the repository.

## Setting it up
To completely customising and setting up authentication you can follow the guides below.
1. [Configure your oauth provider(s)](/wiki/configuring-oauth-provider.md)
1. [Create your User and Session implementations](/wiki/user-and-session-implementation.md)
1. [Customise your way to store of Users, Session and AuthenticationDetails](/wiki/customise-storage.md)
1. [Setup your security for your users](/wiki/security-handler.md)
1. [Handle server requests](/wiki/server-request-handlers.md)

## API
**Getting instance of authentication service**
```java
AuthenticationService<Session, User> authenticationService = AuthenticationService.getInstance();
```

**Getting the current user**
```java
User currentUser = authenticationService.getCurrentUser();
```

**Getting the current session**
```java
Session currentSession = authenticationService.getCurrentUser();
```

> Note: Both user and session will be set only in a HttpRequest. It will get a null if you invoke this on a TimerTask or Scheduler.

## License
MIT 