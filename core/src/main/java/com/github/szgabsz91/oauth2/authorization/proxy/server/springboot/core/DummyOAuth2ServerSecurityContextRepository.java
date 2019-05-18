package com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.core;

import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.api.model.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Set;

/**
 * Dummy subclass of {@link OAuth2ServerSecurityContextRepository} that always returns the same {@link UserInfo}.
 *
 * @author szgabsz91
 */
@Slf4j
public class DummyOAuth2ServerSecurityContextRepository extends OAuth2ServerSecurityContextRepository {

    private final UserInfo dummyUserInfo;

    /**
     * Constructor that sets the dummy {@link UserInfo} to use, the collection of {@link IUserAuthenticationListener}s
     * that will be invoked on authentication success and failure events, and the {@link GrantedAuthority} of
     * authorized users.
     *
     * @param dummyUserInfo the dummy {@link UserInfo} to use
     * @param userAuthenticationListeners the collection of {@link IUserAuthenticationListener}s
     * @param authorizedUserAuthority the {@link GrantedAuthority} of authorized users
     */
    public DummyOAuth2ServerSecurityContextRepository(
            UserInfo dummyUserInfo,
            Collection<IUserAuthenticationListener> userAuthenticationListeners,
            GrantedAuthority authorizedUserAuthority) {
        super(null,  userAuthenticationListeners, authorizedUserAuthority);
        this.dummyUserInfo = dummyUserInfo;
    }

    /**
     * Returns a {@link SecurityContext} inside a {@link Mono} that contains the preconfigured dummy {@link UserInfo}.
     * @param serverWebExchange the {@link ServerWebExchange}
     * @return a {@link Mono} containing the {@link SecurityContext} with the dummy {@link UserInfo}
     */
    @Override
    public Mono<SecurityContext> load(ServerWebExchange serverWebExchange) {
        log.debug("Dummy user info: {}", this.dummyUserInfo);
        this.userAuthenticationListeners
                .forEach(userAuthenticationListener -> userAuthenticationListener.authenticationSuccessful(this.dummyUserInfo));
        var authentication = new OAuth2Authentication(this.dummyUserInfo, Set.of(this.authorizedUserAuthority));
        var securityContext = new SecurityContextImpl(authentication);
        return Mono.just(securityContext);
    }

}
