package com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.core;

import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.api.IOAuth2Provider;
import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.api.model.UserInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.net.URI;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OAuth2ServerSecurityContextRepositoryTest {

    private ServerSecurityContextRepository serverSecurityContextRepository;

    private GrantedAuthority authorizedUserAuthority;

    @Mock
    private IOAuth2Provider facebookOAuth2Provider;

    @Mock
    private IUserAuthenticationListener userAuthenticationListener;

    @Mock
    private ServerWebExchange serverWebExchange;

    @Mock
    private ServerHttpRequest serverHttpRequest;

    @BeforeEach
    public void setUp() {
        var oauth2Providers = Set.of(this.facebookOAuth2Provider);
        this.authorizedUserAuthority = new SimpleGrantedAuthority("USER");
        var userAuthenticationListeners = Set.of(this.userAuthenticationListener);
        this.serverSecurityContextRepository = new OAuth2ServerSecurityContextRepository(oauth2Providers, userAuthenticationListeners, authorizedUserAuthority);
    }

    @Test
    public void testSave() {
        var exception = assertThrows(UnsupportedOperationException.class, () -> this.serverSecurityContextRepository.save(null, null));
        assertThat(exception).hasMessage("Not supported");
    }

    @Test
    public void testLoadWithEmptyHeader() {
        var httpHeaders = new HttpHeaders();

        when(this.serverWebExchange.getRequest())
                .thenReturn(this.serverHttpRequest);
        when(this.serverHttpRequest.getHeaders())
                .thenReturn(httpHeaders);
        when(this.serverHttpRequest.getQueryParams())
                .thenReturn(new LinkedMultiValueMap<>());

        var result = this.serverSecurityContextRepository.load(serverWebExchange);

        StepVerifier.create(result)
                .expectComplete()
                .verify();

        verify(this.serverWebExchange, times(4)).getRequest();
        verify(this.serverHttpRequest, times(2)).getHeaders();
        verify(this.serverHttpRequest, times(2)).getQueryParams();
        verify(this.userAuthenticationListener).authenticationFailed(null, null);
    }

    @Test
    public void testLoadWithNonSupportedOAuth2ProviderId() {
        var oauth2ProviderId = "non-supported";

        var httpHeaders = new HttpHeaders();
        httpHeaders.add(OAuth2ServerSecurityContextRepository.HEADER_OAUTH2_PROVIDER, oauth2ProviderId);
        httpHeaders.add(OAuth2ServerSecurityContextRepository.HEADER_AUTHORIZATION, "Bearer access-token");

        when(this.serverWebExchange.getRequest())
                .thenReturn(this.serverHttpRequest);
        when(this.serverHttpRequest.getHeaders())
                .thenReturn(httpHeaders);
        when(this.serverHttpRequest.getQueryParams())
                .thenReturn(new LinkedMultiValueMap<>());
        when(this.facebookOAuth2Provider.supports(oauth2ProviderId))
                .thenReturn(false);

        var result = this.serverSecurityContextRepository.load(serverWebExchange);

        StepVerifier.create(result)
                .expectComplete()
                .verify();

        verify(this.serverWebExchange, times(4)).getRequest();
        verify(this.serverHttpRequest, times(2)).getHeaders();
        verify(this.serverHttpRequest, times(2)).getQueryParams();
        verify(this.facebookOAuth2Provider).supports(oauth2ProviderId);
        verify(this.userAuthenticationListener).authenticationFailed(null, null);
    }

    @Test
    public void testLoadWithSupportedOAuth2ProviderIdAndAuthenticationFailure() {
        var oauth2ProviderId = "Facebook";
        var accessToken = "access-token";

        var httpHeaders = new HttpHeaders();
        httpHeaders.add(OAuth2ServerSecurityContextRepository.HEADER_OAUTH2_PROVIDER, oauth2ProviderId);
        httpHeaders.add(OAuth2ServerSecurityContextRepository.HEADER_AUTHORIZATION, "Bearer " + accessToken);

        when(this.serverWebExchange.getRequest())
                .thenReturn(this.serverHttpRequest);
        when(this.serverHttpRequest.getHeaders())
                .thenReturn(httpHeaders);
        when(this.serverHttpRequest.getQueryParams())
                .thenReturn(new LinkedMultiValueMap<>());
        when(this.facebookOAuth2Provider.supports(oauth2ProviderId))
                .thenReturn(true);
        when(this.facebookOAuth2Provider.loadUserInfo(accessToken))
                .thenReturn(Mono.empty());

        var result = this.serverSecurityContextRepository.load(serverWebExchange);

        StepVerifier.create(result)
                .expectComplete()
                .verify();

        verify(this.serverWebExchange, times(4)).getRequest();
        verify(this.serverHttpRequest, times(2)).getHeaders();
        verify(this.serverHttpRequest, times(2)).getQueryParams();
        verify(this.facebookOAuth2Provider).supports(oauth2ProviderId);
        verify(this.facebookOAuth2Provider).loadUserInfo(accessToken);
        verify(this.userAuthenticationListener).authenticationFailed(oauth2ProviderId, accessToken);
    }

    @Test
    public void testLoadWithSupportedOAuth2ProviderIdInHeaderAndAuthenticationSuccess() {
        var oauth2ProviderId = "Facebook";
        var accessToken = "access-token";
        var userInfo = UserInfo.builder()
                .id("id")
                .email("email")
                .name("name")
                .gender("gender")
                .link(URI.create("https://google.com"))
                .picture(URI.create("https://facebook.com"))
                .build();

        var httpHeaders = new HttpHeaders();
        httpHeaders.add(OAuth2ServerSecurityContextRepository.HEADER_OAUTH2_PROVIDER, oauth2ProviderId);
        httpHeaders.add(OAuth2ServerSecurityContextRepository.HEADER_AUTHORIZATION, "Bearer " + accessToken);

        when(this.serverWebExchange.getRequest())
                .thenReturn(this.serverHttpRequest);
        when(this.serverHttpRequest.getHeaders())
                .thenReturn(httpHeaders);
        when(this.serverHttpRequest.getQueryParams())
                .thenReturn(new LinkedMultiValueMap<>());
        when(this.facebookOAuth2Provider.supports(oauth2ProviderId))
                .thenReturn(true);
        when(this.facebookOAuth2Provider.loadUserInfo(accessToken))
                .thenReturn(Mono.just(userInfo));

        var result = this.serverSecurityContextRepository.load(serverWebExchange);

        StepVerifier.create(result)
                .expectNextMatches(securityContext -> securityContext.getAuthentication() instanceof OAuth2Authentication &&
                        ((OAuth2Authentication) securityContext.getAuthentication()).getPrincipal().equals(userInfo) &&
                        securityContext.getAuthentication().getAuthorities().equals(Set.of(this.authorizedUserAuthority))
                )
                .expectComplete()
                .verify();

        verify(this.serverWebExchange, times(4)).getRequest();
        verify(this.serverHttpRequest, times(2)).getHeaders();
        verify(this.serverHttpRequest, times(2)).getQueryParams();
        verify(this.facebookOAuth2Provider).supports(oauth2ProviderId);
        verify(this.facebookOAuth2Provider).loadUserInfo(accessToken);
        verify(this.userAuthenticationListener).authenticationSuccessful(userInfo);
    }

    @Test
    public void testLoadWithSupportedOAuth2ProviderIdInQueryParametersAndAuthenticationSuccess() {
        var oauth2ProviderId = "Facebook";
        var accessToken = "access-token";
        var userInfo = UserInfo.builder()
                .id("id")
                .email("email")
                .name("name")
                .gender("gender")
                .link(URI.create("https://google.com"))
                .picture(URI.create("https://facebook.com"))
                .build();

        var queryParameters = new LinkedMultiValueMap<String, String>();
        queryParameters.add(OAuth2ServerSecurityContextRepository.QUERY_PARAMETER_OAUTH2_PROVIDER, oauth2ProviderId);
        queryParameters.add(OAuth2ServerSecurityContextRepository.QUERY_PARAMETER_ACCESS_TOKEN, accessToken);

        when(this.serverWebExchange.getRequest())
                .thenReturn(this.serverHttpRequest);
        when(this.serverHttpRequest.getHeaders())
                .thenReturn(new HttpHeaders());
        when(this.serverHttpRequest.getQueryParams())
                .thenReturn(queryParameters);
        when(this.facebookOAuth2Provider.supports(oauth2ProviderId))
                .thenReturn(true);
        when(this.facebookOAuth2Provider.loadUserInfo(accessToken))
                .thenReturn(Mono.just(userInfo));

        var result = this.serverSecurityContextRepository.load(serverWebExchange);

        StepVerifier.create(result)
                .expectNextMatches(securityContext -> securityContext.getAuthentication() instanceof OAuth2Authentication &&
                        ((OAuth2Authentication) securityContext.getAuthentication()).getPrincipal().equals(userInfo) &&
                        securityContext.getAuthentication().getAuthorities().equals(Set.of(this.authorizedUserAuthority))
                )
                .expectComplete()
                .verify();

        verify(this.serverWebExchange, times(4)).getRequest();
        verify(this.serverHttpRequest, times(2)).getHeaders();
        verify(this.serverHttpRequest, times(2)).getQueryParams();
        verify(this.facebookOAuth2Provider).supports(oauth2ProviderId);
        verify(this.facebookOAuth2Provider).loadUserInfo(accessToken);
        verify(this.userAuthenticationListener).authenticationSuccessful(userInfo);
    }

}
