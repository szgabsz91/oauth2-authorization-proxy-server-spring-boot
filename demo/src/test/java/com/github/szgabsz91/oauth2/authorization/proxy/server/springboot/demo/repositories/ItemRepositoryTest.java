package com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.demo.repositories;

import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.demo.entities.Item;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.test.StepVerifier;

@DataMongoTest
@RunWith(SpringRunner.class)
public class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void testFindByEmail() {
        var email = "email";
        var expected = new Item("1", "dummy", email);
        this.itemRepository.save(expected).block();

        var result = this.itemRepository.findByEmail(email);

        StepVerifier.create(result)
                .expectNext(expected)
                .expectComplete()
                .verify();
    }

}
