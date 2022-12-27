package com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.google.configuration;

import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.google.GoogleOAuth2Provider;
import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.api.IOAuth2Provider;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.oauth2.Oauth2;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * AutoConfiguration class for the {@link GoogleOAuth2Provider}.
 *
 * @author szgabsz91
 */
@AutoConfiguration
@EnableConfigurationProperties(GoogleOAuth2ProviderConfigurationProperties.class)
public class GoogleOAuth2ProviderAutoConfiguration {

    /**
     * Creates the {@link GoogleOAuth2Provider}, but only if the
     * <code>oauth2.authorization.proxy.server.springboot.providers.google.clientId</code> property is set.
     *
     * @param googleOAuth2ProviderConfigurationProperties the {@link GoogleOAuth2ProviderConfigurationProperties}
     * @return the created {@link GoogleOAuth2Provider}
     * @throws GeneralSecurityException if a security error occurs
     * @throws IOException if I/O error occurs
     */
    @Bean
    @ConditionalOnProperty("oauth2.authorization.proxy.server.springboot.providers.google.clientId")
    public IOAuth2Provider googleOAuth2Provider(
            GoogleOAuth2ProviderConfigurationProperties googleOAuth2ProviderConfigurationProperties)
            throws GeneralSecurityException, IOException {
        return new GoogleOAuth2Provider(oauth2(), googleOAuth2ProviderConfigurationProperties);
    }

    /**
     * Creates the {@link Oauth2}, but only if the
     * <code>oauth2.authorization.proxy.server.springboot.providers.google.clientId</code> property is set.
     *
     * @return the {@link Oauth2}
     * @throws GeneralSecurityException if a security error occurs
     * @throws IOException if I/O error occurs
     */
    @Bean
    @ConditionalOnProperty("oauth2.authorization.proxy.server.springboot.providers.google.clientId")
    public Oauth2 oauth2() throws GeneralSecurityException, IOException {
        var httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        var jsonFactory = JacksonFactory.getDefaultInstance();
        var credential = new GoogleCredential();
        return new Oauth2
                .Builder(httpTransport, jsonFactory, credential)
                .build();
    }

}
