package com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.facebook.configuration;

import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.facebook.FacebookOAuth2Provider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for the
 * {@link FacebookOAuth2Provider} class
 * that contains the appId and the optional appSecret.
 *
 * @author szgabsz91
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties("oauth2.authorization.proxy.server.springboot.providers.facebook")
public class FacebookOAuth2ProviderConfigurationProperties {

    private String appId;
    private String appSecret = "";

    /**
     * Returns the Facebook application's appSecret or an empty string if it has not been set by the client application.
     * @return the Facebook application's appSecret or an empty string if it has not been set by the client application
     */
    public String getAppSecret() {
        return appSecret;
    }

}
