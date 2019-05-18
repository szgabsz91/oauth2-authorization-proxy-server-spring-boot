package com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.core.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * AutoConfiguration class for the core module.
 *
 * @author szgabsz91
 */
@Configuration
@Import({
        SecurityConfiguration.class,
        WebFluxConfiguration.class
})
public class CoreAutoConfiguration {

}
