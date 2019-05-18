package com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.facebook;

import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.api.IOAuth2Provider;
import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.api.model.UserInfo;
import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.facebook.configuration.FacebookOAuth2ProviderConfigurationProperties;
import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.facebook.glue.FacebookFactory;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.Reading;
import facebook4j.auth.AccessToken;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.net.URISyntaxException;

/**
 * {@link IOAuth2Provider} implementation for the Facebook OAuth2 provider.
 *
 * @author szgabsz91
 */
@Slf4j
public class FacebookOAuth2Provider implements IOAuth2Provider {

    /**
     * Identifier of the Facebook OAuth2 provider.
     */
    public static final String OAUTH2_PROVIDER_ID = "Facebook";

    private final FacebookFactory facebookFactory;
    private final FacebookOAuth2ProviderConfigurationProperties properties;

    /**
     * Constructor that sets the required services and properties.
     * @param facebookFactory the {@link FacebookFactory}
     * @param properties the {@link FacebookOAuth2ProviderConfigurationProperties}
     */
    public FacebookOAuth2Provider(
            FacebookFactory facebookFactory,
            FacebookOAuth2ProviderConfigurationProperties properties) {
        this.facebookFactory = facebookFactory;
        this.properties = properties;
    }

    /**
     * Returns <i>"Facebook"</i>.
     * @return <i>"Facebook"</i>
     */
    @Override
    public String getId() {
        return OAUTH2_PROVIDER_ID;
    }

    /**
     * Returns if the given OAuth2 provider identifier is supported by this {@link IOAuth2Provider}.
     * @param oauth2ProviderId the OAuth2 provider identifier
     * @return <code>true</code> if the given OAuth2 provider identifier is
     *         {@link FacebookOAuth2Provider#OAUTH2_PROVIDER_ID}, <code>false</code> otherwise
     */
    @Override
    public boolean supports(String oauth2ProviderId) {
        return OAUTH2_PROVIDER_ID.equals(oauth2ProviderId);
    }

    /**
     * Loads the {@link UserInfo} from the given access token.
     * @param accessTokenString the access token from the <i>Authorization</i> header
     * @return the {@link UserInfo}
     */
    @Override
    public Mono<UserInfo> loadUserInfo(String accessTokenString) {
        log.debug("Checking {} access token", OAUTH2_PROVIDER_ID);

        var facebook = this.facebookFactory.getInstance();
        facebook.setOAuthAppId(this.properties.getAppId(), this.properties.getAppSecret());

        try {
            var accessToken = facebook.getOAuthAccessTokenInfo(accessTokenString);
            var userInfo = loadUserInfo(facebook, accessToken);
            return Mono.just(userInfo);
        }
        catch (FacebookException e) {
            return Mono.empty();
        }
    }

    private static UserInfo loadUserInfo(Facebook facebook, AccessToken accessToken) throws FacebookException {
        facebook.setOAuthAccessToken(accessToken);
        var reading = new Reading();
        reading.fields("id", "email", "name", "gender", "link", "picture");
        var me = facebook.getMe(reading);

        try {
            return UserInfo.builder()
                    .id(me.getId())
                    .email(me.getEmail())
                    .name(me.getName())
                    .gender(me.getGender())
                    .link(me.getLink().toURI())
                    .picture(me.getPicture().getURL().toURI())
                    .build();
        }
        catch (URISyntaxException e) {
            throw new FacebookException("Cannot retrieve profile or profile picture URI", e);
        }
    }

}
