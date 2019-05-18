package com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.core.configuration;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OAuth2AuthorizationProxyConfigurerAdapterTest {

    private OAuth2AuthorizationProxyConfigurerAdapter oauth2AuthorizationProxyConfigurerAdapter;

    @Before
    public void setUp() {
        this.oauth2AuthorizationProxyConfigurerAdapter = new OAuth2AuthorizationProxyConfigurerAdapter();
    }

    @Test
    public void testGetAntPatternsForPermitAll() {
        var result = this.oauth2AuthorizationProxyConfigurerAdapter.getAntPatternsForPermitAll();
        assertThat(result).isNull();
    }

    @Test
    public void testGetAuthorizedUserAuthority() {
        var result = this.oauth2AuthorizationProxyConfigurerAdapter.getAuthorizedUserAuthority();
        assertThat(result.getAuthority()).isEqualTo("USER");
    }

}
