package com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.api.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.Serializable;
import java.net.URI;

/**
 * Model class that contains all the information about the authenticated user.
 *
 * @author szgabsz91
 */
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserInfo implements Serializable {

    private String id;

    @NonNull
    private String email;

    @NonNull
    private String name;

    private String gender;

    private URI link;

    @NonNull
    private URI picture;

}
