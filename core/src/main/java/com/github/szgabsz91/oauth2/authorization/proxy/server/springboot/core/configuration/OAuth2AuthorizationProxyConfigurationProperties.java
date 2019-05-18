package com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.core.configuration;

import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.api.model.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for the OAuth2 Authorization Proxy Spring Boot library.
 *
 * @author szgabsz91
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties("oauth2.authorization.proxy.server.springboot")
public class OAuth2AuthorizationProxyConfigurationProperties {

    private UserInfo dummyUser;

}
