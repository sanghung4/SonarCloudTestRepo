package com.reece.punchoutcustomersync.service;

import com.jcraft.jsch.*;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.Properties;

@Slf4j
@Service
public class SftpDataService {

    @Value("${com.reece.sftp.host}")
    private String host;

    @Value("${com.reece.sftp.port}")
    private Integer port;

    @Value("${com.reece.sftp.username}")
    private String username;

    @Value("${com.reece.sftp.password:#{null}}")
    private String password;

    @Value("${com.reece.sftp.sessionTimeoutMs}")
    private Integer sessionTimeout;

    @Value("${com.reece.sftp.channelTimeoutMs}")
    private Integer channelTimeout;

    @Value("${com.reece.sftp.remoteFilePath}")
    private String remoteFilePath;

    @Autowired(required = false)
    private String privateKeyPath;

    @Autowired
    private JSch jSch;

    public String uploadCsv(InputStream csv, String fileName) throws IOException, JSchException, SftpException {
        String uploadPath = String.format("%s%s%s", remoteFilePath, File.separator, fileName);
        log.info("Uploading {} to upload path {}.", fileName, uploadPath);

        // Create the SFTP Channel:
        ChannelSftp channelSftp = createChannelSftp();

        // Upload the CSV file to the upload directory
        channelSftp.put(csv, uploadPath);

        // Disconnect the SFTP channel and session
        disconnectChannelSftp(channelSftp);

        return uploadPath;
    }

    private ChannelSftp createChannelSftp() throws JSchException {
        if (privateKeyPath != null) {
            jSch.addIdentity(privateKeyPath);
        }
        Session session = jSch.getSession(username, host, port);
        if (password != null) {
            session.setPassword(password);
        }
        Properties properties = new Properties();
        properties.put("StrictHostKeyChecking", "no");
        session.setConfig(properties);
        session.connect(sessionTimeout);
        Channel channel = session.openChannel("sftp");
        channel.connect(channelTimeout);
        return (ChannelSftp) channel;
    }

    private void disconnectChannelSftp(ChannelSftp channelSftp) throws JSchException {
        if (channelSftp == null)
            return;

        if (channelSftp.isConnected())
            channelSftp.disconnect();

        if (channelSftp.getSession() != null)
            channelSftp.getSession().disconnect();
    }
}
