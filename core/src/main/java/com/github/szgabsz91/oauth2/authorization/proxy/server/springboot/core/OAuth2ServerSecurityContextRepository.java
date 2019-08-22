package com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.core;

import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.api.IOAuth2Provider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.Collection;
import java.util.Set;

/**
 * Custom {@link ServerSecurityContextRepository} implementation that authenticates the user based on HTTP headers and
 * access tokens from external OAuth2 providers.
 *
 * @author szgabsz91
 */
@Slf4j
public class OAuth2ServerSecurityContextRepository implements ServerSecurityContextRepository {

    /**
     * The name of the HTTP header that contains the OAuth2 provider, e.g. Facebook, Google, etc.
     */
    public static final String HEADER_OAUTH2_PROVIDER = "X-OAuth2-Provider";

    /**
     * The name of the HTTP header that contains the access token in the form of <code>"Bearer ${accessToken}"</code>.
     */
    public static final String HEADER_AUTHORIZATION = "Authorization";

    /**
     * The name of the query parameter that contains the OAuth2 provider, e.g. Facebook, Google, etc.
     */
    public static final String QUERY_PARAMETER_OAUTH2_PROVIDER = "oauth2_provider";

    /**
     * The name of the query parameter that contains the access token.
     */
    public static final String QUERY_PARAMETER_ACCESS_TOKEN = "access_token";

    private final Collection<IOAuth2Provider> oauth2Providers;
    protected final Collection<IUserAuthenticationListener> userAuthenticationListeners;
    protected final GrantedAuthority authorizedUserAuthority;

    /**
     * Constructor that sets the collection of supported {@link IOAuth2Provider}s, the collection of
     * {@link IUserAuthenticationListener}s that will be invoked on authentication success and failure events, and
     * the {@link GrantedAuthority} of authorized users.
     *
     * @param oauth2Providers the collection of supported {@link IOAuth2Provider}s
     * @param userAuthenticationListeners the collection of {@link IUserAuthenticationListener}s
     * @param authorizedUserAuthority the {@link GrantedAuthority} of authorized users
     */
    public OAuth2ServerSecurityContextRepository(
            Collection<IOAuth2Provider> oauth2Providers,
            Collection<IUserAuthenticationListener> userAuthenticationListeners,
            GrantedAuthority authorizedUserAuthority) {
        this.oauth2Providers = oauth2Providers;
        this.userAuthenticationListeners = userAuthenticationListeners;
        this.authorizedUserAuthority = authorizedUserAuthority;
    }

    /**
     * Throws a new {@link UnsupportedOperationException}.
     * @param serverWebExchange the {@link ServerWebExchange}
     * @param securityContext the {@link SecurityContext}
     * @return a {@link Mono}
     */
    @Override
    public Mono<Void> save(ServerWebExchange serverWebExchange, SecurityContext securityContext) {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * Returns a {@link Mono} of {@link SecurityContext} after authenticating the user based on the
     * <code>X-OAuth2-Provider</code> and <code>Authorization</code> HTTP headers.
     *
     * If the user cannot be authenticated because of an invalid access token, an empty {@link Mono} is returned.
     *
     * @param serverWebExchange the {@link ServerWebExchange}
     * @return a {@link Mono} containing the {@link SecurityContext} if the authentication was successful
     */
    @Override
    public Mono<SecurityContext> load(ServerWebExchange serverWebExchange) {
        log.debug("Authenticating user");

        return getOAuth2ProviderAndAccessToken(serverWebExchange)
                .flatMap(headerTuple -> {
                    var oauth2ProviderId = headerTuple.getT1();
                    var accessToken = headerTuple.getT2();

                    log.debug("OAuth2 provider id: {}", oauth2ProviderId);
                    log.debug("Access token: {}", accessToken);

                    return this.oauth2Providers
                            .stream()
                            .filter(oauth2Provider -> oauth2Provider.supports(oauth2ProviderId))
                            .findFirst()
                            .map(oauth2Provider -> oauth2Provider.loadUserInfo(accessToken))
                            .orElse(Mono.empty())
                            .switchIfEmpty(Mono.defer(() -> {
                                this.userAuthenticationListeners
                                        .forEach(userAuthenticationListener -> userAuthenticationListener.authenticationFailed(oauth2ProviderId, accessToken));
                                return Mono.empty();
                            }));
                })
                .map(userInfo -> {
                    log.debug("User info: {}", userInfo);
                    this.userAuthenticationListeners
                            .forEach(userAuthenticationListener -> userAuthenticationListener.authenticationSuccessful(userInfo));
                    var authentication = new OAuth2Authentication(userInfo, Set.of(this.authorizedUserAuthority));
                    return (SecurityContext) new SecurityContextImpl(authentication);
                })
                .switchIfEmpty(Mono.defer(() -> {
                    this.userAuthenticationListeners
                            .forEach(userAuthenticationListener -> userAuthenticationListener.authenticationFailed(null, null));
                    return Mono.empty();
                }));
    }

    private Mono<Tuple2<String, String>> getOAuth2ProviderAndAccessToken(ServerWebExchange serverWebExchange) {
        return getOAuth2ProviderAndAccessTokenFromHeader(serverWebExchange)
                .switchIfEmpty(getOAuth2ProviderAndAccessTokenFromQueryParameters(serverWebExchange));
    }

    private Mono<Tuple2<String, String>> getOAuth2ProviderAndAccessTokenFromHeader(ServerWebExchange serverWebExchange) {
        return getHeader(serverWebExchange, HEADER_OAUTH2_PROVIDER)
                .zipWith(getHeader(serverWebExchange, HEADER_AUTHORIZATION))
                .map(headerTuple -> headerTuple.mapT2(authorizationHeader -> authorizationHeader.substring("Bearer ".length())));
    }

    private Mono<Tuple2<String, String>> getOAuth2ProviderAndAccessTokenFromQueryParameters(ServerWebExchange serverWebExchange) {
        return getQueryParameter(serverWebExchange, QUERY_PARAMETER_OAUTH2_PROVIDER)
                .zipWith(getQueryParameter(serverWebExchange, QUERY_PARAMETER_ACCESS_TOKEN));
    }

    private Mono<String> getHeader(ServerWebExchange serverWebExchange, String headerName) {
        var request = serverWebExchange.getRequest();
        var headers = request.getHeaders();
        var headerValue = headers.getFirst(headerName);

        if (headerValue == null) {
            return Mono.empty();
        }

        return Mono.just(headerValue);
    }

    private Mono<String> getQueryParameter(ServerWebExchange serverWebExchange, String queryParameterName) {
        var request = serverWebExchange.getRequest();
        var queryParams = request.getQueryParams();
        String queryParameterValue = queryParams.getFirst(queryParameterName);

        if (queryParameterValue == null) {
            return Mono.empty();
        }

        return Mono.just(queryParameterValue);
    }

}
