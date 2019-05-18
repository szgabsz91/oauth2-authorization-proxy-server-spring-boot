package com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.core.configuration;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * Default implementation of {@link OAuth2AuthorizationProxyConfigurer} configurer interface that does nothing.
 *
 * @author szgabsz91
 */
public class OAuth2AuthorizationProxyConfigurerAdapter implements OAuth2AuthorizationProxyConfigurer {

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] getAntPatternsForPermitAll() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GrantedAuthority getAuthorizedUserAuthority() {
        return new SimpleGrantedAuthority("USER");
    }

}
