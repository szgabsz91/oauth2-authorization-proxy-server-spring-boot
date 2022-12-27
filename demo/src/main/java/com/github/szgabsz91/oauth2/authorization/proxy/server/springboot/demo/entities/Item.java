package com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.demo.entities;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Spring Data entity to store items that contain an id, a description and an e-mail address.
 *
 * @author szgabsz91
 */
@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Item {

    @Id
    private String id;

    @NotNull
    @NonNull
    private String description;

    @NotNull
    @NonNull
    private String email;

}
