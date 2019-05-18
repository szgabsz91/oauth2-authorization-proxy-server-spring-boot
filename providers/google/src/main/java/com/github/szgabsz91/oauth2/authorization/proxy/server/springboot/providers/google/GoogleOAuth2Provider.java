package com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.google;

import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.api.IOAuth2Provider;
import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.api.model.UserInfo;
import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.google.configuration.GoogleOAuth2ProviderConfigurationProperties;
import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.google.exceptions.GoogleException;
import com.google.api.services.oauth2.Oauth2;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.URI;

/**
 * {@link IOAuth2Provider} implementation for the Google OAuth2 provider.
 *
 * @author szgabsz91
 */
@Slf4j
public class GoogleOAuth2Provider implements IOAuth2Provider {

    /**
     * Identifier of the Google OAuth2 provider.
     */
    public static final String OAUTH2_PROVIDER_ID = "Google";

    private final Oauth2 oauth2;
    private final GoogleOAuth2ProviderConfigurationProperties properties;

    /**
     * Constructor that sets the required services and properties.
     * @param oauth2 the Google OAuth2 flow
     * @param properties the {@link GoogleOAuth2ProviderConfigurationProperties}
     */
    public GoogleOAuth2Provider(Oauth2 oauth2, GoogleOAuth2ProviderConfigurationProperties properties) {
        this.oauth2 = oauth2;
        this.properties = properties;
    }

    /**
     * Returns <i>"Google"</i>.
     * @return <i>"Google"</i>
     */
    @Override
    public String getId() {
        return OAUTH2_PROVIDER_ID;
    }

    /**
     * Returns if the given OAuth2 provider identifier is supported by this {@link IOAuth2Provider}.
     * @param oauth2ProviderId the OAuth2 provider identifier
     * @return <code>true</code> if the given OAuth2 provider identifier is
     *         {@link GoogleOAuth2Provider#OAUTH2_PROVIDER_ID}, <code>false</code> otherwise
     */
    @Override
    public boolean supports(String oauth2ProviderId) {
        return OAUTH2_PROVIDER_ID.equals(oauth2ProviderId);
    }

    /**
     * Loads the {@link UserInfo} from the given access token.
     * @param accessToken the access token from the <i>Authorization</i> header
     * @return the {@link UserInfo}
     */
    @Override
    public Mono<UserInfo> loadUserInfo(String accessToken) {
        log.debug("Checking {} access token", OAUTH2_PROVIDER_ID);

        try {
            this.validateAccessToken(accessToken);
            return this.getUserInfo(accessToken);
        }
        catch (GoogleException e) {
            return Mono.empty();
        }
    }

    private void validateAccessToken(String accessToken) throws GoogleException {
        try {
            var tokenInfo = this.oauth2.tokeninfo()
                    .setAccessToken(accessToken)
                    .execute();

            if (!this.properties.getClientId().equals(tokenInfo.getAudience())) {
                throw new GoogleException("The token is not intended to be used for this application");
            }
        }
        catch (IOException e) {
            throw new GoogleException("The provided access token is invalid", e);
        }
    }

    private Mono<UserInfo> getUserInfo(String accessToken) throws GoogleException {
        try {
            var googleUserInfo = this.oauth2.userinfo()
                    .v2()
                    .me()
                    .get()
                    .setOauthToken(accessToken)
                    .execute();
            var userInfo = UserInfo.builder()
                    .id(googleUserInfo.getId())
                    .email(googleUserInfo.getEmail())
                    .name(googleUserInfo.getName())
                    .gender(googleUserInfo.getGender())
                    .link(googleUserInfo.getLink() == null ? null : URI.create(googleUserInfo.getLink()))
                    .picture(URI.create(googleUserInfo.getPicture()))
                    .build();
            return Mono.just(userInfo);
        }
        catch (IOException e) {
            throw new GoogleException("Cannot get user info", e);
        }
    }

}
