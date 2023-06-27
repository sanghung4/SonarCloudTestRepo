package com.reece.specialpricing.configuration;

import com.jcraft.jsch.JSch;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//not used yet because we don't have an SFTP drive to connect to, but code pulled from
//https://github.com/morsco-reece/inventory-core-service/blob/696e92c6e965a18f5615f4b46e7777d1447f313f/src/main/java/com/reece/platform/inventory/config/FileTransferConfiguration.java

@Configuration
public class SftpConfiguration {
    @Value("${com.reece.sftp.privateKeyName}")
    private String privateKeyPath;

    @Bean
    public JSch jSch() { return new JSch(); }

    @Bean
    public String privateKeyPath() { return getClass().getClassLoader().getResource(privateKeyPath).getPath(); }
}
