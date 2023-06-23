package com.reece.punchoutcustomersync.service;

import com.jcraft.jsch.*;
import com.opencsv.CSVWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.*;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SftpDataServiceTest {

    @Mock
    private JSch jSch;

    @InjectMocks
    private SftpDataService subject;

    @Mock
    private ChannelSftp channel;

    @Mock
    private Session session;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(subject, "host", "http:localhost");
        ReflectionTestUtils.setField(subject, "port", 22);
        ReflectionTestUtils.setField(subject, "username", "test");
        ReflectionTestUtils.setField(subject, "password", "Password");
        ReflectionTestUtils.setField(subject, "sessionTimeout", 1500);
        ReflectionTestUtils.setField(subject, "channelTimeout", 1500);
        ReflectionTestUtils.setField(subject, "remoteFilePath", "/test");
        ReflectionTestUtils.setField(subject, "sessionTimeout", 1500);
    }

    @Test
    public void testShouldNotSetPasswordWhenNull() throws JSchException, SftpException, IOException {
        // Given
        InputStream data = new ByteArrayInputStream(new byte[0]);
        String filename = "test.csv";

        // When
        ReflectionTestUtils.setField(subject, "privateKeyPath", "./resources/sftp/eclipse_test");
        ReflectionTestUtils.setField(subject, "password", null);

        // and when
        doNothing().when(jSch).addIdentity(anyString());
        when(jSch.getSession(any(), any(), anyInt())).thenReturn(session);
        when(session.openChannel(anyString())).thenReturn(channel);

        // then
        String path = subject.uploadCsv(data, filename);

        // verify
        assertThat(path, equalTo(path));
        verify(channel, times(1)).put(any(InputStream.class), eq(path));
        verify(session, times(0)).setPassword(anyString());
    }

    @Test
    public void testShouldNotAddPrivateKeyWhenNull() throws JSchException, SftpException, IOException {
        // Given
        InputStream data = new ByteArrayInputStream(new byte[0]);
        String filename = "test.csv";

        // When
        ReflectionTestUtils.setField(subject, "privateKeyPath", null);
        ReflectionTestUtils.setField(subject, "password", "password");

        // and when
        when(jSch.getSession(any(), any(), anyInt())).thenReturn(session);
        when(session.openChannel(anyString())).thenReturn(channel);

        // then
        String path = subject.uploadCsv(data, filename);

        // verify
        assertThat(path, equalTo("/test/test.csv"));
        verify(channel, times(1)).put(any(InputStream.class), eq(path));
        verify(session, times(1)).setPassword(anyString());
        verify(jSch, times(0)).addIdentity(anyString());
    }
}
