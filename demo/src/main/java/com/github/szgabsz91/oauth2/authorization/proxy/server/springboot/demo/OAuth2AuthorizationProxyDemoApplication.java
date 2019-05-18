package com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

/**
 * Main class of the demo application.
 *
 * @author szgabsz91
 */
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class OAuth2AuthorizationProxyDemoApplication {

	/**
	 * Main method of the demo application.
	 * @param args the command-line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(OAuth2AuthorizationProxyDemoApplication.class, args);
	}

}
