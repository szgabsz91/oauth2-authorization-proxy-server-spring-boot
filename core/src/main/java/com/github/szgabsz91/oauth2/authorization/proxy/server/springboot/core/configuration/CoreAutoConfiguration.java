package com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.core.configuration;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * AutoConfiguration class for the core module.
 *
 * @author szgabsz91
 */
@AutoConfiguration
@Import({
        SecurityConfiguration.class,
        WebFluxConfiguration.class
})
public class CoreAutoConfiguration {

}
