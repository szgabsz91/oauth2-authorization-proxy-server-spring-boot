package com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.demo.repositories;

import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.demo.entities.Item;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data repository to manage {@link Item} entities.
 *
 * @author szgabsz91
 */
@Repository
public interface ItemRepository extends ReactiveCrudRepository<Item, String> {

    /**
     * Returns a {@link Flux} of {@link Item}s that are associated with the given e-mail address.
     * @param email the e-mail address of the required {@link Item}s
     * @return the {@link Flux} of {@link Item}s
     */
    Flux<Item> findByEmail(String email);

}
