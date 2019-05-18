package com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.google.configuration;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GoogleOAuth2ProviderConfigurationPropertiesTest {

    @Test
    public void testGetSecretWithNullSecret() {
        GoogleOAuth2ProviderConfigurationProperties properties = new GoogleOAuth2ProviderConfigurationProperties();
        var result = properties.getClientSecret();
        assertThat(result).isEqualTo("");
    }

    @Test
    public void testGetSecretWithNonNullSecret() {
        var expected = "secret";
        GoogleOAuth2ProviderConfigurationProperties properties = new GoogleOAuth2ProviderConfigurationProperties();
        properties.setClientSecret(expected);
        var result = properties.getClientSecret();
        assertThat(result).isEqualTo(expected);
    }

}
