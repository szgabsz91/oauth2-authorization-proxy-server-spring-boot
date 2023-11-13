# OAuth2 Authorization Proxy Server Spring Boot - Starter

[![Build Status](https://img.shields.io/circleci/project/github/szgabsz91/oauth2-authorization-proxy-server-spring-boot/master.svg)](https://circleci.com/gh/szgabsz91/workflows/oauth2-authorization-proxy-server-spring-boot)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.szgabsz91/oauth2-authorization-proxy-server-spring-boot-starter/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.szgabsz91/oauth2-authorization-proxy-server-spring-boot-starter)
[![License](https://img.shields.io/github/license/szgabsz91/oauth2-authorization-proxy-server-spring-boot.svg)](https://github.com/szgabsz91/oauth2-authorization-proxy-server-spring-boot/blob/master/LICENSE)

This starter project depends on all the other subprojects so that Spring Boot projects can depend on only this one
and all the necessary beans are created automatically.

All the other subprojects contain a `META-INF/spring.factories` file that contains the appropriate configuration classes
under the `org.springframework.boot.autoconfigure.EnableAutoConfiguration` key. Spring Boot reads these classes and
uses them to create the Spring Beans defined by the configuration classes.

Note that some beans are conditional, for example the OAuth2 providers are only created if their related configuration
properties are present.
