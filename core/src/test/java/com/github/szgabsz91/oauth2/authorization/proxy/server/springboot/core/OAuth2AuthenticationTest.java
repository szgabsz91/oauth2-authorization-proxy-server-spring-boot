package com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.core;

import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.api.model.UserInfo;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.net.URI;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class OAuth2AuthenticationTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testConstructorAndGetters() {
        var userInfo = UserInfo.builder()
                .id("id")
                .email("email")
                .name("name")
                .gender("gender")
                .link(URI.create("https://google.com"))
                .picture(URI.create("https://facebook.com"))
                .build();
        var authorities = Set.of(new SimpleGrantedAuthority("USER"));
        var oauth2Authentication = new OAuth2Authentication(userInfo, authorities);

        assertThat(oauth2Authentication.getPrincipal()).isEqualTo(userInfo);
        assertThat(oauth2Authentication.getName()).isEqualTo(userInfo.getEmail());
        assertThat(oauth2Authentication.getAuthorities()).isEqualTo(authorities);
        assertThat(oauth2Authentication.getCredentials()).isNull();
        assertThat(oauth2Authentication.getDetails()).isNull();
        assertThat(oauth2Authentication.isAuthenticated()).isTrue();
    }

    @Test
    public void testSetAuthenticated() {
        var oauth2Authentication = new OAuth2Authentication(null, null);

        thrown.expect(UnsupportedOperationException.class);
        thrown.expectMessage("Not supported");

        oauth2Authentication.setAuthenticated(true);
    }

}
