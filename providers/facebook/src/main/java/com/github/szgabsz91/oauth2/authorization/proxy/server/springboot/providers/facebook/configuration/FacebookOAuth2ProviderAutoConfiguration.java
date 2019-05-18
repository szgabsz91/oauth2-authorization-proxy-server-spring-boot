package com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.facebook.configuration;

import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.facebook.FacebookOAuth2Provider;
import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.facebook.glue.FacebookFactory;
import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.api.IOAuth2Provider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AutoConfiguration class for the {@link FacebookOAuth2Provider}.
 *
 * @author szgabsz91
 */
@Configuration
@EnableConfigurationProperties(FacebookOAuth2ProviderConfigurationProperties.class)
public class FacebookOAuth2ProviderAutoConfiguration {

    /**
     * Creates the {@link FacebookOAuth2Provider}, but only if the
     * <code>oauth2.authorization.proxy.server.springboot.providers.facebook.appId</code> property is set.
     *
     * @param facebookOAuth2ProviderConfigurationProperties the {@link FacebookOAuth2ProviderConfigurationProperties}
     * @return the created {@link FacebookOAuth2Provider}
     */
    @Bean
    @ConditionalOnProperty("oauth2.authorization.proxy.server.springboot.providers.facebook.appId")
    public IOAuth2Provider facebookOAuth2Provider(
            FacebookOAuth2ProviderConfigurationProperties facebookOAuth2ProviderConfigurationProperties) {
        return new FacebookOAuth2Provider(facebookFactory(), facebookOAuth2ProviderConfigurationProperties);
    }

    /**
     * Returns the {@link FacebookFactory}, but only if the
     * <code>oauth2.authorization.proxy.server.springboot.providers.facebook.appId</code> property is set.
     *
     * @return the {@link FacebookFactory}
     */
    @Bean
    @ConditionalOnProperty("oauth2.authorization.proxy.server.springboot.providers.facebook.appId")
    public FacebookFactory facebookFactory() {
        return new FacebookFactory();
    }

}
