package com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.demo.security;

import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.core.IUserAuthenticationListener;
import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.api.model.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Custom {@link IUserAuthenticationListener} implementation that logs the authentication events.
 *
 * @author szgabsz91
 */
@Slf4j
@Component
public class UserAuthenticationListener implements IUserAuthenticationListener {

    /**
     * {@inheritDoc}
     */
    @Override
    public void authenticationSuccessful(UserInfo userInfo) {
        log.debug("Authentication successful for user {}", userInfo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void authenticationFailed(String oauth2ProviderId, String accessToken) {
        log.debug("Authentication failed");
    }

}
