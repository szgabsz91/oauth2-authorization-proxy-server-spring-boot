package com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.api;

import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.api.model.UserInfo;
import reactor.core.publisher.Mono;

/**
 * Interface that must be implemented by the supported OAuth2 providers.
 *
 * <p>These implementations can determine if they support a given OAuth2 provider identifier, and they can construct
 * the {@link UserInfo} object from a given access token.</p>
 *
 * <p>If the user cannot be authenticated with the 3rdparty OAuth2 provider using the provided access token,
 * an empty {@link Mono} is returned.</p>
 *
 * @author szgabsz91
 */
public interface IOAuth2Provider {

    /**
     * Returns the identifier of this {@link IOAuth2Provider}.
     * @return the identifier of this {@link IOAuth2Provider}
     */
    String getId();

    /**
     * Returns if the OAuth2 provider supports the given OAuth2 provider identifier.
     * @param oauth2ProviderId the OAuth2 provider identifier
     * @return <code>true</code> if the given OAuth2 provider identifier is supported, <code>false</code> otherwise
     */
    boolean supports(String oauth2ProviderId);

    /**
     * Constructs the {@link UserInfo} object from the given access token.
     * @param accessToken the access token
     * @return the constructed {@link UserInfo} object inside a {@link Mono}, or an empty {@link Mono}
     *         in case of an authentication failure
     */
    Mono<UserInfo> loadUserInfo(String accessToken);

}
