# OAuth2 Authorization Proxy Server Spring Boot - Demo

This project demonstrates the usage of `oauth2-authorization-proxy-server-spring-boot`.

The main components of the demo application are:

* [`Item`](src/main/java/com/github/szgabsz91/oauth2/authorization/proxy/server/springboot/demo/entities/Item.java):
    entity class containing an `id`, `description` and `email` property. These items are stored in a MongoDB database.
    The `email` property is the e-mail address of the owner who created the item.
* [`ItemRepository`](src/main/java/com/github/szgabsz91/oauth2/authorization/proxy/server/springboot/demo/repositories/ItemRepository.java):
    reactive CRUD repository for items. Contains an additional `findByEmail` method for returning only the items with
    the given e-mail address.
* [`ItemController`](src/main/java/com/github/szgabsz91/oauth2/authorization/proxy/server/springboot/demo/controllers/ItemController.java):
    reactive Spring Webflux controller for managing (retrieving and saving) items. Retrieval can be done in two ways:
    either all the saved items are returned, or only those whose `email` matches the e-amil address of the authenticated
    user. During saving, the item's `email` is filled in using the `email` property of the authenticated user.
* [`UserController`](src/main/java/com/github/szgabsz91/oauth2/authorization/proxy/server/springboot/demo/controllers/UserController.java):
    reactive Spring Webflux controller for returning the `UserInfo` of the authenticated user.
* [`UserAuthenticationListener`](src/main/java/com/github/szgabsz91/oauth2/authorization/proxy/server/springboot/demo/security/UserAuthenticationListener.java):
    logs all successful and failed authentication attempts.
* [`OAuth2AuthorizationProxyConfiguration`](src/main/java/com/github/szgabsz91/oauth2/authorization/proxy/server/springboot/demo/configuration/OAuth2AuthorizationProxyConfiguration.java):
    configures static resources (HTML, CSS, JS and webjar files) to be always permitted.

The project also has a simple frontend page for each supported external OAuth2 provider that displays the profile
picture, the e-mail address, the user id, the gender and the link to the user's profile page. The user can save new
items, list all the items or list the items created by her/him.
