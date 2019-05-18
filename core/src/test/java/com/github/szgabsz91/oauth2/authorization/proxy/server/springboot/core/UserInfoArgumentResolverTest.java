package com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.core;

import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.api.model.UserInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.MethodParameter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.net.URI;
import java.security.Principal;
import java.util.Collection;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserInfoArgumentResolverTest {

    private UserInfoArgumentResolver userInfoArgumentResolver;

    @Mock
    private ServerWebExchange serverWebExchange;

    @Before
    public void setUp() {
        this.userInfoArgumentResolver = new UserInfoArgumentResolver();
    }

    @Test
    public void testSupportsParameterWithUserInfo() throws NoSuchMethodException {
        var methodParameter = new MethodParameter(this.getClass().getDeclaredMethod("dummyMethodUserInfo", UserInfo.class), 0);
        var result = this.userInfoArgumentResolver.supportsParameter(methodParameter);
        assertThat(result).isTrue();
    }

    @Test
    public void testSupportsParameterWithMonoOfUserInfo() throws NoSuchMethodException {
        var methodParameter = new MethodParameter(this.getClass().getDeclaredMethod("dummyMethodMonoUserInfo", Mono.class), 0);
        var result = this.userInfoArgumentResolver.supportsParameter(methodParameter);
        assertThat(result).isTrue();
    }

    @Test
    public void testSupportsParameterWithMonoOfString() throws NoSuchMethodException {
        var methodParameter = new MethodParameter(this.getClass().getDeclaredMethod("dummyMethodMonoString", Mono.class), 0);
        var result = this.userInfoArgumentResolver.supportsParameter(methodParameter);
        assertThat(result).isFalse();
    }

    @Test
    public void testSupportsParameterWithCollection() throws NoSuchMethodException {
        var methodParameter = new MethodParameter(this.getClass().getDeclaredMethod("dummyMethodCollection", Collection.class), 0);
        var result = this.userInfoArgumentResolver.supportsParameter(methodParameter);
        assertThat(result).isFalse();
    }

    @Test
    public void testResolveArgumentWithUserInfo() throws NoSuchMethodException {
        var methodParameter = new MethodParameter(this.getClass().getDeclaredMethod("dummyMethodUserInfo", UserInfo.class), 0);
        var expected = UserInfo.builder()
                .id("id")
                .email("email")
                .name("name")
                .gender("gender")
                .link(URI.create("https://google.com"))
                .picture(URI.create("https://facebook.com"))
                .build();
        var authentication = new OAuth2Authentication(expected, Set.of());
        when(this.serverWebExchange.getPrincipal()).thenReturn(Mono.just(authentication));

        var result = this.userInfoArgumentResolver.resolveArgument(methodParameter, null, serverWebExchange);

        StepVerifier.create(result)
                .expectNext(expected)
                .expectComplete()
                .verify();

        verify(this.serverWebExchange).getPrincipal();
    }

    @Test
    public void testResolveArgumentWithMonoOfUserInfo() throws NoSuchMethodException {
        var methodParameter = new MethodParameter(this.getClass().getDeclaredMethod("dummyMethodMonoUserInfo", Mono.class), 0);
        var expected = UserInfo.builder()
                .id("id")
                .email("email")
                .name("name")
                .gender("gender")
                .link(URI.create("https://google.com"))
                .picture(URI.create("https://facebook.com"))
                .build();
        var authentication = new OAuth2Authentication(expected, Set.of());
        when(this.serverWebExchange.getPrincipal()).thenReturn(Mono.just(authentication));

        var result = this.userInfoArgumentResolver.resolveArgument(methodParameter, null, serverWebExchange);

        StepVerifier.create(result)
                .expectNextMatches(mono -> {
                    @SuppressWarnings("unchecked")
                    var userInfoMono = ((Mono<UserInfo>) mono);
                    var userInfo = userInfoMono.block();
                    return expected.equals(userInfo);
                })
                .expectComplete()
                .verify();

        verify(this.serverWebExchange).getPrincipal();
    }

    private void dummyMethodUserInfo(UserInfo userInfo) {

    }

    private void dummyMethodMonoUserInfo(Mono<UserInfo> userInfoMono) {

    }

    private void dummyMethodMonoString(Mono<String> stringMono) {

    }

    private void dummyMethodCollection(Collection<String> collection) {

    }

    private void dummyMethod(Principal principal) {

    }

}
