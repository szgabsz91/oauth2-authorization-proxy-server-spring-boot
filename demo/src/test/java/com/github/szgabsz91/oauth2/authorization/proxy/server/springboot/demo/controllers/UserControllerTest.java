package com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.demo.controllers;

import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.core.OAuth2ServerSecurityContextRepository;
import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.demo.OAuth2AuthorizationProxyDemoApplication;
import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.api.IOAuth2Provider;
import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.api.model.UserInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = { OAuth2AuthorizationProxyDemoApplication.class, UserControllerTest.MyConfig.class }
)
public class UserControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private IOAuth2Provider oauth2Provider;

    @Configuration
    public static class MyConfig {

        @Bean
        public IOAuth2Provider oauth2Provider() {
            return mock(IOAuth2Provider.class);
        }

    }

    @Before
    public void setUp() {
        reset(oauth2Provider);
    }

    @Test
    public void testMeWithAuthorizedUser() throws URISyntaxException {
        var oauth2ProviderId = "Twitter";
        var accessToken = "access-token";
        var expected = UserInfo.builder()
                .id("id")
                .email("email")
                .name("name")
                .gender("gender")
                .link(URI.create("https://google.com"))
                .picture(URI.create("https://facebook.com"))
                .build();

        when(this.oauth2Provider.supports(oauth2ProviderId)).thenReturn(true);
        when(this.oauth2Provider.loadUserInfo(accessToken)).thenReturn(Mono.just(expected));

        var headers = new HttpHeaders();
        headers.set(OAuth2ServerSecurityContextRepository.HEADER_OAUTH2_PROVIDER, oauth2ProviderId);
        headers.set("Authorization", "Bearer " + accessToken);

        var responseEntity = this.restTemplate.exchange(new URI("/users/me"), HttpMethod.GET, new HttpEntity<>(headers), UserInfo.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        var result = responseEntity.getBody();
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testMeWithUnauthorizedUser() throws URISyntaxException {
        var oauth2ProviderId = "Twitter";
        var accessToken = "access-token";

        when(this.oauth2Provider.supports(oauth2ProviderId)).thenReturn(true);
        when(this.oauth2Provider.loadUserInfo(accessToken)).thenReturn(Mono.empty());

        var headers = new HttpHeaders();
        headers.set(OAuth2ServerSecurityContextRepository.HEADER_OAUTH2_PROVIDER, oauth2ProviderId);
        headers.set("Authorization", "Bearer " + accessToken);

        var responseEntity = this.restTemplate.exchange(new URI("/users/me"), HttpMethod.GET, new HttpEntity<>(headers), Void.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

}
