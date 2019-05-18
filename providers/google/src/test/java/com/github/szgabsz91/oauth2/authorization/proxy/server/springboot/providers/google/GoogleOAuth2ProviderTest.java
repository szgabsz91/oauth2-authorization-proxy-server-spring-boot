package com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.google;

import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.google.configuration.GoogleOAuth2ProviderConfigurationProperties;
import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.api.IOAuth2Provider;
import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.api.model.UserInfo;
import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.google.configuration.GoogleOAuth2ProviderAutoConfiguration;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Tokeninfo;
import com.google.api.services.oauth2.model.Userinfoplus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GoogleOAuth2ProviderAutoConfiguration.class)
public class GoogleOAuth2ProviderTest {

    @Autowired
    private IOAuth2Provider googleOAuth2Provider;

    @Autowired
    private GoogleOAuth2ProviderConfigurationProperties properties;

    @MockBean
    private Oauth2 oauth2;

    @MockBean
    private Oauth2.Tokeninfo tokenInfo;

    @MockBean
    private Oauth2.Userinfo userInfo;

    @MockBean
    private Oauth2.Userinfo.V2 v2;

    @MockBean
    private Oauth2.Userinfo.V2.Me me;

    @MockBean
    private Oauth2.Userinfo.V2.Me.Get get;

    @Test
    public void testGetId() {
        var result = this.googleOAuth2Provider.getId();
        assertThat(result).isEqualTo(GoogleOAuth2Provider.OAUTH2_PROVIDER_ID);
    }

    @Test
    public void testSupportsWithSupportedOAuth2ProviderId() {
        var result = this.googleOAuth2Provider.supports(GoogleOAuth2Provider.OAUTH2_PROVIDER_ID);
        assertThat(result).isTrue();
    }

    @Test
    public void testSupportsWithNonSupportedOAuth2ProviderId() {
        var result = this.googleOAuth2Provider.supports("Facebook");
        assertThat(result).isFalse();
    }

    @Test
    public void testLoadUserInfoWithInvalidAccessToken() throws IOException {
        var accessToken = "access-token";

        when(this.oauth2.tokeninfo()).thenReturn(this.tokenInfo);
        when(this.tokenInfo.setAccessToken(accessToken)).thenReturn(this.tokenInfo);
        when(this.tokenInfo.execute()).thenThrow(new IOException("Invalid token"));

        var result = this.googleOAuth2Provider.loadUserInfo(accessToken);

        StepVerifier.create(result)
                .expectComplete()
                .verify();

        verify(this.oauth2).tokeninfo();
        verify(this.tokenInfo).setAccessToken(accessToken);
        verify(this.tokenInfo).execute();
    }

    @Test
    public void testLoadUserInfoWithInvalidAudience() throws IOException {
        var accessToken = "access-token";
        var tokenInfo = new Tokeninfo()
                .setAudience("unknown");

        when(this.oauth2.tokeninfo()).thenReturn(this.tokenInfo);
        when(this.tokenInfo.setAccessToken(accessToken)).thenReturn(this.tokenInfo);
        when(this.tokenInfo.execute()).thenReturn(tokenInfo);

        var result = this.googleOAuth2Provider.loadUserInfo(accessToken);

        StepVerifier.create(result)
                .expectComplete()
                .verify();

        verify(this.oauth2).tokeninfo();
        verify(this.tokenInfo).setAccessToken(accessToken);
        verify(this.tokenInfo).execute();
    }

    @Test
    public void testLoadUserInfoWithFailingMeRequest() throws IOException {
        var accessToken = "access-token";
        var tokenInfo = new Tokeninfo()
                .setAudience(this.properties.getClientId());

        when(this.oauth2.tokeninfo()).thenReturn(this.tokenInfo);
        when(this.tokenInfo.setAccessToken(accessToken)).thenReturn(this.tokenInfo);
        when(this.tokenInfo.execute()).thenReturn(tokenInfo);
        when(this.oauth2.userinfo()).thenReturn(this.userInfo);
        when(this.userInfo.v2()).thenReturn(this.v2);
        when(this.v2.me()).thenReturn(this.me);
        when(this.me.get()).thenReturn(this.get);
        when(this.get.setOauthToken(accessToken)).thenReturn(this.get);
        when(this.get.execute()).thenThrow(new IOException("Failing request"));

        var result = this.googleOAuth2Provider.loadUserInfo(accessToken);

        StepVerifier.create(result)
                .expectComplete()
                .verify();

        verify(this.oauth2).tokeninfo();
        verify(this.tokenInfo).setAccessToken(accessToken);
        verify(this.tokenInfo).execute();
        verify(this.oauth2).userinfo();
        verify(this.userInfo).v2();
        verify(this.v2).me();
        verify(this.me).get();
        verify(this.get).setOauthToken(accessToken);
        verify(this.get).execute();
    }

    @Test
    public void testLoadUserInfoWithSuccessFulMeRequest() throws IOException {
        var accessToken = "access-token";
        var tokenInfo = new Tokeninfo()
                .setAudience(this.properties.getClientId());
        var userInfoPlus = new Userinfoplus()
                .setId("id")
                .setEmail("email")
                .setName("name")
                .setGender("gender")
                .setLink("link")
                .setPicture("picture");

        when(this.oauth2.tokeninfo()).thenReturn(this.tokenInfo);
        when(this.tokenInfo.setAccessToken(accessToken)).thenReturn(this.tokenInfo);
        when(this.tokenInfo.execute()).thenReturn(tokenInfo);
        when(this.oauth2.userinfo()).thenReturn(this.userInfo);
        when(this.userInfo.v2()).thenReturn(this.v2);
        when(this.v2.me()).thenReturn(this.me);
        when(this.me.get()).thenReturn(this.get);
        when(this.get.setOauthToken(accessToken)).thenReturn(this.get);
        when(this.get.execute()).thenReturn(userInfoPlus);

        var result = this.googleOAuth2Provider.loadUserInfo(accessToken);
        var expected = UserInfo.builder()
                .id(userInfoPlus.getId())
                .email(userInfoPlus.getEmail())
                .name(userInfoPlus.getName())
                .gender(userInfoPlus.getGender())
                .link(URI.create(userInfoPlus.getLink()))
                .picture(URI.create(userInfoPlus.getPicture()))
                .build();

        StepVerifier.create(result)
                .expectNext(expected)
                .expectComplete()
                .verify();

        verify(this.oauth2).tokeninfo();
        verify(this.tokenInfo).setAccessToken(accessToken);
        verify(this.tokenInfo).execute();
        verify(this.oauth2).userinfo();
        verify(this.userInfo).v2();
        verify(this.v2).me();
        verify(this.me).get();
        verify(this.get).setOauthToken(accessToken);
        verify(this.get).execute();
    }

}
