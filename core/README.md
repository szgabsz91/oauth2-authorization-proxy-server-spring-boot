# OAuth2 Authorization Proxy Server Spring Boot - Core

This is the core subproject of `oauth2-authorization-proxy-server-spring-boot` that

* creates the Spring Security filter chain,
* defines configuration options for the project,
* provides a
  [`HandlerMethodArgumentResolver`](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/reactive/result/method/HandlerMethodArgumentResolver.html)
  implementation to resolve
  [`UserInfo`](../providers/api/src/main/java/com/github/szgabsz91/oauth2/authorization/proxy/server/springboot/providers/api/model/UserInfo.java)
  controller method parameters and
* manages the authentication of the users.

## Spring Security filter chain

By default, HTTP basic authentication, form login and logout are disabled, as
`oauth2-authorization-proxy-server-spring-boot` was designed with pure REST APIs in mind.

## Configuration options

There are two possible ways of configuring `oauth2-authorization-proxy-server-spring-boot`:

* the [`OAuth2AuthorizationProxyConfigurer`](src/main/java/com/github/szgabsz91/oauth2/authorization/proxy/server/springboot/core/configuration/OAuth2AuthorizationProxyConfigurer.java)
  that provides implementation time configuration and
* configuration properties that can be easily changed for each deployment.

### OAuth2AuthorizationProxyConfigurer

The [`OAuth2AuthorizationProxyConfigurer`](src/main/java/com/github/szgabsz91/oauth2/authorization/proxy/server/springboot/core/configuration/OAuth2AuthorizationProxyConfigurer.java)
has two methods that can be implemented by services using `oauth2-authorization-proxy-server-spring-boot`:

* `getAntPatternsForPermitAll`: returns a `String` array containing ant patterns for paths that should be permitted
                                by the Spring Security filter chain. Default value: empty array.
* `getAuthorizedUserAuthority`: returns the authority of normal authenticated users. Default value: *USER*.

### Configuration properties

* `oauth2.authorization.proxy.server.springboot.dummyUser`: a dummy [`UserInfo`](../providers/api/src/main/java/com/github/szgabsz91/oauth2/authorization/proxy/server/springboot/providers/api/model/UserInfo.java)
                                                            object that should be used for every request. This property
                                                            is useful for testing purposes, as it makes every request's
                                                            user authenticated by default, without actually reaching out
                                                            to any external OAuth2 providers.

## Resolving UserInfo in controller methods

There is a custom [`HandlerMethodArgumentResolver`](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/reactive/result/method/HandlerMethodArgumentResolver.html)
implementation called [`UserInfoArgumentResolver`](src/main/java/com/github/szgabsz91/oauth2/authorization/proxy/server/springboot/core/UserInfoArgumentResolver.java)
that to resolve Spring WebFlux controller method parameters of type [`UserInfo`](../providers/api/src/main/java/com/github/szgabsz91/oauth2/authorization/proxy/server/springboot/providers/api/model/UserInfo.java)
and `Mono<UserInfo>`.

## Authentication

If the `oauth2.authorization.proxy.server.springboot.dummyUser` property is not set,
[`OAuth2ServerSecurityContextRepository`](src/main/java/com/github/szgabsz91/oauth2/authorization/proxy/server/springboot/core/OAuth2ServerSecurityContextRepository.java)
performs the authentication of the users.

There can be any number of [`IOAuth2Provider`](../providers/api/src/main/java/com/github/szgabsz91/oauth2/authorization/proxy/server/springboot/providers/api/IOAuth2Provider.java)s
in the Spring context, each one having a unique identifier like *Facebook* or *Google*. This identifier selects
which external OAuth2 provider handles the authentication of requests going through the provider.

The core concept is that clients of services using `oauth2-authorization-proxy-server-spring-boot` must send the
`Authorization` header containing a bearer access token, as well as a special `X-OAuth2-Provider` header containing the
unique identifier of an external OAuth2 provider. The appropriate `IOAuth2Provider` will reach out to the external
OAuth2 provider and that will perform the actual authentication. If the access token is valid according to the external
OAuth2 provider, the user authenticated successfully.

Services using `oauth2-authorization-proxy-server-spring-boot` can implement the
[`IUserAuthenticationListener`](src/main/java/com/github/szgabsz91/oauth2/authorization/proxy/server/springboot/core/IUserAuthenticationListener.java)
interface to be notified about successful and failed authentication attempts.

As an example, let's say a request contains the following two headers:

* `Authorization: Bearer xyz`
* `X-OAuth2-Provider: Google`

In this case, the `IOAuth2Provider` with the identifier of *Google* will reach out to Google
to validate the access token *xyz*. If Google says the access token is valid, the user will
be authenticated.

Note that only supported values can come in the `X-OAuth2-Provider` header, as otherwise an error
will occur. Currently two external OAuth2 providers are supported: [Facebook](../providers/facebook) and
[Google](../providers/google).
