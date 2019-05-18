package com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.google.configuration;

import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.google.GoogleOAuth2Provider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for the
 * {@link GoogleOAuth2Provider} class
 * that contains the clientId and the optional clientSecret.
 *
 * @author szgabsz91
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties("oauth2.authorization.proxy.server.springboot.providers.google")
public class GoogleOAuth2ProviderConfigurationProperties {

    private String clientId;
    private String clientSecret = "";

    /**
     * Returns the Google application's secret or an empty string if it has not been set by the client application.
     * @return the Google application's secret or an empty string if it has not been set by the client application
     */
    public String getClientSecret() {
        return clientSecret;
    }

}
