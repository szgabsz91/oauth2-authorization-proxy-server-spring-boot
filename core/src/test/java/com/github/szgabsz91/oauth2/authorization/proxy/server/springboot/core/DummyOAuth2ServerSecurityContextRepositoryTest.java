package com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.core;

import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.api.model.UserInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;

import java.net.URI;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DummyOAuth2ServerSecurityContextRepositoryTest {

    private ServerSecurityContextRepository serverSecurityContextRepository;
    private GrantedAuthority authorizedUserAuthority;
    private UserInfo dummyUserInfo;

    @Mock
    private IUserAuthenticationListener userAuthenticationListener;

    @Before
    public void setUp() {
        this.authorizedUserAuthority = new SimpleGrantedAuthority("USER");
        this.dummyUserInfo = UserInfo.builder()
                .id("id")
                .email("email")
                .name("name")
                .gender("gender")
                .link(URI.create("https://google.com"))
                .picture(URI.create("https://facebook.com"))
                .build();
        var userAuthenticationListeners = Set.of(this.userAuthenticationListener);
        this.serverSecurityContextRepository = new DummyOAuth2ServerSecurityContextRepository(dummyUserInfo, userAuthenticationListeners, authorizedUserAuthority);
    }

    @Test
    public void testLoad() {
        var result = this.serverSecurityContextRepository.load(null);
        var securityContext = result.block();
        assertThat(securityContext).isNotNull();
        var authentication = securityContext.getAuthentication();
        assertThat(authentication).isInstanceOf(OAuth2Authentication.class);
        var oauth2Authentication = (OAuth2Authentication) authentication;
        assertThat(oauth2Authentication.getPrincipal()).isEqualTo(this.dummyUserInfo);
        assertThat(oauth2Authentication.getAuthorities()).isEqualTo(Set.of(this.authorizedUserAuthority));
        verify(this.userAuthenticationListener).authenticationSuccessful(this.dummyUserInfo);
    }

}
