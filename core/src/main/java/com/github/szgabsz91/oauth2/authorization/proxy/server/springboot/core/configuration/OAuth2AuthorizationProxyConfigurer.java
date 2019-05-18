package com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.core.configuration;

import org.springframework.security.core.GrantedAuthority;

/**
 * Configurer interface for the OAuth Authorization Proxy module.
 *
 * @author szgabsz91
 */
public interface OAuth2AuthorizationProxyConfigurer {

    /**
     * Returns the ant patterns for the <i>permitAll</i> Spring Security rule.
     * @return the ant patterns for the <i>permitAll</i> Spring Security rule.
     */
    String[] getAntPatternsForPermitAll();

    /**
     * Returns the authority that an authorized user has.
     * @return the authority that an authorized user has
     */
    GrantedAuthority getAuthorizedUserAuthority();

}
