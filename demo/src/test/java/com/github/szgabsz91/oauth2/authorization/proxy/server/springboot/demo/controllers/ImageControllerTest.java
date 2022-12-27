package com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.demo.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {
        ImageController.class
})
public class ImageControllerTest {

    @Autowired
    private ImageController imageController;

    @Test
    public void testGetImage() throws IOException {
        var responseEntity = this.imageController.getImage();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getHeaders()).containsExactly(Map.entry("Content-Type", List.of("image/png")));
        assertThat(responseEntity.getBody().contentLength()).isGreaterThan(0L);
    }

}
