package com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.google.exceptions;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GoogleExceptionTest {

    @Test
    public void testConstructorWithMessage() {
        var message = "message";
        var exception = new GoogleException(message);
        assertThat(exception).hasMessage(message);
    }

    @Test
    public void testConstructorWithMessageAndCause() {
        var message = "message";
        var cause = new IllegalArgumentException();
        var exception = new GoogleException(message, cause);
        assertThat(exception).hasMessage(message);
        assertThat(exception).hasCause(cause);
    }

}
