package com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.core.configuration;

import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.core.UserInfoArgumentResolver;
import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.api.model.UserInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;

/**
 * Configuration for Spring WebFlux that creates a custom {@link HandlerMethodArgumentResolver} to resolve
 * {@link UserInfo} parameters
 * in Spring MVC controller methods.
 *
 * @author szgabsz91
 */
@Configuration
public class WebFluxConfiguration {

    /**
     * Creates a new {@link UserInfoArgumentResolver}.
     * @return the {@link UserInfoArgumentResolver}
     */
    @Bean
    public HandlerMethodArgumentResolver userInfoArgumentResolver() {
        return new UserInfoArgumentResolver();
    }

}
