package com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.demo.configuration;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OAuth2AuthorizationProxyConfigurationTest {

    private OAuth2AuthorizationProxyConfiguration oauth2AuthorizationProxyConfiguration;

    @Before
    public void setUp() {
        this.oauth2AuthorizationProxyConfiguration = new OAuth2AuthorizationProxyConfiguration();
    }

    @Test
    public void testGetAntPatternsForPermitAll() {
        var result = this.oauth2AuthorizationProxyConfiguration.getAntPatternsForPermitAll();
        assertThat(result).containsExactlyInAnyOrder("/", "/*.html", "/*.css", "/*.js", "/webjars/**");
    }

}
