# OAuth2 Authorization Proxy Server Spring Boot - Providers - Facebook

Integrates [Facebook](https://facebook.com) as an external OAuth2 provider.

The unique identifier and th expected value in the `X-OAuth2-Provider` header is *Facebook*.

## Configuration

The following configuration properties are supported:

* `oauth2.authorization.proxy.server.springboot.providers.facebook.appId`: the application id of the Facebook application.
* `oauth2.authorization.proxy.server.springboot.providers.facebook.appSecret`: the application secret, currently optional.

The Facebook OAuth2 provider is only used if the `oauth2.authorization.proxy.server.springboot.providers.facebook.appId`
is provided by the application, otherwise none of the beans are created, and thus the Facebook OAuth2 provider won't be
available.

## Integration

[Facebook4J](https://facebook4j.github.io/en/index.html) is used for the integration.

The [Facebook](https://facebook4j.github.io/javadoc/facebook4j/Facebook.html) class's `getOAuthAccessTokenInfo` and
`getMe` methods are used to verify the given access token and retrieve the user information from Facebook.

## Testing

The [Facebook Graph API Explorer](https://developers.facebook.com/tools/explorer/216750812448511) can be used for
testing access tokens provided by Facebook.
