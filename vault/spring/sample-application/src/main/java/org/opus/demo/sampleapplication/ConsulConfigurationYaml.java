package org.opus.demo.sampleapplication;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("myapp")
@RefreshScope
public class ConsulConfigurationYaml {
    private String key1;
    private String key2;

    public String getKey1() {
        return key1;
    }

    public void setKey1(String key1) {
        System.out.println("Setting key1: " + key1);
        this.key1 = key1;
    }

    public String getKey2() {
        return key2;
    }

    @Value("${myapp.subkeys.key2}")
    public void setKey2(String key2) {
        System.out.println("Setting key2: " + key2);
        this.key2 = key2;
    }
}
