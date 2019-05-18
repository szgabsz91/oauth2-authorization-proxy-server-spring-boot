# OAuth2 Authorization Proxy Server Spring Boot - Starter

This starter project depends on all the other subprojects so that Spring Boot projects can depend on only this one
and all the necessary beans are created automatically.

All the other subprojects contain a `META-INF/spring.factories` file that contains the appropriate configuration classes
under the `org.springframework.boot.autoconfigure.EnableAutoConfiguration` key. Spring Boot reads these classes and
uses them to create the Spring Beans defined by the configuration classes.

Note that some beans are conditional, for example the OAuth2 providers are only created if their related configuration
properties are present.
