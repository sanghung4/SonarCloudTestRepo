package com.reece.platform.eclipse.service.EclipseService;

import com.jcraft.jsch.*;
import com.reece.platform.eclipse.exceptions.EclipseLoadCountsException;
import com.reece.platform.eclipse.model.DTO.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.MimeTypeUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class FileTransferServiceTest {

    private final String HOST = "host";
    private final int PORT = 22;
    private final String USERNAME = "username";
    private final String PASSWORD = "password";
    private final String REMOTE_FILE_PATH = "remoteFilePath";
    private final int SESSION_TIMEOUT = 20;
    private final int CHANNEL_TIMEOUT = 20;

    private FileTransferService fileTransferService;
    private JobFormDTO jobFormDTO;

    @MockBean
    private JSch jSch;

    private ChannelSftp channel;

    private Session session;

    private final String PRIVATE_KEY_PATH = "PATH";

    @BeforeEach
    public void setup() throws Exception {
        jSch = mock(JSch.class);

        session = mock(Session.class);
        channel = mock(ChannelSftp.class);
        when(session.openChannel("sftp")).thenReturn(channel);
        when(jSch.getSession(USERNAME, HOST, PORT)).thenReturn(session);
        when(channel.getSession()).thenReturn(session);
        when(channel.isConnected()).thenReturn(true);

        fileTransferService = new FileTransferService(jSch, PRIVATE_KEY_PATH);
        jobFormDTO = new JobFormDTO();
        jobFormDTO.setBonding(new BondingDTO());
        jobFormDTO.setGeneralContractor(new GeneralContractorDTO());
        jobFormDTO.setJob(new JobDTO());
        jobFormDTO.setLender(new LenderDTO());
        jobFormDTO.setOwner(new OwnerDTO());
        jobFormDTO.setProject(new ProjectDTO());
        jobFormDTO.setFile("file");
        jobFormDTO.setFileMimeType(MimeTypeUtils.IMAGE_GIF_VALUE);
        ReflectionTestUtils.setField(fileTransferService, "host", HOST);
        ReflectionTestUtils.setField(fileTransferService, "port", PORT);
        ReflectionTestUtils.setField(fileTransferService, "username", USERNAME);
        ReflectionTestUtils.setField(fileTransferService, "remoteFilePath", REMOTE_FILE_PATH);
        ReflectionTestUtils.setField(fileTransferService, "sessionTimeout", SESSION_TIMEOUT);
        ReflectionTestUtils.setField(fileTransferService, "channelTimeout", CHANNEL_TIMEOUT);
    }

    @Test
    void uploadJobForm_success() throws Exception {
        fileTransferService.uploadJobForm(jobFormDTO);
        verify(channel, times(2)).put(anyString(), eq(REMOTE_FILE_PATH));
    }

    @Test
    void uploadJobForm_JSchException() throws Exception {
        when(jSch.getSession(USERNAME, HOST, PORT)).thenThrow(new JSchException());
        assertThrows(JSchException.class, () -> fileTransferService.uploadJobForm(jobFormDTO));
    }

    @Test
    void uploadJobForm_SftpException() throws Exception {
        doThrow(new SftpException(0, "")).when(channel).put(anyString(), eq(REMOTE_FILE_PATH));
        assertThrows(SftpException.class, () -> fileTransferService.uploadJobForm(jobFormDTO));
        verify(channel, times(1)).disconnect();
        verify(session, times(1)).disconnect();
    }

    @Test
    void downloadLatestEclipseCountsFile_success() throws Exception {
        when(channel.ls("/outgoing")).thenReturn(new Vector());
       var response= fileTransferService.downloadLatestEclipseCountsFile();
       assertNotNull(response);
    }

    @Test
    void downloadLatestEclipseCountsFile_Success() throws Exception {
        Vector vector =new Vector();

        ChannelSftp.LsEntry entry = mock(ChannelSftp.LsEntry.class);
        vector.add(entry);
        vector.add(entry);
        vector.add(entry);
        when(entry.getFilename()).thenReturn("PhysInvDump");
        when(channel.ls("/outgoing")).thenReturn(vector);
        when(channel.get("/outgoing/" + entry.getFilename())).thenReturn( new ByteArrayInputStream("value1,value2,value3".getBytes()));

        var response= fileTransferService.downloadLatestEclipseCountsFile();
        assertNotNull(response);
    }

    @Test
    void downloadLatestEclipseCountsFile_Error() throws Exception {
        Vector vector =new Vector();

        ChannelSftp.LsEntry entry = mock(ChannelSftp.LsEntry.class);
        vector.add(entry);
        vector.add(entry);
        vector.add(entry);
        when(entry.getFilename()).thenReturn("PhysInvDump");
        when(channel.ls("/outgoing")).thenReturn(vector);
        assertThrows(Exception.class,()->fileTransferService.downloadLatestEclipseCountsFile());

    }

    @Test
    void downloadLatestEclipseCountsFile_failure() throws Exception {
        when(channel.ls("/outgoing")).thenThrow(new SftpException(123,"error"));
        assertThrows(EclipseLoadCountsException.class,()->fileTransferService.downloadLatestEclipseCountsFile());
    }

}