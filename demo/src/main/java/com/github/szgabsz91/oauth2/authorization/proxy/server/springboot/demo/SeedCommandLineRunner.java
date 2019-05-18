package com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.demo;

import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.demo.entities.Item;
import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.demo.repositories.ItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Custom {@link CommandLineRunner} implementation that creates a couple of {@link Item} objects in the database.
 *
 * @author szgabsz91
 */
@Component
public class SeedCommandLineRunner implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(SeedCommandLineRunner.class);

    private final ItemRepository itemRepository;

    /**
     * Constructor that sets the {@link ItemRepository}.
     * @param itemRepository the {@link ItemRepository}
     */
    public SeedCommandLineRunner(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    /**
     * Creates a couple of {@link Item} objects in the database.
     * @param args command-line arguments
     */
    @Override
    public void run(String... args) {
        this.itemRepository.deleteAll()
                .thenMany(this.itemRepository.saveAll(List.of(
                        new Item("Item 1", "mail1@nonexistent.com"),
                        new Item("Item 2", "mail1@nonexistent.com"),
                        new Item("Item 3", "mail2@nonexistent.com")
                )))
                .subscribe(item -> LOGGER.info("Saved item: {}", item));
    }

}
