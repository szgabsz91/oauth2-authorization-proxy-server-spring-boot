package com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.demo.controllers;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Spring MVC REST controller for retrieving an image.
 *
 * @author szgabsz91
 */
@RestController
@RequestMapping("image")
public class ImageController {

    /**
     * Returns an image from the classpath.
     * @return an image from the classpath
     */
    @GetMapping
    public ResponseEntity<Resource> getImage() {
        var headers = new LinkedMultiValueMap<String, String>();
        headers.add("Content-Type", "image/png");
        var inputStream = getClass().getResourceAsStream("/spring-boot-logo.png");
        var  inputStreamResource = new InputStreamResource(inputStream);
        return new ResponseEntity<>(inputStreamResource, headers, HttpStatus.OK);
    }

}
