package com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.demo.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * Configuration class for Spring WebFlux.
 *
 * @author szgabsz91
 */
@Configuration
public class WebFluxConfiguration {

    /**
     * Creates a new {@link RouterFunction} that forwards the root <i>/</i> path to <i>index.html</i>.
     * @param indexHtml the {@link Resource} of <i>index.html</i>
     * @return the {@link RouterFunction}
     */
    @Bean
    public RouterFunction<ServerResponse> indexRouter(@Value("classpath:/static/index.html") final Resource indexHtml) {
        return route(
                GET("/"),
                request ->
                        ok()
                        .contentType(MediaType.TEXT_HTML)
                        .bodyValue(indexHtml)
        );
    }

}
