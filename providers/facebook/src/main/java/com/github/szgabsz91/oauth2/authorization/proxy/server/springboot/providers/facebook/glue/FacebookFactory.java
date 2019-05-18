package com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.facebook.glue;

import facebook4j.Facebook;

/**
 * Glue class around {@link facebook4j.FacebookFactory} so that it can be mocked in tests.
 *
 * <p>Note that {@link facebook4j.FacebookFactory} is a final class.</p>
 *
 * @author szgabsz91
 */
public class FacebookFactory {

    private final facebook4j.FacebookFactory facebookFactory;

    /**
     * Constructor that initializes the internal {@link facebook4j.FacebookFactory}.
     */
    public FacebookFactory() {
        this.facebookFactory = new facebook4j.FacebookFactory();
    }

    /**
     * Returns the {@link Facebook} instance.
     * @return the {@link Facebook} instance
     */
    public Facebook getInstance() {
        return this.facebookFactory.getInstance();
    }

}
