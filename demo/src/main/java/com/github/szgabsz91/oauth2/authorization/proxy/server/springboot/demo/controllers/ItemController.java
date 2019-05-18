package com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.demo.controllers;

import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.demo.entities.Item;
import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.demo.repositories.ItemRepository;
import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.api.model.UserInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring MVC REST controller for managing {@link Item} entities.
 *
 * @author szgabsz91
 */
@RestController
@RequestMapping("items")
public class ItemController {

    private final ItemRepository itemRepository;

    /**
     * Constructor that sets the {@link ItemRepository}.
     * @param itemRepository the {@link ItemRepository}
     */
    public ItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    /**
     * Returns the {@link Flux} of all the {@link Item} entities from the database.
     * @return the {@link Flux} of all the {@link Item} entities
     */
    @GetMapping("all")
    public Flux<Item> findAll() {
        return this.itemRepository.findAll();
    }

    /**
     * Returns the {@link Flux} of those {@link Item} entities from the database
     * that are visible to the currently authenticated user.
     *
     * @param userInfoMono the currently authenticated user's {@link UserInfo} inside a {@link Mono}
     * @return the {@link Flux} of those {@link Item} entities that are visible to the currently authenticated user
     */
    @GetMapping("visible")
    public Flux<Item> findByUser(Mono<UserInfo> userInfoMono) {
        return userInfoMono
                .map(UserInfo::getEmail)
                .flatMapMany(itemRepository::findByEmail);
    }

    /**
     * Saves the given {@link Item} entity with the e-mail address of the currently authenticated user.
     * @param itemMono the {@link Mono} of the {@link Item} entity to save
     * @param userInfoMono the {@link Mono} of the currently authenticated user's {@link UserInfo}
     * @return the {@link Mono} of the saved {@link Item} entity
     */
    @PostMapping
    public Mono<Item> save(@RequestBody Mono<Item> itemMono, Mono<UserInfo> userInfoMono) {
        return userInfoMono
                .zipWith(itemMono)
                .map(tuple -> {
                    var userInfo = tuple.getT1();
                    var item = tuple.getT2();
                    item.setEmail(userInfo.getEmail());
                    return item;
                })
                .flatMap(this.itemRepository::save);
    }

}
