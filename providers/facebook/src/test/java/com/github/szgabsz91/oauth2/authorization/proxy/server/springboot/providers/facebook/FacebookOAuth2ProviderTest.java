package com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.facebook;

import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.facebook.configuration.FacebookOAuth2ProviderAutoConfiguration;
import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.facebook.glue.FacebookFactory;
import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.api.IOAuth2Provider;
import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.api.model.UserInfo;
import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.facebook.configuration.FacebookOAuth2ProviderConfigurationProperties;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.Picture;
import facebook4j.Reading;
import facebook4j.User;
import facebook4j.auth.AccessToken;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = FacebookOAuth2ProviderAutoConfiguration.class)
public class FacebookOAuth2ProviderTest {

    @Autowired
    private IOAuth2Provider facebookOAuth2Provider;

    @Autowired
    private FacebookOAuth2ProviderConfigurationProperties properties;

    @MockBean
    private FacebookFactory facebookFactory;

    @MockBean
    private Facebook facebook;

    @MockBean
    private User user;

    @MockBean
    private Picture picture;

    @Test
    public void testGetId() {
        var result = this.facebookOAuth2Provider.getId();
        assertThat(result).isEqualTo(FacebookOAuth2Provider.OAUTH2_PROVIDER_ID);
    }

    @Test
    public void testSupportsWithSupportedOAuth2ProviderId() {
        var result = this.facebookOAuth2Provider.supports(FacebookOAuth2Provider.OAUTH2_PROVIDER_ID);
        assertThat(result).isTrue();
    }

    @Test
    public void testSupportsWithNonSupportedOAuth2ProviderId() {
        var result = this.facebookOAuth2Provider.supports("Google");
        assertThat(result).isFalse();
    }

    @Test
    public void testLoadUserInfoWithInvalidAccessToken() throws FacebookException {
        var accessTokenString = "access-token";

        when(this.facebookFactory.getInstance())
                .thenReturn(this.facebook);
        when(this.facebook.getOAuthAccessTokenInfo(accessTokenString))
                .thenThrow(new FacebookException("Invalid access token"));

        var result = this.facebookOAuth2Provider.loadUserInfo(accessTokenString);

        StepVerifier.create(result)
                .expectComplete()
                .verify();
    }

    @Test
    public void testLoadUserInfoWithFailingMeRequest() throws FacebookException {
        var accessTokenString = "access-token";
        var accessToken = new AccessToken(accessTokenString);
        var reading = new Reading();
        reading.fields("id", "email", "name", "gender", "link", "picture");

        when(this.facebookFactory.getInstance())
                .thenReturn(this.facebook);
        when(this.facebook.getOAuthAccessTokenInfo(accessTokenString))
                .thenReturn(accessToken);
        when(this.facebook.getMe(reading))
                .thenThrow(new FacebookException("Cannot get user info"));

        var result = this.facebookOAuth2Provider.loadUserInfo(accessTokenString);

        StepVerifier.create(result)
                .expectComplete()
                .verify();
    }

    @Test
    public void testLoadUserInfoWithSuccessFulMeRequest() throws FacebookException, MalformedURLException, URISyntaxException {
        var accessTokenString = "access-token";
        var accessToken = new AccessToken(accessTokenString);
        var reading = new Reading();
        reading.fields("id", "email", "name", "gender", "link", "picture");
        var id = "id";
        var email = "email";
        var name = "name";
        var gender = "gender";
        var link = new URL("https://google.com");
        var pictureUrl = new URL("https://microsoft.com");

        when(this.facebookFactory.getInstance())
                .thenReturn(this.facebook);
        when(this.facebook.getOAuthAccessTokenInfo(accessTokenString))
                .thenReturn(accessToken);
        when(this.facebook.getMe(reading))
                .thenReturn(this.user);
        when(this.user.getId()).thenReturn(id);
        when(this.user.getEmail()).thenReturn(email);
        when(this.user.getName()).thenReturn(name);
        when(this.user.getGender()).thenReturn(gender);
        when(this.user.getLink()).thenReturn(link);
        when(this.user.getPicture()).thenReturn(this.picture);
        when(this.picture.getURL()).thenReturn(pictureUrl);

        var expected = UserInfo.builder()
                .id(id)
                .email(email)
                .name(name)
                .gender(gender)
                .link(link.toURI())
                .picture(picture.getURL().toURI())
                .build();
        var result = this.facebookOAuth2Provider.loadUserInfo(accessTokenString);

        StepVerifier.create(result)
                .expectNext(expected)
                .expectComplete()
                .verify();

        verify(this.facebookFactory).getInstance();
        verify(this.facebook).setOAuthAppId(properties.getAppId(), properties.getAppSecret());
        verify(this.facebook).setOAuthAccessToken(accessToken);
        verify(this.facebook).getMe(reading);
    }

}
