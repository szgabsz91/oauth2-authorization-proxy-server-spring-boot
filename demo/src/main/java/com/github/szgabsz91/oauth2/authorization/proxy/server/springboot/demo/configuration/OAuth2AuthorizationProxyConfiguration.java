package com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.demo.configuration;

import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.core.configuration.OAuth2AuthorizationProxyConfigurerAdapter;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for the OAuth2 Authorization Proxy Spring Boot module that allows anonymous access
 * to static resources.
 *
 * @author szgabsz91
 */
@Configuration
public class OAuth2AuthorizationProxyConfiguration extends OAuth2AuthorizationProxyConfigurerAdapter {

    /**
     * Returns the set of ant patterns for the permit all Spring Security rule.
     * @return the set of ant patterns for the permit all Spring Security rule
     */
    @Override
    public String[] getAntPatternsForPermitAll() {
        return new String[] { "/", "/*.html", "/*.css", "/*.js", "/webjars/**" };
    }

}
