package com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.facebook.configuration;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FacebookOAuth2ProviderConfigurationPropertiesTest {

    @Test
    public void testGetSecretWithNullSecret() {
        FacebookOAuth2ProviderConfigurationProperties properties = new FacebookOAuth2ProviderConfigurationProperties();
        var result = properties.getAppSecret();
        assertThat(result).isEqualTo("");
    }

    @Test
    public void testGetSecretWithNonNullSecret() {
        var expected = "secret";
        FacebookOAuth2ProviderConfigurationProperties properties = new FacebookOAuth2ProviderConfigurationProperties();
        properties.setAppSecret(expected);
        var result = properties.getAppSecret();
        assertThat(result).isEqualTo(expected);
    }

}
