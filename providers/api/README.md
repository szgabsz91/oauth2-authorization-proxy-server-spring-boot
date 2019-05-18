# OAuth2 Authorization Proxy Server Spring Boot - Providers - API

Each supported external OAuth2 provider is integrated by implementing the
[`IOauth2Provider`](src/main/java/com/github/szgabsz91/oauth2/authorization/proxy/server/springboot/providers/api/IOAuth2Provider.java)
interface that has the following methods:

* `String getId()`: returns the unique identifier of the external OAuth2 provider, e.g. *Facebook*, *Google*, etc.
* `boolean supports(String oauth2ProviderId)`: returns if this implementation supports the given OAuth2 provider id.
* `Mono<UserInfo> loadUserInfo(String accessToken)`: validates the given access token with the external OAuth2 provider
                                                     and returns the `UserInfo` object inside a `Mono`, or an empty
                                                     `Mono` if the access token is invalid

The
[`UserInfo`](src/main/java/com/github/szgabsz91/oauth2/authorization/proxy/server/springboot/providers/api/model/UserInfo.java)
class represents an authenticated user, containing her/his data provided by the external OAuth2 provider based on the
access token sent by the client. The following properties are retrieved:

* `id`: the unique identifier of the user in the database of the external OAuth2 provider
* `email`: the e-mail address of the user
* `name`: the full name of the user
* `gender`: the gender of the user (might be null)
* `link`: the link to the user's profile page on the external OAuth2 provider's site (might be null)
* `picture`: the URI of the user's profile picture
