# OAuth2 Authorization Proxy Server Spring Boot - Providers - Google

Integrates [Google](https://developers.google.com/identity/protocols/OAuth2) as an external OAuth2 provider.

The unique identifier and th expected value in the `X-OAuth2-Provider` header is *Google*.

## Configuration

The following configuration properties are supported:

* `oauth2.authorization.proxy.server.springboot.providers.google.clientId`: the client id of the Google OAuth2 application.
* `oauth2.authorization.proxy.server.springboot.providers.google.clientSecret` the client secret, currently optional.

The Google OAuth2 provider is only used if the `oauth2.authorization.proxy.server.springboot.providers.google.clientId`
is provided by the application, otherwise none of the beans are created, and thus the Google OAuth2 provider won't be
available.

## Integration

[Google API Client Library for Java](https://developers.google.com/api-client-library/java/google-api-java-client/oauth2)
is used for the integration.

The
[Oauth2](https://developers.google.com/resources/api-libraries/documentation/oauth2/v2/java/latest/com/google/api/services/oauth2/Oauth2.html)
class's `tokeninfo` and `userinfo` methods are used to verify the given access token and retrieve the user information
from Google.

## Testing

The [Google OAuth 2.0 Playground](https://developers.google.com/oauthplayground) can be used for
testing access tokens provided by Google.
 