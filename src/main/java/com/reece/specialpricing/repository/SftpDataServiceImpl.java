package com.reece.specialpricing.repository;

import com.jcraft.jsch.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.util.Properties;

@Service("sftpUploadService")
public class SftpDataServiceImpl implements FileUploadDataService {

    @Value("${com.reece.sftp.host}")
    private String host;

    @Value("${com.reece.sftp.port}")
    private Integer port;

    @Value("${com.reece.sftp.username}")
    private String username;

    @Value("${com.reece.sftp.sessionTimeoutMs}")
    private Integer sessionTimeout;

    @Value("${com.reece.sftp.channelTimeoutMs}")
    private Integer channelTimeout;

    @Value("${com.reece.sftp.remoteFilePath}")
    private String remoteFilePath;

    @Autowired
    private JSch jSch;

    @Autowired
    private String privateKeyPath;

    public String uploadFile(byte[] writableBytes, String fileName) throws Exception{
        ChannelSftp channelSftp;
        String uploadPath = remoteFilePath + '/' + fileName;
        OutputStream outputStream = null;
        try {
            channelSftp = createChannelSftp();

            outputStream = channelSftp.put(uploadPath);
            outputStream.write(writableBytes);

            disconnectChannelSftp(channelSftp);
        } catch (Exception e) {
            // Exception when handling file upload
            throw e;
        } finally {
            writableBytes = null;
            if(outputStream!=null)
                outputStream.close();
        }

        return uploadPath;
    }

    private ChannelSftp createChannelSftp() throws JSchException {
        jSch.addIdentity(privateKeyPath);
        Session session = jSch.getSession(username, host, port);
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
