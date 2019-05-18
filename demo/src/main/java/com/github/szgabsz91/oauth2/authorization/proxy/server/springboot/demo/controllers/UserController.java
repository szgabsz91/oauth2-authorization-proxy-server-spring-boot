package com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.demo.controllers;

import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.api.model.UserInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Spring MVC REST controller for retrieving the authenticated user's {@link UserInfo}.
 *
 * @author szgabsz91
 */
@RestController
@RequestMapping("users")
public class UserController {

    /**
     * Returns the currently authenticated user's {@link UserInfo}.
     * @param userInfoMono the currently authenticated user's {@link UserInfo} inside a {@link Mono}
     * @return the currently authenticated user's {@link UserInfo}
     */
    @GetMapping("me")
    public Mono<UserInfo> me(Mono<UserInfo> userInfoMono) {
        return userInfoMono;
    }

}
