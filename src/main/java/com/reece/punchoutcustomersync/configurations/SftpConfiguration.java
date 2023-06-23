package com.reece.punchoutcustomersync.configurations;

import com.jcraft.jsch.JSch;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.swing.filechooser.FileSystemView;

@Configuration
public class SftpConfiguration {

    @Value("${com.reece.sftp.privateKeyName:#{null}}")
    private String privateKeyPath;

    @Bean
    public JSch jSch() { return new JSch(); }

    @Bean
    public String privateKeyPath() {
        return privateKeyPath != null ? getClass().getClassLoader().getResource(privateKeyPath).getPath() : null;
    }

    @Bean
    public FileSystemView fileSystemView() {
        return FileSystemView.getFileSystemView();
    }
}
