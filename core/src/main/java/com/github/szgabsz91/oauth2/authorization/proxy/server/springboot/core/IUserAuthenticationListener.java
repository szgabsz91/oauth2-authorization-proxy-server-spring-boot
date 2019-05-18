package com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.core;

import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.api.model.UserInfo;

/**
 * Listener interface whose implementers get notified in case of authentication success and failure events.
 *
 * @author szgabsz91
 */
public interface IUserAuthenticationListener {

    /**
     * Invoked if the authentication was successful, passing the created {@link UserInfo}.
     * @param userInfo the {@link UserInfo} of the authenticated user
     */
    void authenticationSuccessful(UserInfo userInfo);

    /**
     * Invoked if the authentication failed, passing the requested OAuth2 provider identifier and the access token.
     * @param oauth2ProviderId the identifier of the requested OAuth2 provider
     * @param accessToken the access token
     */
    void authenticationFailed(String oauth2ProviderId, String accessToken);

}
