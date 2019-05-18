package com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.core.configuration;

import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.core.IUserAuthenticationListener;
import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.core.OAuth2ServerSecurityContextRepository;
import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.core.DummyOAuth2ServerSecurityContextRepository;
import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.api.IOAuth2Provider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;

/**
 * Configuration for Spring Security that creates the Spring Security filter chain for authentication and authorization.
 *
 * @author szgabsz91
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(OAuth2AuthorizationProxyConfigurationProperties.class)
public class SecurityConfiguration implements ApplicationContextAware {

    @Autowired(required = false)
    private final OAuth2AuthorizationProxyConfigurer configurer = new OAuth2AuthorizationProxyConfigurerAdapter();

    private ApplicationContext applicationContext;

    /**
     * Sets the {@link ApplicationContext} so that we can get the set of {@link IOAuth2Provider}s later.
     * @param applicationContext the {@link ApplicationContext}
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * Creates the {@link SecurityWebFilterChain} by disabling CSRF, basic authentication, form login and logout,
     * setting up permitted URL patterns and authorization.
     *
     * @param httpSecurity the {@link ServerHttpSecurity}
     * @param serverSecurityContextRepository the {@link OAuth2ServerSecurityContextRepository}
     * @return the {@link SecurityWebFilterChain}
     */
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(
            ServerHttpSecurity httpSecurity,
            ServerSecurityContextRepository serverSecurityContextRepository) {
        var filterChainBuilder = httpSecurity
                .csrf().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .logout().disable()
                .securityContextRepository(serverSecurityContextRepository)
                .authorizeExchange();

        var antPatternsForPermitAll = configurer.getAntPatternsForPermitAll();
        if (antPatternsForPermitAll != null) {
            filterChainBuilder
                    .pathMatchers(antPatternsForPermitAll).permitAll();
        }

        var authorizedUserAuthority = configurer.getAuthorizedUserAuthority();
        filterChainBuilder
                    .anyExchange().hasAuthority(authorizedUserAuthority.getAuthority());

        return filterChainBuilder.and().build();
    }

    /**
     * Creates a new {@link DummyOAuth2ServerSecurityContextRepository} if the
     * <i>oauth2.authorization.proxy.dummyUser.email</i> property is set.
     *
     * @param oauth2AuthorizationProxyConfigurationProperties the {@link OAuth2AuthorizationProxyConfigurationProperties}
     * @return the {@link DummyOAuth2ServerSecurityContextRepository}
     */
    @Bean
    @ConditionalOnProperty("oauth2.authorization.proxy.dummyUser.email")
    public ServerSecurityContextRepository dummyServerSecurityContextRepository(OAuth2AuthorizationProxyConfigurationProperties oauth2AuthorizationProxyConfigurationProperties) {
        var userInfo = oauth2AuthorizationProxyConfigurationProperties.getDummyUser();
        log.info("Using dummy security with user {}", userInfo);
        var userAuthenticationListeners = this.applicationContext.getBeansOfType(IUserAuthenticationListener.class).values();
        return new DummyOAuth2ServerSecurityContextRepository(userInfo, userAuthenticationListeners, configurer.getAuthorizedUserAuthority());
    }

    /**
     * Creates a new {@link OAuth2ServerSecurityContextRepository} using the set of {@link IOAuth2Provider}.
     * @return the {@link OAuth2ServerSecurityContextRepository}
     */
    @Bean
    @ConditionalOnMissingBean
    public ServerSecurityContextRepository serverSecurityContextRepository() {
        var oauth2Providers = this.applicationContext.getBeansOfType(IOAuth2Provider.class).values();
        if (oauth2Providers.isEmpty()) {
            throw new IllegalStateException("No OAuth2 provider implementations found in context, you may be missing some application properties!");
        }
        else {
            oauth2Providers.forEach(oauth2Provider -> log.info("OAuth2 provider found for {}", oauth2Provider.getId()));
        }
        var userAuthenticationListeners = this.applicationContext.getBeansOfType(IUserAuthenticationListener.class).values();
        return new OAuth2ServerSecurityContextRepository(oauth2Providers, userAuthenticationListeners, configurer.getAuthorizedUserAuthority());
    }

}
