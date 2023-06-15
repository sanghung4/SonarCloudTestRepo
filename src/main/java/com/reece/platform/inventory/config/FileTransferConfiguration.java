package com.reece.platform.inventory.config;

import com.jcraft.jsch.JSch;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.client.RestTemplate;

@Configuration
public class FileTransferConfiguration {

    @Value("${sftp.privateKeyName}")
    private String privateKeyName;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public JSch jSch() {
        return new JSch();
    }

    @Bean
    public String privateKeyPath() {
        return getClass().getResource(privateKeyName).getPath();
    }
}
