package com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.demo.controllers;

import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.demo.entities.Item;
import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.demo.repositories.ItemRepository;
import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.api.model.UserInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.net.URI;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        ItemController.class,
        ItemRepository.class
})
public class ItemControllerTest {

    @Autowired
    private ItemController itemController;

    @MockBean
    private ItemRepository itemRepository;

    @Test
    public void testFindAll() {
        var expected = Flux.fromIterable(List.of(new Item("1", "description", "email")));
        when(this.itemRepository.findAll()).thenReturn(expected);

        var result = this.itemController.findAll();
        assertThat(result).isEqualTo(expected);

        verify(this.itemRepository).findAll();
    }

    @Test
    public void testFindByEmail() {
        var email = "email";
        var userInfo = UserInfo.builder()
                .id("id")
                .email("email")
                .name("name")
                .gender("gender")
                .link(URI.create("https://google.com"))
                .picture(URI.create("https://facebook.com"))
                .build();
        var expected = List.of(new Item("1", "description", email));
        when(this.itemRepository.findByEmail(email)).thenReturn(Flux.fromIterable(expected));

        var result = this.itemController.findByUser(Mono.just(userInfo));

        StepVerifier.create(result)
                .expectNext(expected.get(0))
                .expectComplete()
                .verify();

        verify(this.itemRepository).findByEmail(email);
    }

    @Test
    public void testSave() {
        var item = new Item(null, "description", "other");
        var userInfo = UserInfo.builder()
                .id("id")
                .email("email")
                .name("name")
                .gender("gender")
                .link(URI.create("https://google.com"))
                .picture(URI.create("https://facebook.com"))
                .build();
        var saved = new Item(null, item.getDescription(), userInfo.getEmail());
        var expected = new Item("1", item.getDescription(), userInfo.getEmail());

        when(this.itemRepository.save(saved)).thenReturn(Mono.just(expected));

        var result = this.itemController.save(Mono.just(item), Mono.just(userInfo));

        StepVerifier.create(result)
                .expectNext(expected)
                .expectComplete()
                .verify();

        verify(this.itemRepository).save(new Item(null, item.getDescription(), userInfo.getEmail()));
    }

}
