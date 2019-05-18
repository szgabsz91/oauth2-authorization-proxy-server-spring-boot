package com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.core;

import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.api.model.UserInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Custom {@link Authentication} implementation that stores a {@link UserInfo} and
 * the collection of {@link GrantedAuthority} objects.
 *
 * @author szgabsz91
 */
public class OAuth2Authentication implements Authentication {

    private final UserInfo userInfo;
    private final Collection<? extends GrantedAuthority> authorities;

    /**
     * Constructor that sets the {@link UserInfo} and the collection of {@link GrantedAuthority} objects.
     * @param userInfo the {@link UserInfo}
     * @param authorities the collection of {@link GrantedAuthority} objects
     */
    public OAuth2Authentication(UserInfo userInfo, Collection<? extends GrantedAuthority> authorities) {
        this.userInfo = userInfo;
        this.authorities = authorities;
    }

    /**
     * Returns the {@link UserInfo}.
     * @return the {@link UserInfo}
     */
    @Override
    public UserInfo getPrincipal() {
        return userInfo;
    }

    /**
     * Returns the e-mail address of the authenticated user.
     * @return the e-mail address of the authenticated user
     */
    @Override
    public String getName() {
        return userInfo.getEmail();
    }

    /**
     * Returns the collection of {@link GrantedAuthority} objects.
     * @return the collection of {@link GrantedAuthority} objects
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * Returns <code>null</code>.
     * @return <code>null</code>
     */
    @Override
    public Object getCredentials() {
        return null;
    }

    /**
     * Returns <code>null</code>.
     * @return <code>null</code>
     */
    @Override
    public Object getDetails() {
        return null;
    }

    /**
     * Returns <code>true</code>.
     * @return <code>true</code>
     */
    @Override
    public boolean isAuthenticated() {
        return true;
    }

    /**
     * Throws a new {@link UnsupportedOperationException}.
     * @param authenticated the new authenticated flag
     * @throws IllegalArgumentException never
     */
    @Override
    public void setAuthenticated(boolean authenticated) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported");
    }

}
