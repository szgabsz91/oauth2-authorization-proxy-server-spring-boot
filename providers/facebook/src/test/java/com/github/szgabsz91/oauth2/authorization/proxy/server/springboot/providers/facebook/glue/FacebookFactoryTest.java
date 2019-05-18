package com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.facebook.glue;

import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.facebook.configuration.FacebookOAuth2ProviderAutoConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FacebookOAuth2ProviderAutoConfiguration.class)
public class FacebookFactoryTest {

    @Autowired
    private FacebookFactory facebookFactory;

    @Test
    public void testGetInstance() {
        var result = this.facebookFactory.getInstance();
        assertThat(result).isNotNull();
    }

}
